package org.hyperledger.identus.apollo.utils

import org.khronos.webgl.Int8Array
import org.khronos.webgl.Uint8Array

/**
 * Converts a Uint8Array to a ByteArray.
 *
 * @receiver The Uint8Array to convert.
 * @return The converted ByteArray.
 */
fun Uint8Array.asByteArray() = Int8Array(buffer, byteOffset, length).unsafeCast<ByteArray>()
