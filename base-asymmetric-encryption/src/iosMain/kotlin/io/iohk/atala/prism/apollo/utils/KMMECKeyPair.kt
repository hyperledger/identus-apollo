package io.iohk.atala.prism.apollo.utils

actual class KMMECKeyPair actual constructor(actual val privateKey: KMMECPrivateKey, actual val publicKey: KMMECPublicKey) {

    @OptIn(ExperimentalUnsignedTypes::class)
    internal constructor(privateNative: ByteArray, publicNative: ByteArray) : this(KMMECPrivateKey(privateNative.toUByteArray()), KMMECPublicKey(publicNative.toUByteArray()))

    actual companion object : ECKeyPairGeneration {
        override fun generateECKeyPair(): KMMECKeyPair {
            throw NotImplementedError("Yet to be decided on Default Curve. Please use `generateSecp256k1KeyPair`")
        }
    }
}
