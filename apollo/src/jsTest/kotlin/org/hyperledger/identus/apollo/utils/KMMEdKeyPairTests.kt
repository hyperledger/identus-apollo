@file:Suppress("ktlint")

package org.hyperledger.identus.apollo.utils

import org.hyperledger.identus.apollo.base64.base64DecodedBytes
import org.hyperledger.identus.apollo.base64.base64UrlDecodedBytes
import org.hyperledger.identus.apollo.base64.base64UrlEncoded
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class KMMEdKeyPairTests {
     val rawMessage = "Hello".encodeToByteArray()
     val rawSK = "M7+EvGp9vjLyx5SQ1xiKhRUN+YQ0kjx6d21WgX8P1hE".base64DecodedBytes
     val rawSKEncoded = rawSK.base64UrlEncoded
     val rawPk = "6kXLIS4a3UAzHm/5XTcvjCTGoAQ+yqPTT0YsM76EeuQ".base64DecodedBytes
     val rawPKEncoded = rawPk.base64UrlEncoded
     val rawSig = "rQHC0fmOclPBFiPVJCK_WJB0NgTAxSqpggPwRotNdncKrhgM2eECtr3j7UBTBmDdPTmpXGwQvyhTRTG8cymeBA".base64UrlDecodedBytes

    @Test
    fun testGenerateKeyPair() {
        val keyPair = KMMEdKeyPair.generateKeyPair()
        assertNotNull(keyPair)
        assertNotNull(keyPair.privateKey)
        assertNotNull(keyPair.publicKey)
    }

    @Test
    fun testConstructorRaw() {
        val key = KMMEdPrivateKey(rawSK)
        val rawKeyBytes = key.raw.toByteArray()
        val rawKeyEncodedBytes = key.getEncoded().toByteArray().decodeToString()
        assertTrue(rawKeyBytes contentEquals rawSK)
        assertTrue(rawKeyEncodedBytes contentEquals rawSKEncoded)
    }

    @Test
    fun testConstructorEncoded() {
        val key = KMMEdPrivateKey(rawSKEncoded.base64UrlDecodedBytes)
        assertTrue(key.raw.toByteArray() contentEquals rawSK)
        assertTrue(key.getEncoded().toByteArray().decodeToString() contentEquals rawSKEncoded)
    }

    @Test
    fun testGetEncoded() {
        val key = KMMEdPrivateKey(rawSK)
        assertTrue(key.getEncoded().toByteArray().decodeToString() contentEquals rawSKEncoded)
    }


    @Test
    fun testPublicKey() {
        val privateKey = KMMEdPrivateKey(rawSK)
        val publicKey = privateKey.publicKey()
        assertTrue(publicKey.raw.toByteArray() contentEquals rawPk)
        assertTrue(publicKey.getEncoded().toByteArray().decodeToString() contentEquals rawPKEncoded)
    }

    @Test
    fun testSignMessage() {
        val keyPair = KMMEdKeyPair.generateKeyPair()
        val sig = keyPair.sign(rawMessage)
        assertNotNull(sig)
    }


    @Test
    fun testSignMessageKnownValue() {
        val privateKey = KMMEdPrivateKey(rawSK)
        val sig = privateKey.sign(rawMessage)
        assertNotNull(sig)
        assertTrue(sig contentEquals rawSig)
    }

    @Test
    fun testVerifyMessage() {
        val keyPair = KMMEdKeyPair.generateKeyPair()
        val sig = keyPair.sign(rawMessage)
        val verified = keyPair.verify(rawMessage, sig)
        assertTrue(verified)
    }


    @Test
    fun testVerifyWithAnotherKeyPairFails() {
        val keyPair = KMMEdKeyPair.generateKeyPair()
        val sig = keyPair.sign(rawMessage)
        val wrongKeyPair = KMMEdKeyPair.generateKeyPair()
        val verified = wrongKeyPair.verify(rawMessage, sig)
        assertFalse(verified)
    }
}
