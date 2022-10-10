package io.iohk.prism.apollo.hashing.internal

import kotlin.jvm.JvmOverloads

/**
 * [HMAC] is defined in RFC 2104 (also FIPS 198a).
 */
class HMAC @JvmOverloads constructor(dig: Digest, key: ByteArray, outputLength: Int? = null) : HashingBase() {

    private var dig: Digest
    private var outputLength: Int
    private var tmpOut: ByteArray
    private var onlyThis = 0
    private lateinit var kipad: ByteArray
    private lateinit var kopad: ByteArray
    override val digestLength: Int
        get() = if (outputLength < 0) dig.digestLength else outputLength
    override val blockLength: Int
        get() = 64

    /**
     *
     *
     * @param dig the underlying hash function
     * @param key the MAC key
     * @param outputLength (optional) the HMAC output length (in bytes)
     */
    init {
        var key1 = key
        dig.reset()
        this.dig = dig
        var b = dig.blockLength
        if (b < 0) {
            val n = -b
            b = n * ((key1.size + (n - 1)) / n)
        }
        val keyB = ByteArray(b)
        var len = key1.size
        if (len > b) {
            key1 = dig.digest(key1)
            len = key1.size
            if (len > b) len = b
        }
        key1.copyInto(keyB, 0, 0, len)
        processKey(keyB)
        this.outputLength = -1
        tmpOut = ByteArray(dig.digestLength)
        reset()

        if (outputLength != null && outputLength < dig.digestLength) {
            this.outputLength = outputLength
        }
    }

    override fun doInit() {
        // we don't need to do anything here
    }

    override fun engineReset() {
        dig.reset()
        dig.update(kipad)
    }

    override fun doPadding(output: ByteArray, outputOffset: Int) {
        onlyThis = flush()
        if (onlyThis > 0) update(zeroPad, 0, 64 - onlyThis)
        var olen = tmpOut.size
        dig.digest(tmpOut, 0, olen)
        dig.update(kopad)
        dig.update(tmpOut)
        dig.digest(tmpOut, 0, olen)
        if (outputLength >= 0) olen = outputLength
        tmpOut.copyInto(output, outputOffset, 0, olen)
    }

    override fun processBlock(data: ByteArray) {
        if (onlyThis > 0) {
            dig.update(data, 0, onlyThis)
            onlyThis = 0
        } else {
            dig.update(data)
        }
    }

    private fun processKey(keyB: ByteArray) {
        val b = keyB.size
        kipad = ByteArray(b)
        kopad = ByteArray(b)
        for (i in 0 until b) {
            val x = keyB[i].toInt()
            kipad[i] = (x xor 0x36).toByte()
            kopad[i] = (x xor 0x5C).toByte()
        }
    }

    override fun toString(): String {
        return "HMAC/$dig"
    }

    companion object {
        private val zeroPad = ByteArray(64)
    }
}
