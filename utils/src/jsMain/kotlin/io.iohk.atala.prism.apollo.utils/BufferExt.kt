package io.iohk.atala.prism.apollo.utils

import js.core.get
import node.buffer.Buffer

fun Buffer.toByteArray(): ByteArray {
    val byteArray = ByteArray(length)
    for (i in byteArray.indices) {
        byteArray[i] = this[i]
    }

    return byteArray
}
