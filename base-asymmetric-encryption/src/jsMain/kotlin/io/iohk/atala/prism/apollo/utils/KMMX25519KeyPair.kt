package io.iohk.atala.prism.apollo.utils

actual class KMMX25519KeyPair actual constructor(
    actual val privateKey: KMMX25519PrivateKey,
    actual val publicKey: KMMX25519PublicKey
) {
    init {
        throw NotImplementedError("X25519 is yet to be implemented in JS")
    }

    actual companion object : X25519KeyPairGeneration {
        override fun generateX25519KeyPair(): KMMX25519KeyPair {
            throw NotImplementedError("X25519 is yet to be implemented in JS")
        }
    }
}
