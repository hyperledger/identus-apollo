package org.hyperledger.identus.apollo.utils

import kotlin.test.Test
import kotlin.test.assertNotNull

class KMMX25519KeyPairTests {
    @Test
    fun testGenerateKeyPair() {
        val keyPair = KMMX25519KeyPair.generateKeyPair()

        assertNotNull(keyPair)
        assertNotNull(keyPair.privateKey)
        assertNotNull(keyPair.publicKey)
    }
}
