package io.iohk.atala.prism.apollo.utils

import js.buffer.ArrayBuffer
import org.khronos.webgl.Int8Array
import org.khronos.webgl.Uint8Array

fun ArrayBuffer.toByteArray(): ByteArray = js.typedarrays.Int8Array(this).unsafeCast<ByteArray>()
fun ByteArray.asUint8Array() = unsafeCast<Int8Array>().run { Uint8Array(buffer, byteOffset, length) }
