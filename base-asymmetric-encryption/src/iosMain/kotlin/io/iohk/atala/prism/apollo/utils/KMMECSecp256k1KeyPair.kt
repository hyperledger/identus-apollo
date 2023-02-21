package io.iohk.atala.prism.apollo.utils

import io.iohk.atala.prism.apollo.secp256k1.Secp256k1

actual class KMMECSecp256k1KeyPair actual constructor(actual val privateKey: KMMECSecp256k1PrivateKey, actual val publicKey: KMMECSecp256k1PublicKey) {

    @OptIn(ExperimentalUnsignedTypes::class)
    internal constructor(privateNative: ByteArray, publicNative: ByteArray) : this(KMMECSecp256k1PrivateKey(privateNative.toUByteArray()), KMMECSecp256k1PublicKey(publicNative.toUByteArray()))

    actual companion object : Secp256k1KeyPairGeneration {
        override fun generateSecp256k1KeyPair(): KMMECSecp256k1KeyPair {
            val secp256k1 = Secp256k1()
            val keyPair = secp256k1.generateKeyPair()
            return KMMECSecp256k1KeyPair(keyPair.first, keyPair.second)
        }
    }
}
