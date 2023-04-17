package io.iohk.atala.prism.apollo.ecdsa

import io.iohk.atala.prism.apollo.utils.KMMECSecp256k1KeyPair
import io.iohk.atala.prism.apollo.utils.KMMECSecp256k1PrivateKey
import io.iohk.atala.prism.apollo.utils.decodeHex
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class KMMECDSATests {

    @Test
    fun testECDSA() {
        val textToSign = "Hello IOG!"
        val keyPair = KMMECSecp256k1KeyPair.generateSecp256k1KeyPair()

        val signature = KMMECDSA.sign(
            ECDSAType.ECDSA_SHA256,
            textToSign.encodeToByteArray(),
            keyPair.privateKey
        )

        assertTrue(
            KMMECDSA.verify(
                ECDSAType.ECDSA_SHA256,
                textToSign.encodeToByteArray(),
                keyPair.publicKey,
                signature
            )
        )
    }

    @Test
    fun testSignAndVerifyText() {
        val keyPair = KMMECSecp256k1KeyPair.generateSecp256k1KeyPair()
        val text = "The quick brown fox jumps over the lazy dog"

        val signature = KMMECDSA.sign(
            type = ECDSAType.ECDSA_SHA256,
            privateKey = keyPair.privateKey,
            data = text.encodeToByteArray()
        )

        assertTrue(
            KMMECDSA.verify(
                type = ECDSAType.ECDSA_SHA256,
                data = text.encodeToByteArray(),
                publicKey = keyPair.publicKey,
                signature = signature
            )
        )
    }

    @Test
    fun testSignAndVerifyData() {
        val testData = byteArrayOf(-107, 101, 68, 118, 27, 74, 29, 50, -32, 72, 47, -127, -49, 3, -8, -55, -63, -66, 46, 125)
        val keyPair = KMMECSecp256k1KeyPair.generateSecp256k1KeyPair()

        val signature = KMMECDSA.sign(
            type = ECDSAType.ECDSA_SHA256,
            privateKey = keyPair.privateKey,
            data = testData
        )

        assertTrue(
            KMMECDSA.verify(
                type = ECDSAType.ECDSA_SHA256,
                data = testData,
                publicKey = keyPair.publicKey,
                signature = signature
            )
        )
    }

    @Test
    fun testNotVerifyWrongInput() {
        val type = ECDSAType.ECDSA_SHA256
        val keyPair = KMMECSecp256k1KeyPair.generateSecp256k1KeyPair()
        val wrongKeyPair = KMMECSecp256k1KeyPair.generateSecp256k1KeyPair()

        val text = "The quick brown fox jumps over the lazy dog"
        val wrongText = "Wrong text"

        val signature = KMMECDSA.sign(
            type = type,
            privateKey = keyPair.privateKey,
            data = text.encodeToByteArray()
        )
        val wrongSignature = KMMECDSA.sign(
            type = type,
            privateKey = keyPair.privateKey,
            data = wrongText.encodeToByteArray()
        )

        assertFalse(
            KMMECDSA.verify(
                type = type,
                data = wrongText.encodeToByteArray(),
                publicKey = keyPair.publicKey,
                signature = signature
            )
        )
        assertFalse(
            KMMECDSA.verify(
                type = type,
                data = text.encodeToByteArray(),
                publicKey = wrongKeyPair.publicKey,
                signature = signature
            )
        )
        assertFalse(
            KMMECDSA.verify(
                type = type,
                data = text.encodeToByteArray(),
                publicKey = keyPair.publicKey,
                signature = wrongSignature
            )
        )
    }

    @Test
    @Ignore // Not working for JS
    fun testVerifySameSignatureInAllImplementations() {
        val testData = byteArrayOf(-107, 101, 68, 118, 27, 74, 29, 50, -32, 72, 47, -127, -49, 3, -8, -55, -63, -66, 46, 125)
        val hexEncodedPrivateKey = "0123fbf1050c3fc060b709fdcf240e766a41190c40afc5ac7a702961df8313c0"
        val hexEncodedSignature =
            "30450221008a78c557dfc18275b5c800281ef8d26d2b40572b9c1442d708c610f50f797bd302207e44e340f787df7ab1299dabfc988e4c02fcaca0f68dbe813050f4b8641fa739"
        val privateKey = KMMECSecp256k1PrivateKey.secp256k1FromBytes(hexEncodedPrivateKey.decodeHex())
        val signature = hexEncodedSignature.decodeHex()

        assertTrue(
            KMMECDSA.verify(
                type = ECDSAType.ECDSA_SHA256,
                data = testData,
                publicKey = privateKey.getPublicKey(),
                signature = signature
            )
        )
    }
}
