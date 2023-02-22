package io.iohk.atala.prism.apollo.utils

import io.iohk.atala.prism.apollo.utils.external.BN
import io.iohk.atala.prism.apollo.utils.external.base.BasePoint

actual class KMMECKeyPair actual constructor(actual val privateKey: KMMECPrivateKey, actual val publicKey: KMMECPublicKey) {

    internal constructor(privateNative: BN, publicNative: BasePoint) : this(KMMECPrivateKey(privateNative), KMMECPublicKey(publicNative))

    actual companion object : ECKeyPairGeneration {
        override fun generateECKeyPair(): KMMECKeyPair {
            throw NotImplementedError("Yet to be decided on Default Curve. Please use `generateSecp256k1KeyPair`")
        }
    }
}
