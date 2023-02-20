package io.iohk.atala.prism.apollo.utils

fun ByteArray.padStart(length: Int, padValue: Byte): ByteArray {
    return if (size >= length) {
        this
    } else {
        val result = ByteArray(length) { padValue }
        copyInto(result, length - size)
        result
    }
}

@OptIn(ExperimentalUnsignedTypes::class)
fun ByteArray.toHex(): String {
    val HEX_ARRAY = "0123456789abcdef".toCharArray()
    val ubytes = this.toUByteArray()
    val hexChars = CharArray(this.size * 2)
    for (j in ubytes.indices) {
        val v = (ubytes[j] and 0xFF.toUByte()).toInt()

        hexChars[j * 2] = HEX_ARRAY[v ushr 4]
        hexChars[j * 2 + 1] = HEX_ARRAY[v and 0x0F]
    }
    return hexChars.concatToString()
}
