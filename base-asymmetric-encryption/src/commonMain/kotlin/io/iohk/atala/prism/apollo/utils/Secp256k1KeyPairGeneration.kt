package io.iohk.atala.prism.apollo.utils

interface Secp256k1KeyPairGeneration {
    fun generateSecp256k1KeyPair(): KMMECKeyPair
}
