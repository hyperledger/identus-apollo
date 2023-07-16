package io.iohk.atala.prism.apollo.utils

actual class KMMX25519KeyPair actual constructor(
    actual val privateKey: KMMX25519PrivateKey,
    actual val publicKey: KMMX25519PublicKey
) {
    actual companion object : X25519KeyPairGeneration {
        override fun generateX25519KeyPair(): KMMX25519KeyPair {
            val privateKey = KMMX25519PrivateKey()
            return KMMX25519KeyPair(privateKey, privateKey.publicKey())
        }
    }
}
