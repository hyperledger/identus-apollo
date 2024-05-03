package io.iohk.atala.prism.apollo.utils

import org.kotlincrypto.hash.sha2.SHA512
import kotlin.experimental.and
import kotlin.experimental.or

fun convertSecretKeyToX25519(secretKey: ByteArray): ByteArray {
    // Hash the first 32 bytes of the Ed25519 secret key
    val hashed = SHA512().digest(secretKey.sliceArray(0 until 32))
    // Clamping the hashed value to conform with X25519 format
    hashed[0] = hashed[0] and 248.toByte()
    hashed[31] = hashed[31] and 127.toByte()
    hashed[31] = hashed[31] or 64.toByte()
    // Return the first 32 bytes of the hash as the X25519 secret key
    return hashed.sliceArray(0 until 32)
}
