package io.iohk.atala.prism.apollo.utils

import io.iohk.atala.prism.apollo.secp256k1.Secp256k1

actual class KMMECKeyPair actual constructor(actual val privateKey: KMMECPrivateKey, actual val publicKey: KMMECPublicKey) {

    @OptIn(ExperimentalUnsignedTypes::class)
    internal constructor(privateNative: ByteArray, publicNative: ByteArray) : this(KMMECPrivateKey(privateNative.toUByteArray()), KMMECPublicKey(publicNative.toUByteArray()))

    actual companion object : ECKeyPairGeneration, Secp256k1KeyPairGeneration {
        override fun generateECKeyPair(): KMMECKeyPair {
            throw NotImplementedError("Yet to be decided on Default Curve. Please use `generateSecp256k1KeyPair`")
        }

        override fun generateSecp256k1KeyPair(): KMMECKeyPair {
            val secp256k1 = Secp256k1()
            val keyPair = secp256k1.generateKeyPair()
            return KMMECKeyPair(keyPair.first, keyPair.second)
        }
    }
}
