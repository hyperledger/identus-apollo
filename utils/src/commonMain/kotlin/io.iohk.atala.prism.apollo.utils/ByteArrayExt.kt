package io.iohk.atala.prism.apollo.utils

fun ByteArray.padStart(length: Int, padValue: Byte): ByteArray =
    if (size >= length) {
        this
    } else {
        val result = ByteArray(length) { padValue }
        copyInto(result, length - size)
        result
    }
