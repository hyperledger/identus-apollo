package io.iohk.atala.prism.apollo.utils

import io.iohk.atala.prism.apollo.utils.external.BN
import io.iohk.atala.prism.apollo.utils.external.base.BasePoint
import io.iohk.atala.prism.apollo.utils.external.ec

actual class KMMECKeyPair actual constructor(actual val privateKey: KMMECPrivateKey, actual val publicKey: KMMECPublicKey) {

    internal constructor(privateNative: BN, publicNative: BasePoint) : this(KMMECPrivateKey(privateNative), KMMECPublicKey(publicNative))

    actual companion object : ECKeyPairGeneration, Secp256k1KeyPairGeneration {
        override fun generateECKeyPair(): KMMECKeyPair {
            throw NotImplementedError("Yet to be decided on Default Curve. Please use `generateSecp256k1KeyPair`")
        }

        override fun generateSecp256k1KeyPair(): KMMECKeyPair {
            val ecjs = ec("secp256k1")
            val keyPair = ecjs.genKeyPair()
            val bigNumber = keyPair.getPrivate()
            val basePoint = keyPair.getPublic()
            return KMMECKeyPair(bigNumber, basePoint)
        }
    }
}
