package org.hyperledger.identus.apollo.utils

/**
 * Decodes a hexadecimal string into a byte array.
 *
 * @return the decoded byte array.
 * @throws IllegalArgumentException if the input string length is not even.
 */
fun String.decodeHex(): ByteArray {
    check(length % 2 == 0) { "Must have an even length" }

    return chunked(2)
        .map { it.toInt(16).toByte() }
        .toByteArray()
}
