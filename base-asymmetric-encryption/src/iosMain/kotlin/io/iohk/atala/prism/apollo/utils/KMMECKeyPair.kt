package io.iohk.atala.prism.apollo.utils

import io.iohk.atala.prism.apollo.secp256k1.Secp256k1

actual class KMMECKeyPair actual constructor(actual val privateKey: KMMECPrivateKey, actual val publicKey: KMMECPublicKey) {

    internal constructor(privateNative: ByteArray, publicNative: ByteArray) : this(KMMECPrivateKey(privateNative), KMMECPublicKey(publicNative))

    actual companion object : ECKeyPairGeneration {
        override fun generateECKeyPair(curve: EllipticCurve): KMMECKeyPair {
            when (curve) {
                EllipticCurve.SECP256k1 -> {
                    val secp256k1 = Secp256k1()
                    val keyPair = secp256k1.generateKeyPair()
                    return KMMECKeyPair(keyPair.first, keyPair.second)
                }
                EllipticCurve.SECP256r1 -> {
                    throw NotImplementedError("It has not been develop")
                }
            }
        }
    }
}
