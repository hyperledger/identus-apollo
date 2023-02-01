package io.iohk.atala.prism.apollo.utils

import io.iohk.atala.prism.apollo.utils.external.BN
import io.iohk.atala.prism.apollo.utils.external.base.BasePoint
import io.iohk.atala.prism.apollo.utils.external.ec

actual class KMMECKeyPair actual constructor(actual val privateKey: KMMECPrivateKey, actual val publicKey: KMMECPublicKey) {

    internal constructor(privateNative: BN, publicNative: BasePoint) : this(KMMECPrivateKey(privateNative), KMMECPublicKey(publicNative))

    actual companion object : ECKeyPairGeneration {
        override fun generateECKeyPair(curve: EllipticCurve): KMMECKeyPair {
            when (curve) {
                EllipticCurve.SECP256k1 -> {
                    val ecjs = ec("secp256k1")
                    val keyPair = ecjs.genKeyPair()
                    val bigNumber = keyPair.getPrivate()
                    val basePoint = keyPair.getPublic()
                    return KMMECKeyPair(bigNumber, basePoint)
                }
                EllipticCurve.SECP256r1 -> {
                    throw NotImplementedError("It has not been develop")
                }
            }
        }
    }
}
