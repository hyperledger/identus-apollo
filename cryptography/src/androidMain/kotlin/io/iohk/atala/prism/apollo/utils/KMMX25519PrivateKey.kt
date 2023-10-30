package io.iohk.atala.prism.apollo.utils

import org.bouncycastle.crypto.params.X25519PrivateKeyParameters

actual class KMMX25519PrivateKey(val raw: ByteArray) {

    fun publicKey(): KMMX25519PublicKey {
        val private = X25519PrivateKeyParameters(raw, 0)
        val public = private.generatePublicKey()
        return KMMX25519PublicKey(public.encoded)
    }
}
