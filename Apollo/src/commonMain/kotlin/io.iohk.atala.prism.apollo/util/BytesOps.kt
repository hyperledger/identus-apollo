package io.iohk.atala.prism.apollo.util

internal fun ByteArray.toUByteArray(): UByteArray {
    return this.map { it.toUByte() }.toUByteArray()
}

internal fun ByteArray.padStart(length: Int, padValue: Byte): ByteArray =
    if (size >= length) {
        this
    } else {
        val result = ByteArray(length) { padValue }
        copyInto(result, length - size)
        result
    }

public object BytesOps {
    private val HEX_ARRAY = "0123456789abcdef".toCharArray()

    public fun bytesToHex(bytes: ByteArray): String {
        val ubytes = bytes.toUByteArray()
        val hexChars = CharArray(bytes.size * 2)
        for (j in ubytes.indices) {
            val v = (ubytes[j] and 0xFF.toUByte()).toInt()

            hexChars[j * 2] = HEX_ARRAY[v ushr 4]
            hexChars[j * 2 + 1] = HEX_ARRAY[v and 0x0F]
        }
        return hexChars.concatToString()
    }

    public fun hexToBytes(string: String): ByteArray {
        val result = UByteArray(string.length / 2) { UByte.MIN_VALUE }

        for (i in string.indices step 2) {
            val firstIndex = HEX_ARRAY.indexOf(string[i])
            val secondIndex = HEX_ARRAY.indexOf(string[i + 1])

            val octet = firstIndex.shl(4).or(secondIndex)
            result[i.shr(1)] = octet.toUByte()
        }

        return result.toByteArray()
    }
}
