package io.iohk.atala.prism.apollo.utils

import io.iohk.atala.prism.apollo.utils.external.generateKeyPair as stableLibGenerateKeyPair

@OptIn(ExperimentalJsExport::class)
@JsExport
actual class KMMX25519KeyPair actual constructor(
    actual val privateKey: KMMX25519PrivateKey,
    actual val publicKey: KMMX25519PublicKey
) {
    actual companion object : X25519KeyPairGeneration {
        override fun generateKeyPair(): KMMX25519KeyPair {
            val keyPair = stableLibGenerateKeyPair()

            return KMMX25519KeyPair(
                KMMX25519PrivateKey(keyPair.secretKey.buffer.toByteArray()),
                KMMX25519PublicKey(keyPair.publicKey.buffer.toByteArray())
            )
        }
    }
}
