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

/**
 * Convert [ByteArray] to Hex String
 * @return [ByteArray] represented in hex format
 */
fun ByteArray.toHexString(): String {
    return joinToString("") {
        (0xFF and it.toInt()).toString(16).padStart(2, '0')
    }
}
