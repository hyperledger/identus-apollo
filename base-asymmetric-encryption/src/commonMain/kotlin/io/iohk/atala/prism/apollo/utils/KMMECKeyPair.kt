package io.iohk.atala.prism.apollo.utils

// TODO(Create KMMSecp256k1KeyPair to contains all below implementation to better separate responsibilities)
expect class KMMECKeyPair(privateKey: KMMECPrivateKey, publicKey: KMMECPublicKey) {
    val privateKey: KMMECPrivateKey
    val publicKey: KMMECPublicKey

    companion object : ECKeyPairGeneration, Secp256k1KeyPairGeneration
}
