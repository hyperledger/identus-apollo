package io.iohk.atala.prism.apollo.utils

import io.iohk.atala.prism.apollo.base64.base64DecodedBytes
import io.iohk.atala.prism.apollo.base64.base64PadDecodedBytes
import io.iohk.atala.prism.apollo.base64.base64UrlDecodedBytes
import io.iohk.atala.prism.apollo.base64.base64UrlEncoded
import io.iohk.atala.prism.apollo.secp256k1.Secp256k1Lib
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

// @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
class Secp256k1LibTests {
    @Test
    fun testCreatePublicKey() {
        val privKeyBase64 = "N/JFgvYaReyRXwassz5FHg33A4I6dczzdXrjdHGksmg="
        val base64ByteArray = privKeyBase64.base64DecodedBytes
        val pubKey = Secp256k1Lib().createPublicKey(base64ByteArray, false)
        assertEquals("BD-l4lrQ6Go-oN5XtdpY6o5dyf2V2v5EbMAvRjVGJpE1gYVURJfxKMpNPnKlLr4MOLNVaYvBNOoy9L50E8jVx8Q", pubKey.base64UrlEncoded)
    }

    @Test
    fun testDerivePrivateKey() {
        val privKeyBase64 = "96ViMAl0/N1Xm5RJesQxC2NvxhNc4ZkwPyVevZ4akDI="
        val derivedPrivKeyBase64 = "xEDIjzhlf/0o+vL42KupeLuZDiWBqpUHhVuwO8a2BBA="

        val derivedKey = Secp256k1Lib().derivePrivateKey(privKeyBase64.base64DecodedBytes, derivedPrivKeyBase64.base64PadDecodedBytes)
        assertEquals("u-Yqv0HafNqAlodCU2_ahWRZ91IvQ438BK6wbJSaUwE", derivedKey!!.base64UrlEncoded)
    }

    @Test
    fun testSignature() {
        val privKeyBase64 = "N_JFgvYaReyRXwassz5FHg33A4I6dczzdXrjdHGksmg"
        val message = "Test"

        val signature = Secp256k1Lib().sign(privKeyBase64.base64UrlDecodedBytes, message.encodeToByteArray())
        assertEquals("MEUCIQCFeGlhJrH-9R70X4JzrurWs52SwuxCnJ8ky6riFwMOrwIgT7zlLo7URMHW5tiMgG73IOw2Dm3XyLl1iqW1-t5NFWQ", signature.base64UrlEncoded)
    }

    @Test
    fun testVerification() {
        val pubKeyBase64 = "BD-l4lrQ6Go-oN5XtdpY6o5dyf2V2v5EbMAvRjVGJpE1gYVURJfxKMpNPnKlLr4MOLNVaYvBNOoy9L50E8jVx8Q"
        val signatureBase64 = "MEUCIQCFeGlhJrH-9R70X4JzrurWs52SwuxCnJ8ky6riFwMOrwIgT7zlLo7URMHW5tiMgG73IOw2Dm3XyLl1iqW1-t5NFWQ"
        val message = "Test"

        assertTrue { Secp256k1Lib().verify(pubKeyBase64.base64UrlDecodedBytes, signatureBase64.base64UrlDecodedBytes, message.encodeToByteArray()) }
    }

    @Test
    fun testCompress() {
        val pubKeyBase64 = "BHza5mV6_Iz6XdyMpxpjUMprZUCN_MpMuQCTFYpxSf8rW7N7DD04troywCgLkg0_ABP-IcxZcE1-qKjwCWYTVO8"

        assertEquals(Secp256k1Lib().compressPublicKey(pubKeyBase64.base64UrlDecodedBytes).base64UrlEncoded, "A3za5mV6_Iz6XdyMpxpjUMprZUCN_MpMuQCTFYpxSf8r")
    }

    @Test
    fun testUncompress() {
        val pubKeyBase64 = "A3za5mV6_Iz6XdyMpxpjUMprZUCN_MpMuQCTFYpxSf8r"

        assertEquals(Secp256k1Lib().uncompressPublicKey(pubKeyBase64.base64UrlDecodedBytes).base64UrlEncoded, "BHza5mV6_Iz6XdyMpxpjUMprZUCN_MpMuQCTFYpxSf8rW7N7DD04troywCgLkg0_ABP-IcxZcE1-qKjwCWYTVO8")
    }
}
