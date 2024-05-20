package org.hyperledger.identus.apollo.utils

import js.buffer.ArrayBuffer
import js.typedarrays.Int8Array

/**
 * Converts a ByteArray to an ArrayBuffer.
 *
 * @return The resulting ArrayBuffer.
 */
fun ByteArray.toArrayBuffer(): ArrayBuffer = this.unsafeCast<Int8Array>().buffer
