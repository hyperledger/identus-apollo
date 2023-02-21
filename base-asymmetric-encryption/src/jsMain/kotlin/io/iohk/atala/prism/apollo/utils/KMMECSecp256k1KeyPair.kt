package io.iohk.atala.prism.apollo.utils

import io.iohk.atala.prism.apollo.utils.external.BN
import io.iohk.atala.prism.apollo.utils.external.base.BasePoint
import io.iohk.atala.prism.apollo.utils.external.ec

actual class KMMECSecp256k1KeyPair actual constructor(actual val privateKey: KMMECSecp256k1PrivateKey, actual val publicKey: KMMECSecp256k1PublicKey) {

    internal constructor(privateNative: BN, publicNative: BasePoint) : this(KMMECSecp256k1PrivateKey(privateNative), KMMECSecp256k1PublicKey(publicNative))

    actual companion object : Secp256k1KeyPairGeneration {
        override fun generateSecp256k1KeyPair(): KMMECSecp256k1KeyPair {
            val ecjs = ec("secp256k1")
            val keyPair = ecjs.genKeyPair()
            val bigNumber = keyPair.getPrivate()
            val basePoint = keyPair.getPublic()
            return KMMECSecp256k1KeyPair(bigNumber, basePoint)
        }
    }
}
