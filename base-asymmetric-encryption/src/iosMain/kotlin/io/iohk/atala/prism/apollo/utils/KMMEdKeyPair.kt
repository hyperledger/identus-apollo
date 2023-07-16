package io.iohk.atala.prism.apollo.utils

actual class KMMEdKeyPair actual constructor(
    actual val privateKey: KMMEdPrivateKey,
    actual val publicKey: KMMEdPublicKey
) {

    actual companion object : Ed25519KeyPairGeneration {
        override fun generateEd25519KeyPair(): KMMEdKeyPair {
            val privateKey = KMMEdPrivateKey()
            return KMMEdKeyPair(privateKey, privateKey.publicKey())
        }
    }
}
