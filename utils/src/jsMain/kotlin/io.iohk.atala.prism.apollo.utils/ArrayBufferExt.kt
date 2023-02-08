package io.iohk.atala.prism.apollo.utils

import js.buffer.ArrayBuffer
import js.typedarrays.Int8Array

fun ArrayBuffer.toByteArray(): ByteArray = Int8Array(this).unsafeCast<ByteArray>()
fun ByteArray.toArrayBuffer(): ArrayBuffer = this.unsafeCast<Int8Array>().buffer
