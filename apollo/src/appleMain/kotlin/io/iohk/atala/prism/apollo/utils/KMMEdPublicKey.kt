package io.iohk.atala.prism.apollo.utils

import kotlinx.cinterop.ExperimentalForeignApi
import swift.cryptoKit.Ed25519

/**
 * Represents a public key for the KMMEd cryptographic system.
 *
 * @property raw The raw byte array representation of the public key.
 */
@OptIn(ExperimentalForeignApi::class)
public actual class KMMEdPublicKey(val raw: ByteArray = ByteArray(0)) {
    /**
     * Verifies a message signature using a public key.
     *
     * @param message A ByteArray containing the original message.
     * @param sig A ByteArray containing the signature to be verified.
     * @return A boolean indicating whether the signature matches the original message.
     * @throws RuntimeException if the verification fails or the result is null.
     */
    @Throws(RuntimeException::class)
    actual fun verify(message: ByteArray, sig: ByteArray): Boolean {
        val result = Ed25519.verifyWithPublicKey(raw.toNSData(), sig.toNSData(), message.toNSData())
        result.failure()?.let { throw RuntimeException(it.localizedDescription()) }
        return result.success()?.boolValue ?: throw RuntimeException("Null result")
    }
}
