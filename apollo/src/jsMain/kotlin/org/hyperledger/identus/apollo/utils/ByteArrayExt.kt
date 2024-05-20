package org.hyperledger.identus.apollo.utils

import js.buffer.ArrayBuffer
import org.khronos.webgl.Int8Array
import org.khronos.webgl.Uint8Array

/**
 * Converts the ArrayBuffer to a ByteArray.
 *
 * @return The converted ByteArray.
 */
fun ArrayBuffer.toByteArray(): ByteArray = js.typedarrays.Int8Array(this).unsafeCast<ByteArray>()

/**
 * Converts a ByteArray to a Uint8Array.
 *
 * @receiver The ByteArray to convert.
 * @return The converted Uint8Array.
 */
fun ByteArray.asUint8Array() = unsafeCast<Int8Array>().run { Uint8Array(buffer, byteOffset, length) }
