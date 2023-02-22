package io.iohk.atala.prism.apollo.utils

interface ECKeyPairGeneration {
    fun generateECKeyPair(): KMMECKeyPair
}
