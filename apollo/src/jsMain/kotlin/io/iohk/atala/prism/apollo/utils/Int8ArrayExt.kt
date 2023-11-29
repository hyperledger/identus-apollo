package io.iohk.atala.prism.apollo.utils

import org.khronos.webgl.Int8Array
import org.khronos.webgl.Uint8Array

fun Uint8Array.asByteArray() = Int8Array(buffer, byteOffset, length).unsafeCast<ByteArray>()
