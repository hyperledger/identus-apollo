package io.iohk.atala.prism.apollo.ecdsa

expect object KMMECDSA {
    fun sign(type: ECDSAType, data: ByteArray, privateKey: KMMECPrivateKey): ByteArray
    fun verify(type: ECDSAType, data: ByteArray, publicKey: KMMECPublicKey, signature: ByteArray): Boolean
}
