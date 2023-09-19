package io.iohk.atala.prism.apollo.utils

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class KMMEdKeyPairTestsIgnored {
    @Test
    fun testGenerateKeyPair() {
        val keyPair = KMMEdKeyPair.generateKeyPair()

        assertNotNull(keyPair)
        assertNotNull(keyPair.privateKey)
        assertNotNull(keyPair.publicKey)
    }

    @Test
    fun testSignMessage() {
        val keyPair = KMMEdKeyPair.generateKeyPair()
        val message = "testing".encodeToByteArray()
        val sig = keyPair.sign(message)

        assertNotNull(sig)
    }

    @Test
    fun testVerifyMessage() {
        val keyPair = KMMEdKeyPair.generateKeyPair()
        val msgHash = "testing".encodeToByteArray()
        val sig = keyPair.sign(msgHash)
        val verified = keyPair.verify(msgHash, sig)

        assertTrue(verified)
    }

    @Test
    fun testVerifyWithAnotherKeyPairFails() {
        val keyPair = KMMEdKeyPair.generateKeyPair()
        val msgHash = "testing".encodeToByteArray()
        val sig = keyPair.sign(msgHash)

        val wrongKeyPair = KMMEdKeyPair.generateKeyPair()
        val verified = wrongKeyPair.verify(msgHash, sig)

        assertFalse(verified)
    }
}
