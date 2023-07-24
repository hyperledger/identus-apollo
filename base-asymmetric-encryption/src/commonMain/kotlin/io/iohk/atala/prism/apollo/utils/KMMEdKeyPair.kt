package io.iohk.atala.prism.apollo.utils

expect class KMMEdKeyPair(privateKey: KMMEdPrivateKey, publicKey: KMMEdPublicKey) {
    val privateKey: KMMEdPrivateKey
    val publicKey: KMMEdPublicKey

    companion object : Ed25519KeyPairGeneration

    fun sign(message: ByteArray): ByteArray

    fun verify(message: ByteArray, sig: ByteArray): Boolean
}
