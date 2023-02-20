package io.iohk.atala.prism.apollo.utils

import js.buffer.ArrayBuffer
import js.typedarrays.Int8Array

fun ByteArray.toArrayBuffer(): ArrayBuffer = this.unsafeCast<Int8Array>().buffer
