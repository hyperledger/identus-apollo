package io.iohk.atala.prism.apollo.utils

import kotlinx.cinterop.ExperimentalForeignApi
import swift.cryptoKit.Ed25519

/**
 * Represents a private key for the KMMEd cryptographic system.
 * @property raw The raw byte array representation of the private key.
 */
@OptIn(ExperimentalForeignApi::class)
public actual class KMMEdPrivateKey(val raw: ByteArray) {
    /**
     * Represents a private key for the KMMEd cryptographic system.
     *
     * @property raw The raw byte array representation of the private key.
     */
    @Throws(RuntimeException::class)
    public constructor() : this(Ed25519.createPrivateKey().success()?.toByteArray() ?: throw RuntimeException("Null result"))

    /**
     * Signs a message with a private key using the Ed25519 algorithm.
     *
     * @param message A ByteArray with the message to be signed.
     * @return A ByteArray containing the signed message.
     * @throws RuntimeException if signing fails for any reason.
     */
    @Throws(RuntimeException::class)
    actual fun sign(message: ByteArray): ByteArray {
        val result = Ed25519.signWithPrivateKey(raw.toNSData(), message.toNSData())
        result.failure()?.let { throw RuntimeException(it.localizedDescription()) }
        return result.success()?.toByteArray() ?: throw RuntimeException("Null result")
    }

    /**
     * Retrieves the public key corresponding to this private key.
     *
     * @return The public key as a [KMMEdPublicKey] object.
     * @throws RuntimeException if an error occurs during the public key retrieval.
     */
    @Throws(RuntimeException::class)
    fun publicKey(): KMMEdPublicKey {
        val result = Ed25519.publicKeyWithPrivateKey(raw.toNSData())
        result.failure()?.let { throw RuntimeException(it.localizedDescription()) }
        val publicRaw = result.success()?.toByteArray() ?: throw RuntimeException("Null result")
        return KMMEdPublicKey(publicRaw)
    }
}
