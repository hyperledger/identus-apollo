package io.iohk.prism.hashing.internal

open class BLAKE2B: Digest {
    private var digestSize = 64
    private var keyLength = 0
    private var salt: ByteArray? = null
    private var personalization: ByteArray? = null
    private var key: ByteArray? = null
    private var buffer: ByteArray? = null
    private var bufferPos = 0
    private val internalState = LongArray(16)
    private var chainValue: LongArray? = null
    private var t0 = 0L
    private var t1 = 0L
    private var f0 = 0L
    override val digestLength: Int
        get() = digestSize
    override val blockLength: Int
        get() = 128

    /**
     * Basic sized constructor - size in bits.
     *
     * @param digestSize size of the digest in bits
     * @throws [IllegalArgumentException]
     */
    @Throws(IllegalArgumentException::class)
    constructor(digestSize: Int = 512) {
        if (digestSize < 8 || digestSize > 512 || digestSize % 8 != 0) {
            throw IllegalArgumentException(
                "BLAKE2b digest bit length must be a multiple of 8 and not greater than 512"
            )
        }
        buffer = ByteArray(blockLength)
        keyLength = 0
        this.digestSize = digestSize / 8
        init()
    }

    /**
     * Blake2b for authentication ("Prefix-MAC mode").
     * After calling the doFinal() method, the key will
     * remain to be used for further computations of
     * this instance.
     * The key can be overwritten using the clearKey() method.
     *
     * @param key A key up to 64 bytes or null
     * @throws [IllegalArgumentException]
     */
    @Throws(IllegalArgumentException::class)
    constructor(key: ByteArray?) {
        buffer = ByteArray(blockLength)
        if (key != null) {
            this.key = key.copyInto(ByteArray(key.size), 0, 0, key.size)
            if (key.size > 64) {
                throw IllegalArgumentException(
                    "Keys > 64 are not supported"
                )
            }
            keyLength = key.size
            key.copyInto(buffer!!, 0, 0, key.size)
            bufferPos = blockLength // zero padding
        }
        digestSize = 64
        init()
    }

    /**
     * Blake2b with key, required digest length (in bytes), salt and personalization.
     * After calling the doFinal() method, the key, the salt and the personal string
     * will remain and might be used for further computations with this instance.
     * The key can be overwritten using the clearKey() method, the salt (pepper)
     * can be overwritten using the clearSalt() method.
     *
     * @param key A key up to 64 bytes or null
     * @param digestSize size of the digest in bits
     * @param salt 16 bytes or null
     * @param personalization 16 bytes or null
     * @throws [IllegalArgumentException]
     */
    @Throws(IllegalArgumentException::class)
    constructor(key: ByteArray?, digestSize: Int, salt: ByteArray?, personalization: ByteArray?) {
        if (digestSize < 8 || digestSize > 512 || digestSize % 8 != 0) {
            throw IllegalArgumentException(
                "BLAKE2b digest bit length must be a multiple of 8 and not greater than 512"
            )
        }
        this.digestSize = digestSize / 8

        buffer = ByteArray(blockLength)
        if (salt != null) {
            if (salt.size != 16) {
                throw IllegalArgumentException(
                    "salt length must be exactly 16 bytes"
                )
            }
            this.salt = salt.copyInto(ByteArray(16), 0, 0, salt.size)
        }
        if (personalization != null) {
            if (personalization.size != 16) {
                throw IllegalArgumentException(
                    "personalization length must be exactly 16 bytes"
                )
            }
            this.personalization = personalization.copyInto(ByteArray(16), 0, 0, personalization.size)
        }
        if (key != null) {
            this.key = key.copyInto(ByteArray(key.size), 0, 0, key.size)
            if (key.size > 64) {
                throw IllegalArgumentException(
                    "Keys > 64 are not supported"
                )
            }
            keyLength = key.size
            key.copyInto(buffer!!, 0, 0, key.size)
            bufferPos = blockLength // zero padding
        }
        init()
    }

    /**
     * Overwrite the key
     * if it is no longer used (zeroization)
     */
    fun clearKey() {
        if (key != null) {
            key?.fill(0)
            buffer?.fill(0)
        }
    }

    /**
     * Overwrite the salt (pepper) if it
     * is secret and no longer used (zeroization)
     */
    fun clearSalt() {
        if (salt != null) {
            salt?.fill(0)
        }
    }

    /**
     * close the digest, producing the final digest value. The doFinal
     * call leaves the digest reset.
     * Key, salt and personal string remain.
     *
     * @param out the array the digest is to be copied into.
     * @param outOffset the offset into the out array the digest is to start at.
     */
    fun doFinal(out: ByteArray?, outOffset: Int): Int {
        f0 = -0x1L
        t0 += bufferPos.toLong()
        if (bufferPos > 0 && t0 == 0L) {
            t1++
        }
        compress(buffer!!, 0)
        buffer?.fill(0) // Holds eventually the key if input is null
        internalState.fill(0L)
        var i = 0
        while (i < chainValue!!.size && i * 8 < digestSize) {
            val bytes = ByteArray(8)
            MathHelper.encodeLELong(chainValue!![i], bytes, 0)
            if (i * 8 < digestSize - 8) {
                bytes.copyInto(out!!, outOffset + i * 8, 0, 8)
            } else {
                bytes.copyInto(out!!, outOffset + i * 8, 0, digestSize - i * 8)
            }
            i++
        }
        chainValue?.fill(0L)
        reset()
        return digestSize
    }

    override fun update(input: ByteArray) {
        update(input, 0, input.size)
    }

    /**
     * update the message digest with a single byte.
     *
     * @param input the input byte to be entered.
     */
    override fun update(input: Byte) {
        // process the buffer if full else add to buffer:
        val remainingLength = blockLength - bufferPos
        if (remainingLength == 0) {
            // full buffer
            t0 += blockLength.toLong()
            if (t0 == 0L) { // if message > 2^64
                t1++
            }
            compress(buffer!!, 0)
            buffer!!.fill(0) // clear buffer
            buffer!![0] = input
            bufferPos = 1
        } else {
            buffer!![bufferPos] = input
            bufferPos++
            return
        }
    }

    /**
     * update the message digest with a block of bytes.
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

        if (bufferPos != 0) {
            // commenced, incomplete buffer

            // complete the buffer:
            remainingLength = blockLength - bufferPos
            if (remainingLength < length) { // full buffer + at least 1 byte
                input.copyInto(buffer!!, bufferPos, offset, offset + remainingLength)
                t0 += blockLength.toLong()
                if (t0 == 0L) { // if message > 2^64
                    t1++
                }
                compress(buffer!!, 0)
                bufferPos = 0
                // clear buffer
                buffer?.fill(0)
            } else {
                input.copyInto(buffer!!, bufferPos, offset, offset + length)
                bufferPos += length
                return
            }
        }

        // process blocks except last block (also if last block is full)
        val blockWiseLastPos = offset + length - blockLength
        var messagePos: Int = offset + remainingLength
        while (messagePos < blockWiseLastPos) {
            // block wise 128 bytes
            // without buffer:
            t0 += blockLength.toLong()
            if (t0 == 0L) {
                t1++
            }
            compress(input, messagePos)
            messagePos += blockLength
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
     * Reset the digest back to it's initial state.
     * The key, the salt and the personal string will
     * remain for further computations.
     */
    override fun reset() {
        bufferPos = 0
        f0 = 0L
        t0 = 0L
        t1 = 0L
        chainValue = null
        buffer?.fill(0)
        if (key != null) {
            key!!.copyInto(buffer!!, 0, 0, key!!.size)
            bufferPos = blockLength // zero padding
        }
        init()
    }

    override fun toString(): String = "BLAKE2B"

    private fun initializeInternalState() {
        // initialize v:
        chainValue!!.copyInto(internalState, 0, 0, chainValue!!.size)
        blake2b_IV.copyInto(internalState, chainValue!!.size, 0, 4)

        internalState[12] = t0 xor blake2b_IV[4]
        internalState[13] = t1 xor blake2b_IV[5]
        internalState[14] = f0 xor blake2b_IV[6]
        internalState[15] = blake2b_IV[7] // ^ f1 with f1 = 0
    }

    private fun init() {
        if (chainValue == null) {
            chainValue = LongArray(8)
            chainValue!![0] = (blake2b_IV[0]
                    xor (digestSize.toLong() or (keyLength.toLong() shl 8) or 0x1010000L))
            // 0x1010000 = ((fanout << 16) | (depth << 24) | (leafLength <<
            // 32));
            // with fanout = 1; depth = 0; leafLength = 0;
            chainValue!![1] = blake2b_IV[1] // ^ nodeOffset; with nodeOffset = 0;
            chainValue!![2] = blake2b_IV[2] // ^ ( nodeDepth | (innerHashLength <<
            // 8) );
            // with nodeDepth = 0; innerHashLength = 0;
            chainValue!![3] = blake2b_IV[3]
            chainValue!![4] = blake2b_IV[4]
            chainValue!![5] = blake2b_IV[5]
            if (salt != null) {
                chainValue!![4] = chainValue!![4] xor MathHelper.decodeLELong(salt!!, 0)
                chainValue!![5] = chainValue!![5] xor MathHelper.decodeLELong(salt!!, 8)
            }
            chainValue!![6] = blake2b_IV[6]
            chainValue!![7] = blake2b_IV[7]
            if (personalization != null) {
                chainValue!![6] = chainValue!![6] xor MathHelper.decodeLELong(personalization!!, 0)
                chainValue!![7] = chainValue!![7] xor MathHelper.decodeLELong(personalization!!, 8)
            }
        }
    }

    private fun g(m1: Long, m2: Long, posA: Int, posB: Int, posC: Int, posD: Int) {
        internalState[posA] = internalState[posA] + internalState[posB] + m1
        internalState[posD] = MathHelper.circularRightLong(internalState[posD] xor internalState[posA], 32)
        internalState[posC] = internalState[posC] + internalState[posD]
        internalState[posB] = MathHelper.circularRightLong(internalState[posB] xor internalState[posC], 24) // replaces 25 of BLAKE
        internalState[posA] = internalState[posA] + internalState[posB] + m2
        internalState[posD] = MathHelper.circularRightLong(internalState[posD] xor internalState[posA], 16)
        internalState[posC] = internalState[posC] + internalState[posD]
        internalState[posB] = MathHelper.circularRightLong(internalState[posB] xor internalState[posC], 63) // replaces 11 of BLAKE
    }

    private fun compress(message: ByteArray, messagePos: Int) {
        initializeInternalState()
        val m = LongArray(16)
        for (j in 0..15) {
            m[j] = MathHelper.decodeLELong(message, messagePos + j * 8)
        }
        for (round in 0 until ROUNDS) {

            // G apply to columns of internalState:m[blake2b_sigma[round][2 *
            // blockPos]] /+1
            g(m[blake2b_sigma[round][0].toInt()], m[blake2b_sigma[round][1].toInt()], 0, 4, 8, 12)
            g(m[blake2b_sigma[round][2].toInt()], m[blake2b_sigma[round][3].toInt()], 1, 5, 9, 13)
            g(m[blake2b_sigma[round][4].toInt()], m[blake2b_sigma[round][5].toInt()], 2, 6, 10, 14)
            g(m[blake2b_sigma[round][6].toInt()], m[blake2b_sigma[round][7].toInt()], 3, 7, 11, 15)
            // G apply to diagonals of internalState:
            g(m[blake2b_sigma[round][8].toInt()], m[blake2b_sigma[round][9].toInt()], 0, 5, 10, 15)
            g(m[blake2b_sigma[round][10].toInt()], m[blake2b_sigma[round][11].toInt()], 1, 6, 11, 12)
            g(m[blake2b_sigma[round][12].toInt()], m[blake2b_sigma[round][13].toInt()], 2, 7, 8, 13)
            g(m[blake2b_sigma[round][14].toInt()], m[blake2b_sigma[round][15].toInt()], 3, 4, 9, 14)
        }

        // update chain values:
        for (offset in chainValue!!.indices) {
            chainValue!![offset] = chainValue!![offset] xor internalState[offset] xor internalState[offset + 8]
        }
    }

    class Keyed(
        key: ByteArray,
        salt: ByteArray? = null,
        personalisation: ByteArray? = null,
        outputSizeBits: Int = 512
    ): BLAKE2B(key, outputSizeBits shl 3, salt, personalisation)

    companion object {
        // Blake2b Initialization Vector:
        private val blake2b_IV = longArrayOf(
            0x6a09e667f3bcc908L, -0x4498517a7b3558c5L, 0x3c6ef372fe94f82bL,
            -0x5ab00ac5a0e2c90fL, 0x510e527fade682d1L, -0x64fa9773d4c193e1L,
            0x1f83d9abfb41bd6bL, 0x5be0cd19137e2179L
        )

        // Message word permutations:
        private val blake2b_sigma = arrayOf(
            byteArrayOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15),
            byteArrayOf(14, 10, 4, 8, 9, 15, 13, 6, 1, 12, 0, 2, 11, 7, 5, 3),
            byteArrayOf(11, 8, 12, 0, 5, 2, 15, 13, 10, 14, 3, 6, 7, 1, 9, 4),
            byteArrayOf(7, 9, 3, 1, 13, 12, 11, 14, 2, 6, 5, 10, 4, 0, 15, 8),
            byteArrayOf(9, 0, 5, 7, 2, 4, 10, 15, 14, 1, 11, 12, 6, 8, 3, 13),
            byteArrayOf(2, 12, 6, 10, 0, 11, 8, 3, 4, 13, 7, 5, 15, 14, 1, 9),
            byteArrayOf(12, 5, 1, 15, 14, 13, 4, 10, 0, 7, 6, 3, 9, 2, 8, 11),
            byteArrayOf(13, 11, 7, 14, 12, 1, 3, 9, 5, 0, 15, 4, 8, 6, 2, 10),
            byteArrayOf(6, 15, 14, 9, 11, 3, 0, 8, 12, 2, 13, 7, 1, 4, 10, 5),
            byteArrayOf(10, 2, 8, 4, 7, 6, 1, 5, 15, 11, 9, 14, 3, 12, 13, 0),
            byteArrayOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15),
            byteArrayOf(14, 10, 4, 8, 9, 15, 13, 6, 1, 12, 0, 2, 11, 7, 5, 3)
        )
        private const val ROUNDS = 12 // to use for Catenas H'
    }
}
