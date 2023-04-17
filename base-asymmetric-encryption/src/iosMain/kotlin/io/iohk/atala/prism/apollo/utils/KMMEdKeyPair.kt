package io.iohk.atala.prism.apollo.utils

actual class KMMEdKeyPair actual constructor(
    actual val privateKey: KMMEdPrivateKey,
    actual val publicKey: KMMEdPublicKey
) {
    init {
        // TODO: To be investigated
        throw NotImplementedError("Ed25519 is yet to be implemented in iOS")
    }

    actual companion object : Ed25519KeyPairGeneration {
        override fun generateEd25519KeyPair(): KMMEdKeyPair {
            // TODO: To be investigated
            throw NotImplementedError("Ed25519 is yet to be implemented in iOS")
        }
    }
}
