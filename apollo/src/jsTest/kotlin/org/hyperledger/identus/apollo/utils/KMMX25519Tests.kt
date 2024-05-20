@file:Suppress("ktlint")

package org.hyperledger.identus.apollo.utils

import node.buffer.Buffer
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class KMMX25519Tests {
    private val raw = arrayOf(51, 115, 246, 68, 98, 108, 130, 79, 66, 173, 201, 51, 112, 98, 163, 196, 188, 34, 100, 148, 28, 98, 236, 251, 234, 41, 3, 175, 80, 1, 64, 152)
    private val rawBytes = Buffer.from(raw).toByteArray()
    private val encoded = arrayOf(77, 51, 80, 50, 82, 71, 74, 115, 103, 107, 57, 67, 114, 99, 107, 122, 99, 71, 75, 106, 120, 76, 119, 105, 90, 74, 81, 99, 89, 117, 122, 55, 54, 105, 107, 68, 114, 49, 65, 66, 81, 74, 103)
    private val encodedBytes = Buffer.from(encoded).toByteArray()
    private val publicRaw = arrayOf(212, 97, 242, 116, 254, 39, 85, 254, 32, 125, 72, 58, 203, 231, 151, 68, 217, 36, 15, 137, 108, 58, 150, 193, 48, 67, 203, 34, 115, 180, 148, 27)
    private val publicRawBytes = Buffer.from(publicRaw).toByteArray()
    private val publicEncoded = arrayOf(49, 71, 72, 121, 100, 80, 52, 110, 86, 102, 52, 103, 102, 85, 103, 54, 121, 45, 101, 88, 82, 78, 107, 107, 68, 52, 108, 115, 79, 112, 98, 66, 77, 69, 80, 76, 73, 110, 79, 48, 108, 66, 115)
    private val publicEncodedBytes = Buffer.from(publicEncoded).toByteArray()

    @Test
    fun testGenerateKeyPair() {
        val keyPair = KMMX25519KeyPair.generateKeyPair()

        assertNotNull(keyPair)
        assertNotNull(keyPair.privateKey)
        assertNotNull(keyPair.publicKey)
    }

    @Test
    fun testConstructorRaw() {
        val key = KMMX25519PrivateKey(rawBytes)

        assertTrue(key.raw.toByteArray() contentEquals rawBytes)
        assertTrue(key.getEncoded().toByteArray() contentEquals encodedBytes)
    }

    @Test
    fun testConstructorEncoded() {
        val key = KMMX25519PrivateKey(encodedBytes)

        assertTrue(key.raw.toByteArray() contentEquals rawBytes)
        assertTrue(key.getEncoded().toByteArray() contentEquals encodedBytes)
    }

    @Test
    fun testGetEncoded() {
        val key = KMMX25519PrivateKey(rawBytes)

        assertTrue(key.getEncoded().toByteArray() contentEquals encodedBytes)
    }

    @Test
    fun testPublicKey() {
        val privateKey = KMMX25519PrivateKey(rawBytes)
        val publicKey = privateKey.publicKey()

        assertTrue(publicKey.raw.toByteArray() contentEquals publicRawBytes)
        assertTrue(publicKey.getEncoded().toByteArray() contentEquals publicEncodedBytes)
    }
}
