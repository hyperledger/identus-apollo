package io.iohk.atala.prism.apollo.utils

actual class KMMEdKeyPair actual constructor(
    actual val privateKey: KMMEdPrivateKey,
    actual val publicKey: KMMEdPublicKey
) {
    init {
        // TODO: we will use this lib for JS https://github.com/indutny/elliptic
        throw NotImplementedError("Ed25519 is yet to be implemented in JS")
    }

    actual companion object : Ed25519KeyPairGeneration {
        override fun generateEd25519KeyPair(): KMMEdKeyPair {
            // TODO: we will use this lib for JS https://github.com/indutny/elliptic
            throw NotImplementedError("Ed25519 is yet to be implemented in JS")
        }
    }
}
