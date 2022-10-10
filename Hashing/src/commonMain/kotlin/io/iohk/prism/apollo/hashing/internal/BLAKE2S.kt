package io.iohk.prism.apollo.hashing.internal

open class BLAKE2S : Digest {
    private var digestSize = 32
    private var keyLength = 0
    private var salt: ByteArray? = null
    private var personalization: ByteArray? = null
    private var key: ByteArray? = null
    private var fanout = 1
    private var depth = 1
    private var leafLength = 0
    private var nodeOffset = 0L
    private var nodeDepth = 0
    private var innerHashLength = 0
    /**
     * Whenever this buffer overflows, it will be processed in the compress()
     * function. For performance issues, long messages will not use this buffer.
     */
    private var buffer: ByteArray? = null
    /**
     * Position of last inserted byte
     */
    private var bufferPos = 0 // a value from 0 up to BLOCK_LENGTH_BYTES
    /**
     * Internal state, in the BLAKE2 paper it is called v
     */
    private var internalState = IntArray(16)
    /**
     * State vector, in the BLAKE2 paper it is called h
     */
    private var chainValue: IntArray? = null
    /**
     * holds least significant bits of counter
     */
    private var t0 = 0
    /**
     * holds most significant bits of counter
     */
    private var t1 = 0
    /**
     * finalization flag, for last block: ~0
     */
    private var f0 = 0
    override val digestLength: Int
        get() = digestSize
    override val blockLength: Int
        get() = byteLength // 64

    /**
     * BLAKE2s for hashing.
     *
     * @param digestBits the desired digest length in bits. Must be a multiple of 8 and less than 256.
     * @throws [IllegalArgumentException]
     */
    @Throws(IllegalArgumentException::class)
    constructor(digestBits: Int = 256) {
        if (digestBits < 8 || digestBits > 256 || digestBits % 8 != 0) {
            throw IllegalArgumentException(
                "BLAKE2s digest bit length must be a multiple of 8 and not greater than 256"
            )
        }
        digestSize = digestBits / 8
        init(null, null, null)
    }

    /**
     * BLAKE2s for authentication ("Prefix-MAC mode").
     *
     *
     * After calling the doFinal() method, the key will remain to be used for
     * further computations of this instance. The key can be overwritten using
     * the clearKey() method.
     *
     * @param key a key up to 32 bytes or null
     */
    constructor(key: ByteArray?) {
        init(null, null, key)
    }

    /**
     * BLAKE2s with key, required digest length, salt and personalization.
     *
     *
     * After calling the doFinal() method, the key, the salt and the personal
     * string will remain and might be used for further computations with this
     * instance. The key can be overwritten using the clearKey() method, the
     * salt (pepper) can be overwritten using the clearSalt() method.
     *
     * @param key a key up to 32 bytes or null
     * @param digestBytes from 1 up to 32 bytes
     * @param salt 8 bytes or null
     * @param personalization 8 bytes or null
     * @Throws(IllegalArgumentException::class)
     */
    @Throws(IllegalArgumentException::class)
    constructor(
        key: ByteArray?,
        digestBytes: Int,
        salt: ByteArray?,
        personalization: ByteArray?
    ) {
        if (digestBytes < 1 || digestBytes > 32) {
            throw IllegalArgumentException(
                "Invalid digest length (required: 1 - 32)"
            )
        }
        digestSize = digestBytes
        init(salt, personalization, key)
    }

    private constructor(digestBytes: Int, key: ByteArray?, salt: ByteArray?, personalization: ByteArray?, offset: Long) {
        digestSize = digestBytes
        nodeOffset = offset
        init(salt, personalization, key)
    }

    private constructor(digestBytes: Int, hashLength: Int, offset: Long) {
        digestSize = digestBytes
        nodeOffset = offset
        fanout = 0
        depth = 0
        leafLength = hashLength
        innerHashLength = hashLength
        nodeDepth = 0
        init(null, null, null)
    }

    /**
     * Overwrite the key if it is no longer used (zeroization).
     */
    fun clearKey() {
        if (key != null) {
            key?.fill(0)
            buffer?.fill(0)
        }
    }

    /**
     * Overwrite the salt (pepper) if it is secret and no longer used
     * (zeroization).
     */
    fun clearSalt() {
        if (salt != null) {
            salt?.fill(0)
        }
    }

    /**
     * Close the digest, producing the final digest value. The doFinal() call
     * leaves the digest reset. Key, salt and personal string remain.
     *
     * @param out       the array the digest is to be copied into.
     * @param outOffset the offset into the out array the digest is to start at.
     */
    fun doFinal(out: ByteArray, outOffset: Int): Int {
        f0 = -0x1
        t0 += bufferPos
        // bufferPos may be < 64, so (t0 == 0) does not work
        // for 2^32 < message length > 2^32 - 63
        if (t0 < 0 && bufferPos > -t0) {
            t1++
        }
        compress(buffer, 0)
        buffer?.fill(0) // Holds eventually the key if input is null
        internalState.fill(0)
        var i = 0
        while (i < chainValue!!.size && i * 4 < digestSize) {
            val bytes = ByteArray(4)
            MathHelper.encodeLEInt(chainValue!![i], bytes, 0)
            if (i * 4 < digestSize - 4) {
                bytes.copyInto(out, outOffset + i * 4, 0, 4)
            } else {
                bytes.copyInto(out, outOffset + i * 4, 0, digestSize - i * 4)
            }
            i++
        }
        chainValue?.fill(0)
        reset()
        return digestSize
    }

    override fun update(input: ByteArray) {
        update(input, 0, input.size)
    }

    /**
     * Update the message digest with a single byte.
     *
     * @param input the input byte to be entered.
     */
    override fun update(input: Byte) {
        // process the buffer if full else add to buffer:
        val remainingLength: Int = byteLength - bufferPos // left bytes of buffer
        if (remainingLength == 0) { // full buffer
            t0 += byteLength
            if (t0 == 0) { // if message > 2^32
                t1++
            }
            compress(buffer, 0)
            buffer?.fill(0) // clear buffer
            buffer!![0] = input
            bufferPos = 1
        } else {
            buffer!![bufferPos] = input
            bufferPos++
        }
    }

    /**
     * Update the message digest with a block of bytes.
     *
     * @param input the byte array containing the data.
     * @param offset  the offset into the byte array where the data starts.
     * @param length     the length of the data.
     */
    override fun update(input: ByteArray, offset: Int, length: Int) {
        if (length == 0) {
            return
        }
        var remainingLength = 0 // left bytes of buffer
        if (bufferPos != 0) { // commenced, incomplete buffer

            // complete the buffer:
            remainingLength = byteLength - bufferPos
            if (remainingLength < length) { // full buffer + at least 1 byte
                input.copyInto(buffer!!, bufferPos, offset, offset + remainingLength)
                t0 += byteLength
                if (t0 == 0) { // if message > 2^32
                    t1++
                }
                compress(buffer, 0)
                bufferPos = 0
                buffer?.fill(0) // clear buffer
            } else {
                input.copyInto(buffer!!, bufferPos, offset, offset + length)
                bufferPos += length
                return
            }
        }

        // process blocks except last block (also if last block is full)
        val blockWiseLastPos = offset + length - byteLength
        var messagePos: Int = offset + remainingLength
        while (messagePos < blockWiseLastPos) {
            // block wise 64 bytes
            // without buffer:
            t0 += byteLength
            if (t0 == 0) {
                t1++
            }
            compress(input, messagePos)
            messagePos += byteLength
        }

        // fill the buffer with left bytes, this might be a full block
        input.copyInto(buffer!!, 0, messagePos, offset + length)
        bufferPos += offset + length - messagePos
    }

    override fun digest(): ByteArray {
        val digest = ByteArray(digestSize)
        doFinal(digest, 0)
        return digest
    }

    override fun digest(input: ByteArray): ByteArray {
        update(input)
        return digest()
    }

    @Throws(IllegalArgumentException::class)
    override fun digest(output: ByteArray, offset: Int, length: Int): Int {
        val digest = digest()

        if (length < digest.size) throw IllegalArgumentException("partial digests not returned")
        if (output.size - offset < digest.size) throw IllegalArgumentException("insufficient space in the output buffer to store the digest")

        digest.copyInto(output, offset, 0, digest.size)

        return digest.size
    }

    /**
     * Reset the digest back to its initial state. The key, the salt and the
     * personal string will remain for further computations.
     */
    override fun reset() {
        bufferPos = 0
        f0 = 0
        t0 = 0
        t1 = 0
        chainValue = null
        buffer?.fill(0)
        if (key != null) {
            key!!.copyInto(buffer!!, 0, 0, key!!.size)
            bufferPos = byteLength // zero padding
        }
        init(salt, personalization, key)
    }

    override fun toString() = "BLAKE2s"

    private fun initializeInternalState() {
        // initialize v:
        chainValue!!.copyInto(internalState, 0, 0, chainValue!!.size)
        blake2s_IV.copyInto(internalState, chainValue!!.size, 0, 4)
        internalState[12] = t0 xor blake2s_IV[4]
        internalState[13] = t1 xor blake2s_IV[5]
        internalState[14] = f0 xor blake2s_IV[6]
        internalState[15] = blake2s_IV[7] // ^ f1 with f1 = 0
    }

    @Throws(IllegalArgumentException::class)
    private fun init(salt: ByteArray?, personalization: ByteArray?, key: ByteArray?) {
        buffer = ByteArray(byteLength)
        if (key != null && key.isNotEmpty()) {
            if (key.size > 32) {
                throw IllegalArgumentException(
                    "Keys > 32 bytes are not supported"
                )
            }
            this.key = key.copyInto(ByteArray(key.size), 0, 0, key.size)
            keyLength = key.size
            key.copyInto(buffer!!, 0, 0, key.size)
            bufferPos = byteLength // zero padding
        }
        if (chainValue == null) {
            chainValue = IntArray(8)
            chainValue!![0] = (
                blake2s_IV[0]
                    xor (digestSize or (keyLength shl 8) or (fanout shl 16 or (depth shl 24)))
                )
            chainValue!![1] = blake2s_IV[1] xor leafLength
            val nofHi = (nodeOffset shr 32).toInt()
            val nofLo = nodeOffset.toInt()
            chainValue!![2] = blake2s_IV[2] xor nofLo
            chainValue!![3] = blake2s_IV[3] xor (
                nofHi or
                    (nodeDepth shl 16) or (innerHashLength shl 24)
                )
            chainValue!![4] = blake2s_IV[4]
            chainValue!![5] = blake2s_IV[5]
            if (salt != null) {
                if (salt.size != 8) {
                    throw IllegalArgumentException(
                        "Salt length must be exactly 8 bytes"
                    )
                }
                this.salt = salt.copyInto(ByteArray(8), 0, 0, salt.size)
                chainValue!![4] = chainValue!![4] xor MathHelper.decodeLEInt(salt, 0)
                chainValue!![5] = chainValue!![5] xor MathHelper.decodeLEInt(salt, 4)
            }
            chainValue!![6] = blake2s_IV[6]
            chainValue!![7] = blake2s_IV[7]
            if (personalization != null) {
                if (personalization.size != 8) {
                    throw IllegalArgumentException(
                        "Personalization length must be exactly 8 bytes"
                    )
                }
                this.personalization = personalization.copyInto(ByteArray(8), 0, 0, personalization.size)
                chainValue!![6] = chainValue!![6] xor MathHelper.decodeLEInt(personalization, 0)
                chainValue!![7] = chainValue!![7] xor MathHelper.decodeLEInt(personalization, 4)
            }
        }
    }

    private fun compress(message: ByteArray?, messagePos: Int) {
        initializeInternalState()
        val m = IntArray(16)
        for (j in 0..15) {
            m[j] = MathHelper.decodeLEInt(message!!, messagePos + j * 4)
        }
        for (round in 0 until ROUNDS) {

            // G apply to columns of internalState:m[blake2s_sigma[round][2 *
            // blockPos]] /+1
            g(m[blake2s_sigma[round][0].toInt()], m[blake2s_sigma[round][1].toInt()], 0, 4, 8, 12)
            g(m[blake2s_sigma[round][2].toInt()], m[blake2s_sigma[round][3].toInt()], 1, 5, 9, 13)
            g(m[blake2s_sigma[round][4].toInt()], m[blake2s_sigma[round][5].toInt()], 2, 6, 10, 14)
            g(m[blake2s_sigma[round][6].toInt()], m[blake2s_sigma[round][7].toInt()], 3, 7, 11, 15)
            // G apply to diagonals of internalState:
            g(m[blake2s_sigma[round][8].toInt()], m[blake2s_sigma[round][9].toInt()], 0, 5, 10, 15)
            g(m[blake2s_sigma[round][10].toInt()], m[blake2s_sigma[round][11].toInt()], 1, 6, 11, 12)
            g(m[blake2s_sigma[round][12].toInt()], m[blake2s_sigma[round][13].toInt()], 2, 7, 8, 13)
            g(m[blake2s_sigma[round][14].toInt()], m[blake2s_sigma[round][15].toInt()], 3, 4, 9, 14)
        }

        // update chain values:
        for (offset in chainValue!!.indices) {
            chainValue!![offset] = chainValue!![offset] xor internalState[offset] xor internalState[offset + 8]
        }
    }

    private fun g(m1: Int, m2: Int, posA: Int, posB: Int, posC: Int, posD: Int) {
        internalState[posA] = internalState[posA] + internalState[posB] + m1
        internalState[posD] = rotr32(internalState[posD] xor internalState[posA], 16)
        internalState[posC] = internalState[posC] + internalState[posD]
        internalState[posB] = rotr32(internalState[posB] xor internalState[posC], 12)
        internalState[posA] = internalState[posA] + internalState[posB] + m2
        internalState[posD] = rotr32(internalState[posD] xor internalState[posA], 8)
        internalState[posC] = internalState[posC] + internalState[posD]
        internalState[posB] = rotr32(internalState[posB] xor internalState[posC], 7)
    }

    private fun rotr32(x: Int, rot: Int): Int {
        return x ushr rot or (x shl 32 - rot)
    }

    companion object {
        /**
         * BLAKE2s Initialization Vector
         */
        private val blake2s_IV = intArrayOf(
            0x6a09e667, -0x4498517b, 0x3c6ef372,
            -0x5ab00ac6, 0x510e527f, -0x64fa9774,
            0x1f83d9ab, 0x5be0cd19
        )

        /**
         * Message word permutations
         */
        private val blake2s_sigma = arrayOf(
            byteArrayOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15),
            byteArrayOf(14, 10, 4, 8, 9, 15, 13, 6, 1, 12, 0, 2, 11, 7, 5, 3),
            byteArrayOf(11, 8, 12, 0, 5, 2, 15, 13, 10, 14, 3, 6, 7, 1, 9, 4),
            byteArrayOf(7, 9, 3, 1, 13, 12, 11, 14, 2, 6, 5, 10, 4, 0, 15, 8),
            byteArrayOf(9, 0, 5, 7, 2, 4, 10, 15, 14, 1, 11, 12, 6, 8, 3, 13),
            byteArrayOf(2, 12, 6, 10, 0, 11, 8, 3, 4, 13, 7, 5, 15, 14, 1, 9),
            byteArrayOf(12, 5, 1, 15, 14, 13, 4, 10, 0, 7, 6, 3, 9, 2, 8, 11),
            byteArrayOf(13, 11, 7, 14, 12, 1, 3, 9, 5, 0, 15, 4, 8, 6, 2, 10),
            byteArrayOf(6, 15, 14, 9, 11, 3, 0, 8, 12, 2, 13, 7, 1, 4, 10, 5),
            byteArrayOf(10, 2, 8, 4, 7, 6, 1, 5, 15, 11, 9, 14, 3, 12, 13, 0)
        )
        private const val ROUNDS = 10 // to use for Catenas H'

        /**
         * Return the size in bytes of the internal buffer the digest applies its
         * compression function to.
         *
         * @return byte length of the digest's internal buffer.
         */
        val byteLength
            get() = 64
    }
}