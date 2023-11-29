package io.iohk.atala.prism.apollo.utils

import kotlin.js.ExperimentalJsExport

actual class KMMX25519KeyPair actual constructor(
    actual val privateKey: KMMX25519PrivateKey,
    actual val publicKey: KMMX25519PublicKey
) {
    @OptIn(ExperimentalJsExport::class)
    public actual companion object : X25519KeyPairGeneration {
        public override fun generateKeyPair(): KMMX25519KeyPair {
            val privateKey = KMMX25519PrivateKey()
            return KMMX25519KeyPair(privateKey, privateKey.publicKey())
        }
    }
}
