package io.iohk.atala.prism.apollo.utils

import org.bouncycastle.crypto.params.Ed25519PrivateKeyParameters
import org.bouncycastle.crypto.signers.Ed25519Signer
import java.io.ByteArrayInputStream

actual class KMMEdPrivateKey(val raw: ByteArray) {

    fun publicKey(): KMMEdPublicKey {
        val private = Ed25519PrivateKeyParameters(raw, 0)
        val public = private.generatePublicKey()
        return KMMEdPublicKey(public.encoded)
    }

    actual fun sign(message: ByteArray): ByteArray {
        val privateKeyParameters = Ed25519PrivateKeyParameters(ByteArrayInputStream(raw))
        val signer = Ed25519Signer()
        signer.init(true, privateKeyParameters)
        signer.update(message, 0, message.size)
        return signer.generateSignature()
    }
}
