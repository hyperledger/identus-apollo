package io.iohk.atala.prism.apollo.utils

import io.iohk.atala.prism.apollo.utils.external.eddsa
import io.iohk.atala.prism.apollo.utils.external.rand
import node.buffer.Buffer

@ExperimentalJsExport
@JsExport
actual class KMMEdKeyPair actual constructor(
    actual val privateKey: KMMEdPrivateKey,
    actual val publicKey: KMMEdPublicKey
) {
    actual fun sign(message: ByteArray): ByteArray {
        return privateKey.sign(message)
    }

    actual fun verify(message: ByteArray, sig: ByteArray): Boolean {
        return publicKey.verify(message, sig)
    }

    actual companion object : Ed25519KeyPairGeneration {
        override fun generateKeyPair(): KMMEdKeyPair {
            val ed25519 = eddsa("ed25519")
            val rnd = rand(32)
            val secret = Buffer.from(rnd).toByteArray()
            val keypair = ed25519.keyFromSecret(secret)
            val public = keypair.getPublic()

            return KMMEdKeyPair(KMMEdPrivateKey(secret), KMMEdPublicKey(public))
        }
    }
}
