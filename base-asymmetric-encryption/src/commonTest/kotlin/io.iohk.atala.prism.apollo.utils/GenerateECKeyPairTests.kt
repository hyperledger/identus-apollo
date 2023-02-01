package io.iohk.atala.prism.apollo.utils

import kotlin.test.Test
import kotlin.test.assertTrue

class GenerateECKeyPairTests {
    @Test
    fun testGenerateECKeyPairSecp256k1() {
        val keyPair = KMMECKeyPair.generateECKeyPair(EllipticCurve.SECP256k1)

        if (keyPair.privateKey != null && keyPair.publicKey != null) {
            assertTrue(true)
        } else {
            assertTrue(false)
        }
    }

    @Test
    fun testGenerateECKeyPairSecp256r1() {
        val keyPair = KMMECKeyPair.generateECKeyPair(EllipticCurve.SECP256r1)

        if (keyPair.privateKey != null && keyPair.publicKey != null) {
            assertTrue(true)
        } else {
            assertTrue(false)
        }
    }
}
