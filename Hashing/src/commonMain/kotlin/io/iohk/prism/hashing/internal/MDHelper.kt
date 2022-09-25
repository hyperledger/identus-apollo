package io.iohk.prism.hashing.internal

abstract class MDHelper(
    private val littleEndian: Boolean,
    lenlen: Int,
    private val fbyte: Byte = 0x80.toByte()
) : HashingBase() {
    private val countBuf: ByteArray = ByteArray(lenlen)

    /**
     * Compute the padding. The padding data is input into the engine,
     * which is flushed.
     */
    fun makeMDPadding() {
        val dataLen = flush()
        val blen = blockLength
        var currentLength = blockCount * blen.toLong()
        currentLength = (currentLength + dataLen.toLong()) * 8L
        val lenlen = countBuf.size
        if (littleEndian) {
            MathHelper.encodeLEInt(currentLength.toInt(), countBuf, 0)
            MathHelper.encodeLEInt((currentLength ushr 32).toInt(), countBuf, 4)
        } else {
            MathHelper.encodeBEInt(
                (currentLength ushr 32).toInt(),
                countBuf, lenlen - 8
            )
            MathHelper.encodeBEInt(
                currentLength.toInt(),
                countBuf, lenlen - 4
            )
        }
        val endLen = dataLen + lenlen + blen and (blen - 1).inv()
        update(fbyte)
        repeat(endLen - lenlen - dataLen - 1) {
            update(0.toByte())
        }
        update(countBuf)
    }
}
