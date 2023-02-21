package io.iohk.atala.prism.apollo.utils

expect class KMMECSecp256k1KeyPair(privateKey: KMMECSecp256k1PrivateKey, publicKey: KMMECSecp256k1PublicKey) {
    val privateKey: KMMECSecp256k1PrivateKey
    val publicKey: KMMECSecp256k1PublicKey
    companion object : Secp256k1KeyPairGeneration
}
