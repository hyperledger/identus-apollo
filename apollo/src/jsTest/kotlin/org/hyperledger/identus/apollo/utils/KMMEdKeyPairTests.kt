@file:Suppress("ktlint")

package org.hyperledger.identus.apollo.utils

import node.buffer.Buffer
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class KMMEdKeyPairTests {
    private val raw = arrayOf(234, 155, 38, 115, 124, 211, 171, 185, 149, 186, 77, 255, 240, 94, 209, 65, 63, 214, 168, 213, 146, 68, 68, 196, 167, 211, 183, 80, 14, 166, 239, 217)
    private val rawBytes = Buffer.from(raw).toByteArray()
    private val encoded = arrayOf(54, 112, 115, 109, 99, 51, 122, 84, 113, 55, 109, 86, 117, 107, 51, 95, 56, 70, 55, 82, 81, 84, 95, 87, 113, 78, 87, 83, 82, 69, 84, 69, 112, 57, 79, 51, 85, 65, 54, 109, 55, 57, 107)
    private val encodedBytes = Buffer.from(encoded).toByteArray()
    private val publicRaw = arrayOf(207, 230, 188, 131, 200, 191, 223, 38, 163, 19, 244, 3, 35, 18, 5, 238, 195, 245, 155, 246, 139, 41, 51, 159, 202, 2, 46, 72, 150, 167, 68, 8)
    private val publicRawBytes = Buffer.from(publicRaw).toByteArray()
    private val publicEncoded = arrayOf(122, 45, 97, 56, 103, 56, 105, 95, 51, 121, 97, 106, 69, 95, 81, 68, 73, 120, 73, 70, 55, 115, 80, 49, 109, 95, 97, 76, 75, 84, 79, 102, 121, 103, 73, 117, 83, 74, 97, 110, 82, 65, 103)
    private val publicEncodedBytes = Buffer.from(publicEncoded).toByteArray()

    @Test
    fun testGenerateKeyPair() {
        val keyPair = KMMEdKeyPair.generateKeyPair()

        assertNotNull(keyPair)
        assertNotNull(keyPair.privateKey)
        assertNotNull(keyPair.publicKey)
    }

    @Test
    fun testConstructorRaw() {
        val key = KMMEdPrivateKey(rawBytes)

        assertTrue(key.raw.toByteArray() contentEquals rawBytes)
        assertTrue(key.getEncoded().toByteArray() contentEquals encodedBytes)
    }

    @Test
    fun testConstructorEncoded() {
        val key = KMMEdPrivateKey(encodedBytes)

        assertTrue(key.raw.toByteArray() contentEquals rawBytes)
        assertTrue(key.getEncoded().toByteArray() contentEquals encodedBytes)
    }

    @Test
    fun testGetEncoded() {
        val key = KMMEdPrivateKey(rawBytes)

        assertTrue(key.getEncoded().toByteArray() contentEquals encodedBytes)
    }

    @Test
    fun testPublicKey() {
        val privateKey = KMMEdPrivateKey(rawBytes)
        val publicKey = privateKey.publicKey()

        assertTrue(publicKey.raw.toByteArray() contentEquals publicRawBytes)
        assertTrue(publicKey.getEncoded().toByteArray() contentEquals publicEncodedBytes)
    }

    @Test
    fun testSignMessage() {
        val keyPair = KMMEdKeyPair.generateKeyPair()
        val message = "testing".encodeToByteArray()
        val sig = keyPair.sign(message)

        assertNotNull(sig)
    }

    @Test
    fun testSignMessageKnownValue() {
        val privateKey = KMMEdPrivateKey(rawBytes)
        val message = "testing".encodeToByteArray()
        val sig = privateKey.sign(message)
        val sigStr = Buffer.from(sig).toString()
        val expectedBytes = byteArrayOf(67, 68, 57, 67, 68, 69, 52, 67, 49, 54, 50, 51, 65, 69, 57, 65, 51, 48, 55, 51, 69, 66, 50, 52, 49, 48, 67, 53, 53, 48, 52, 57, 53, 52, 70, 51, 57, 69, 68, 67, 68, 55, 66, 68, 57, 49, 57, 67, 54, 67, 49, 54, 67, 68, 54, 51, 52, 56, 48, 55, 50, 56, 53, 69, 66, 51, 70, 57, 69, 69, 51, 52, 52, 51, 57, 49, 66, 55, 65, 51, 55, 69, 54, 53, 53, 70, 56, 51, 49, 70, 68, 48, 57, 70, 50, 50, 52, 53, 68, 55, 66, 70, 50, 67, 48, 57, 70, 66, 69, 67, 57, 55, 55, 51, 50, 50, 49, 69, 65, 48, 52, 50, 70, 69, 69, 49, 48, 48)
        val expectedStr = Buffer.from(expectedBytes).toString()

        assertNotNull(sig)
        assertTrue(expectedBytes contentEquals sig)
        assertTrue(expectedStr contentEquals sigStr)
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
