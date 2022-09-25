package io.iohk.prism.hashing.internal

/**
 * An object containing some mathematical operation needed in the hashing algorithms.
 */
object MathHelper {
    /**
     * Encode the 32-bit word [value] into the array
     * [buf] at offset [off], in big-endian
     * convention (most significant byte first).
     *
     * @param value   the value to encode
     * @param buf   the destination buffer
     * @param off   the destination offset
     */
    fun encodeBEInt(value: Int, buf: ByteArray, off: Int) {
        buf[off + 0] = (value ushr 24).toByte()
        buf[off + 1] = (value ushr 16).toByte()
        buf[off + 2] = (value ushr 8).toByte()
        buf[off + 3] = value.toByte()
    }

    /**
     * Decode a 32-bit big-endian word from the array [buf]
     * at offset [off].
     *
     * @param buf   the source buffer
     * @param off   the source offset
     * @return the decoded value
     */
    fun decodeBEInt(buf: ByteArray, off: Int): Int {
        return (
            buf[off].toInt() and 0xFF shl 24
                or (buf[off + 1].toInt() and 0xFF shl 16)
                or (buf[off + 2].toInt() and 0xFF shl 8)
                or (buf[off + 3].toInt() and 0xFF)
            )
    }

    /**
     * Encode the 32-bit word [value] into the array
     * [buf] at offset [off], in little-endian
     * convention (least significant byte first).
     *
     * @param value   the value to encode
     * @param buf   the destination buffer
     * @param off   the destination offset
     */
    fun encodeLEInt(value: Int, buf: ByteArray, off: Int) {
        buf[off + 0] = value.toByte()
        buf[off + 1] = (value ushr 8).toByte()
        buf[off + 2] = (value ushr 16).toByte()
        buf[off + 3] = (value ushr 24).toByte()
    }

    /**
     * Perform a circular rotation by [n] to the left
     * of the 32-bit word [x]. The [n] parameter
     * must lie between 1 and 31 (inclusive).
     *
     * @param x   the value to rotate
     * @param n   the rotation count (between 1 and 31)
     * @return the rotated value
     */
    fun circularLeftInt(x: Int, n: Int): Int {
        return x.rotateLeft(n)
    }

    /**
     * Encode the 64-bit word [value] into the array
     * [buf] at offset [off], in big-endian
     * convention (most significant byte first).
     *
     * @param value   the value to encode
     * @param buf   the destination buffer
     * @param off   the destination offset
     */
    fun encodeBELong(value: Long, buf: ByteArray, off: Int) {
        buf[off + 0] = (value ushr 56).toByte()
        buf[off + 1] = (value ushr 48).toByte()
        buf[off + 2] = (value ushr 40).toByte()
        buf[off + 3] = (value ushr 32).toByte()
        buf[off + 4] = (value ushr 24).toByte()
        buf[off + 5] = (value ushr 16).toByte()
        buf[off + 6] = (value ushr 8).toByte()
        buf[off + 7] = value.toByte()
    }

    /**
     * Decode a 64-bit big-endian word from the array [buf]
     * at offset [off].
     *
     * @param buf   the source buffer
     * @param off   the source offset
     * @return the decoded value
     */
    fun decodeBELong(buf: ByteArray, off: Int): Long {
        return (
            (buf[off].toLong() and 0xFF) shl 56
                or ((buf[off + 1].toLong() and 0xFF) shl 48)
                or ((buf[off + 2].toLong() and 0xFF) shl 40)
                or ((buf[off + 3].toLong() and 0xFF) shl 32)
                or ((buf[off + 4].toLong() and 0xFF) shl 24)
                or ((buf[off + 5].toLong() and 0xFF) shl 16)
                or ((buf[off + 6].toLong() and 0xFF) shl 8)
                or (buf[off + 7].toLong() and 0xFF)
            )
    }

    /**
     * Perform a circular rotation by [n] to the left
     * of the 64-bit word [x]. The [n] parameter
     * must lie between 1 and 63 (inclusive).
     *
     * @param x   the value to rotate
     * @param n   the rotation count (between 1 and 63)
     * @return the rotated value
     */
    fun circularLeftLong(x: Long, n: Int): Long {
        return x.rotateLeft(n)
    }

    /**
     * Decode a 32-bit little-endian word from the array [buf]
     * at offset [off].
     *
     * @param buf   the source buffer
     * @param off   the source offset
     * @return the decoded value
     */
    fun decodeLEInt(buf: ByteArray, off: Int): Int {
        return (
            (buf[off + 3].toInt() and 0xFF shl 24)
                or (buf[off + 2].toInt() and 0xFF shl 16)
                or (buf[off + 1].toInt() and 0xFF shl 8)
                or (buf[off].toInt() and 0xFF)
            )
    }

    /**
     * Encode a 64-bit integer with little-endian convention.
     *
     * @param [value]   the integer to encode
     * @param dst   the destination buffer
     * @param off   the destination offset
     */
    fun encodeLELong(value: Long, dst: ByteArray, off: Int) {
        dst[off + 0] = value.toByte()
        dst[off + 1] = (value.toInt() ushr 8).toByte()
        dst[off + 2] = (value.toInt() ushr 16).toByte()
        dst[off + 3] = (value.toInt() ushr 24).toByte()
        dst[off + 4] = (value ushr 32).toByte()
        dst[off + 5] = (value ushr 40).toByte()
        dst[off + 6] = (value ushr 48).toByte()
        dst[off + 7] = (value ushr 56).toByte()
    }

    /**
     * Decode a 64-bit little-endian integer.
     *
     * @param buf   the source buffer
     * @param off   the source offset
     * @return the decoded integer
     */
    fun decodeLELong(buf: ByteArray, off: Int): Long {
        return (
            buf[off + 0].toLong() and 0xFF
                or ((buf[off + 1].toLong() and 0xFF) shl 8)
                or ((buf[off + 2].toLong() and 0xFF) shl 16)
                or ((buf[off + 3].toLong() and 0xFF) shl 24)
                or ((buf[off + 4].toLong() and 0xFF) shl 32)
                or ((buf[off + 5].toLong() and 0xFF) shl 40)
                or ((buf[off + 6].toLong() and 0xFF) shl 48)
                or ((buf[off + 7].toLong() and 0xFF) shl 56)
            )
    }

    /**
     * Perform a circular rotation by [n] to the right
     * of the 32-bit word [x]. The [n] parameter
     * must lie between 1 and 31 (inclusive).
     *
     * @param x   the value to rotate
     * @param n   the rotation count (between 1 and 31)
     * @return the rotated value
     */
    fun circularRightInt(x: Int, n: Int): Int {
        return x.rotateRight(n)
    }

    /**
     * Perform a circular rotation by [n] to the right
     * of the 64-bit word [x]. The [n] parameter
     * must lie between 1 and 63 (inclusive).
     *
     * @param x   the value to rotate
     * @param n   the rotation count (between 1 and 63)
     * @return the rotated value
     */
    internal fun circularRightLong(x: Long, n: Int): Long {
        return x.rotateRight(n)
    }
}
