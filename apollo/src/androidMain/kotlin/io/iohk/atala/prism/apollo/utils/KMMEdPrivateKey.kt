package io.iohk.atala.prism.apollo.utils

import org.bouncycastle.crypto.params.Ed25519PrivateKeyParameters
import org.bouncycastle.crypto.signers.Ed25519Signer

/**
 * Represents a private key for the KMMEd cryptographic system.
 *
 * @property raw The raw byte array representation of the private key.
 */
actual class KMMEdPrivateKey(val raw: ByteArray) {
    /**
     * Retrieves the public key associated with a KMMEdPrivateKey.
     *
     * @return The KMMEdPublicKey object representing the public key.
     */
    fun publicKey(): KMMEdPublicKey {
        val private = Ed25519PrivateKeyParameters(raw, 0)
        val public = private.generatePublicKey()
        return KMMEdPublicKey(public.encoded)
    }

    /**
     * Signs a message with the given private key using the Ed25519 cryptographic system.
     *
     * @param message The message to be signed.
     * @return The signature of the message.
     */
    actual fun sign(message: ByteArray): ByteArray {
        val privateKeyParameters = Ed25519PrivateKeyParameters(raw, 0)
        val signer = Ed25519Signer()
        signer.init(true, privateKeyParameters)
        signer.update(message, 0, message.size)
        return signer.generateSignature()
    }

    /**
     * Method convert an ed25519 private key to a x25519 private key
     *
     * @return KMMX25519PrivateKey private key
     */
    actual fun x25519PrivateKey(): KMMX25519PrivateKey {
        val rawX25519Prv = convertSecretKeyToX25519(this.raw)
        return KMMX25519PrivateKey(rawX25519Prv)
    }
}
