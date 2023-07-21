package io.iohk.atala.prism.apollo.utils

import io.iohk.atala.prism.apollo.base64.base64UrlEncoded
import io.iohk.atala.prism.apollo.utils.external.generateKeyPair as stableLibGenerateKeyPair

@ExperimentalJsExport
@JsExport
actual class KMMX25519KeyPair actual constructor(
    actual val privateKey: KMMX25519PrivateKey,
    actual val publicKey: KMMX25519PublicKey
) {
    actual companion object : X25519KeyPairGeneration {
        override fun generateKeyPair(): KMMX25519KeyPair {
            val keyPair = stableLibGenerateKeyPair()
            val secretBytes = keyPair.secretKey.buffer.toByteArray().base64UrlEncoded.encodeToByteArray()
            val publicBytes = keyPair.publicKey.buffer.toByteArray().base64UrlEncoded.encodeToByteArray()

            return KMMX25519KeyPair(
                KMMX25519PrivateKey(secretBytes),
                KMMX25519PublicKey(publicBytes)
            )
        }
    }
}
