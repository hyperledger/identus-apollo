package io.iohk.atala.prism.apollo.util

import org.khronos.webgl.Int8Array
import org.khronos.webgl.Uint8Array

internal fun ByteArray.asUint8Array() = unsafeCast<Int8Array>().run { Uint8Array(buffer, byteOffset, length) }

internal fun Uint8Array.asByteArray() = Int8Array(buffer, byteOffset, length).unsafeCast<ByteArray>()
