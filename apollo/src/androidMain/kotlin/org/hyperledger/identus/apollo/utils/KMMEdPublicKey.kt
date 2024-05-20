package org.hyperledger.identus.apollo.utils

import org.bouncycastle.crypto.params.Ed25519PublicKeyParameters
import org.bouncycastle.crypto.signers.Ed25519Signer
import java.io.ByteArrayInputStream

/**
 * Represents a public key for the KMMEd cryptographic system.
 *
 * @property raw The raw byte array representation of the public key.
 */
actual class KMMEdPublicKey(val raw: ByteArray) {
    /**
     * Verifies the signature of a message using the provided public key.
     *
     * @param message The message to verify.
     * @param sig The signature to verify.
     * @return true if the signature is valid, false otherwise.
     */
    actual fun verify(message: ByteArray, sig: ByteArray): Boolean {
        return try {
            val publicKeyParams = Ed25519PublicKeyParameters(ByteArrayInputStream(raw))
            val verifier = Ed25519Signer()

            verifier.init(false, publicKeyParams)
            verifier.update(message, 0, message.size)

            verifier.verifySignature(sig)
        } catch (e: Exception) {
            return false
        }
    }
}
