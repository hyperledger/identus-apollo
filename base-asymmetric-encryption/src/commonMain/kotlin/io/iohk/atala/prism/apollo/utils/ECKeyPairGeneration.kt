package io.iohk.atala.prism.apollo.utils

interface ECKeyPairGeneration {
    fun generateECKeyPair(curve: EllipticCurve = EllipticCurve.SECP256k1): KMMECKeyPair
}
