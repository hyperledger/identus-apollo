package io.iohk.atala.prism.apollo.utils

interface Ed25519KeyPairGeneration {
    fun generateEd25519KeyPair(): KMMEdKeyPair
}
