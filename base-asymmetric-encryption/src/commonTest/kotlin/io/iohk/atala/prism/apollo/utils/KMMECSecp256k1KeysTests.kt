package io.iohk.atala.prism.apollo.utils

import io.iohk.atala.prism.apollo.base64.base64UrlDecodedBytes
import io.iohk.atala.prism.apollo.base64.base64UrlEncoded
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class KMMECSecp256k1KeysTests {
    @Test
    fun testSigning() {
        val privateKeyBase64 = "N_JFgvYaReyRXwassz5FHg33A4I6dczzdXrjdHGksmg"
        val base64ByteArray = privateKeyBase64.base64UrlDecodedBytes
        val privateKey = KMMECSecp256k1PrivateKey.secp256k1FromByteArray(base64ByteArray)
        val message = "Test"
        val signature = privateKey.sign(message.encodeToByteArray())
        assertEquals("MEUCIQCFeGlhJrH-9R70X4JzrurWs52SwuxCnJ8ky6riFwMOrwIgT7zlLo7URMHW5tiMgG73IOw2Dm3XyLl1iqW1-t5NFWQ", signature.base64UrlEncoded)
    }

    @Test
    fun testVerifyingFromPrivateKey() {
        val privateKeyBase64 = "N_JFgvYaReyRXwassz5FHg33A4I6dczzdXrjdHGksmg"
        val base64ByteArray = privateKeyBase64.base64UrlDecodedBytes
        val privateKey = KMMECSecp256k1PrivateKey.secp256k1FromByteArray(base64ByteArray)
        val message = "Test"
        val signature = privateKey.sign(message.encodeToByteArray())
        assertEquals("MEUCIQCFeGlhJrH-9R70X4JzrurWs52SwuxCnJ8ky6riFwMOrwIgT7zlLo7URMHW5tiMgG73IOw2Dm3XyLl1iqW1-t5NFWQ", signature.base64UrlEncoded)
        assertTrue(privateKey.verify(signature, message.encodeToByteArray()))
    }

    @Test
    fun testVerifyingFromPublicKey() {
        val privateKeyBase64 = "N_JFgvYaReyRXwassz5FHg33A4I6dczzdXrjdHGksmg"
        val base64ByteArray = privateKeyBase64.base64UrlDecodedBytes
        val privateKey = KMMECSecp256k1PrivateKey.secp256k1FromByteArray(base64ByteArray)
        val message = "Test"
        val signature = privateKey.sign(message.encodeToByteArray())
        assertEquals("MEUCIQCFeGlhJrH-9R70X4JzrurWs52SwuxCnJ8ky6riFwMOrwIgT7zlLo7URMHW5tiMgG73IOw2Dm3XyLl1iqW1-t5NFWQ", signature.base64UrlEncoded)
        val publicKey = privateKey.getPublicKey()
        assertTrue(publicKey.verify(signature, message.encodeToByteArray()))
    }

    @Test
    fun getPublicKeyFromPrivateKey() {
        val privateKeyBase64 = "N_JFgvYaReyRXwassz5FHg33A4I6dczzdXrjdHGksmg"
        val base64ByteArray = privateKeyBase64.base64UrlDecodedBytes
        val privateKey = KMMECSecp256k1PrivateKey.secp256k1FromByteArray(base64ByteArray)
        assertEquals("BD-l4lrQ6Go-oN5XtdpY6o5dyf2V2v5EbMAvRjVGJpE1gYVURJfxKMpNPnKlLr4MOLNVaYvBNOoy9L50E8jVx8Q", privateKey.getPublicKey().raw.base64UrlEncoded)
    }

    @Test
    fun testIsPointOnSecp256k1Curve() {
        val point = KMMECPoint(
            "P6XiWtDoaj6g3le12ljqjl3J_ZXa_kRswC9GNUYmkTU".base64UrlDecodedBytes,
            "gYVURJfxKMpNPnKlLr4MOLNVaYvBNOoy9L50E8jVx8Q".base64UrlDecodedBytes
        )
        try {
            val publicKey = KMMECSecp256k1PublicKey.secp256k1FromByteCoordinates(
                point.x,
                point.y
            )
            assertNotNull(publicKey)
            assertTrue(true)
        } catch (ex: Exception) {
            assertTrue(false)
        }
    }

    @Test
    fun testGetCurvePoint() {
        val publicKey = KMMECSecp256k1PublicKey("BD-l4lrQ6Go-oN5XtdpY6o5dyf2V2v5EbMAvRjVGJpE1gYVURJfxKMpNPnKlLr4MOLNVaYvBNOoy9L50E8jVx8Q".base64UrlDecodedBytes)
        val point = publicKey.getCurvePoint()
        assertEquals("P6XiWtDoaj6g3le12ljqjl3J_ZXa_kRswC9GNUYmkTU", point.x.base64UrlEncoded)
        assertEquals("gYVURJfxKMpNPnKlLr4MOLNVaYvBNOoy9L50E8jVx8Q", point.y.base64UrlEncoded)
    }

    @Test
    fun testECSecp256K1FromByteCoordinates() {
        try {
            val publicKey = KMMECSecp256k1PublicKey.secp256k1FromByteCoordinates(
                "P6XiWtDoaj6g3le12ljqjl3J_ZXa_kRswC9GNUYmkTU".base64UrlDecodedBytes,
                "gYVURJfxKMpNPnKlLr4MOLNVaYvBNOoy9L50E8jVx8Q".base64UrlDecodedBytes
            )
            assertNotNull(publicKey)
            assertTrue(true)
        } catch (ex: Exception) {
            assertTrue(false)
        }
    }
}
