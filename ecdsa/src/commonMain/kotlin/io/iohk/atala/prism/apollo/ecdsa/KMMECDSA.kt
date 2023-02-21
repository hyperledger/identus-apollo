package io.iohk.atala.prism.apollo.ecdsa

import io.iohk.atala.prism.apollo.utils.KMMECPrivateKey
import io.iohk.atala.prism.apollo.utils.KMMECPublicKey

expect object KMMECDSA {
    fun sign(type: ECDSAType, data: ByteArray, privateKey: KMMECPrivateKey): KMMECDSASignature
    fun verify(type: ECDSAType, data: ByteArray, publicKey: KMMECPublicKey, signature: KMMECDSASignature): Boolean
}
