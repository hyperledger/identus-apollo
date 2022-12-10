package io.iohk.atala.prism.apollo.util

import Buffer
import org.khronos.webgl.get

internal fun Buffer.toByteArray(): ByteArray {
    val byteArray = ByteArray(length)
    for (i in byteArray.indices) {
        byteArray[i] = this[i]
    }

    return byteArray
}
