package io.iohk.atala.prism.apollo.utils

expect class KMMECKeyPair(privateKey: KMMECPrivateKey, publicKey: KMMECPublicKey) {
    val privateKey: KMMECPrivateKey
    val publicKey: KMMECPublicKey
    companion object : ECKeyPairGeneration
}
