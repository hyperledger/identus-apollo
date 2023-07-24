package io.iohk.atala.prism.apollo.utils

import org.bouncycastle.crypto.params.Ed25519PublicKeyParameters
import org.bouncycastle.crypto.signers.Ed25519Signer

actual class KMMEdPublicKey(val raw: ByteArray) {
    actual fun verify(message: ByteArray, sig: ByteArray): Boolean {
        return try {
            val publicKeyParams = Ed25519PublicKeyParameters(raw, 0)
            val verifier = Ed25519Signer()

            verifier.init(false, publicKeyParams)
            verifier.update(message, 0, message.size)

            verifier.verifySignature(sig)
        } catch (e: Exception) {
            return false
        }
    }
}
