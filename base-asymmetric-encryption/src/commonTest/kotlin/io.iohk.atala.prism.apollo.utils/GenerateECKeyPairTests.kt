package io.iohk.atala.prism.apollo.utils

import com.ionspin.kotlin.bignum.integer.BigInteger
import com.ionspin.kotlin.bignum.integer.Sign
import io.iohk.atala.prism.apollo.ecdsa.ECDSAType
import io.iohk.atala.prism.apollo.ecdsa.KMMECDSA
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class GenerateECKeyPairTests {
    val testData =
        byteArrayOf(-107, 101, 68, 118, 27, 74, 29, 50, -32, 72, 47, -127, -49, 3, -8, -55, -63, -66, 46, 125)

    @Test
    fun testGenerateECKeyPairSecp256k1() {
        val keyPair = KMMECKeyPair.generateSecp256k1KeyPair()

        if (keyPair.privateKey != null && keyPair.publicKey != null) {
            assertTrue(true)
        } else {
            assertTrue(false)
        }
    }

    @Test
    fun testGeneration() {
        val keyPair = KMMECKeyPair.generateSecp256k1KeyPair()
        assertEquals(keyPair.privateKey.getEncoded().size, ECConfig.PRIVATE_KEY_BYTE_SIZE)
        assertEquals(keyPair.privateKey.getEncoded().toHex().length, ECConfig.PRIVATE_KEY_BYTE_SIZE * 2)
        assertEquals(keyPair.publicKey.getEncoded().size, ECConfig.PUBLIC_KEY_BYTE_SIZE)
        assertEquals(keyPair.publicKey.getEncoded().toHex().length, ECConfig.PUBLIC_KEY_BYTE_SIZE * 2)
    }

    @Test
    fun testPrivateKeyFromEncoded() {
        val keyPair = KMMECKeyPair.generateSecp256k1KeyPair()
        val encodedPrivateKey = keyPair.privateKey.getEncoded()
        val d = BigInteger.fromByteArray(encodedPrivateKey, Sign.POSITIVE)

        assertEquals(keyPair.privateKey, KMMECPrivateKey.secp256k1FromBytes(encodedPrivateKey))
        assertEquals(keyPair.privateKey, KMMECPrivateKey.secp256k1FromBigInteger(d))
    }

    @OptIn(ExperimentalUnsignedTypes::class)
    @Test
    fun testPublicKeyFromEncoded() {
        val keyPair = KMMECKeyPair.generateSecp256k1KeyPair()
        val encodedPublicKey = keyPair.publicKey.getEncoded()
        val curvePoint = keyPair.publicKey.getCurvePoint()

        // Modulus for Secp256k1. See https://en.bitcoin.it/wiki/Secp256k1
        val modulus = BigInteger.fromUByteArray(
            "fffffffffffffffffffffffffffffffffffffffffffffffffffffffefffffc2f".decodeHex().toUByteArray(),
            Sign.POSITIVE
        )
        val x = curvePoint.x.coordinate
        val y = curvePoint.y.coordinate
        assertEquals((y * y).mod(modulus), (x * x * x + 7).mod(modulus), "Public key point should follow the elliptic curve equation")

        assertEquals(keyPair.publicKey, KMMECPublicKey.secp256k1FromBytes(encodedPublicKey))
        assertEquals(keyPair.publicKey, KMMECPublicKey.secp256k1FromBigIntegerCoordinates(x, y))
        assertEquals(keyPair.publicKey, KMMECPublicKey.secp256k1FromByteCoordinates(x.toByteArray(), y.toByteArray()))
    }

    @Test
    fun testGenerateSamePrivateKeyAcrossAllImplementations() {
        val hexEncodedPrivateKey = "933c25b9e0b10b0618517edeb389b1b5ba5e781f377af6f573a1af354d008034"

        val privateKey = KMMECPrivateKey.secp256k1FromBytes(hexEncodedPrivateKey.decodeHex())

        assertEquals(hexEncodedPrivateKey, privateKey.getEncoded().toHex())
    }

    @Test
    fun testGenerateSamePublicKeyAcrossAllImplementations() {
        val hexEncodedPublicKey =
            "0477d650217424671208f06ed816dab6c09e6b08c4da0f2f46ead049dd5fbd1c82cd23343346003d4c7faf24ed6314bf340e7882941fd69929526cc889a0f93a1c"

        val publicKey = KMMECPublicKey.secp256k1FromBytes(hexEncodedPublicKey.decodeHex())

        assertEquals(hexEncodedPublicKey, publicKey.getEncoded().toHex())
    }

    @Test
    fun testSignAndVerifyText() {
        val keyPair = KMMECKeyPair.generateSecp256k1KeyPair()
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
        val keyPair = KMMECKeyPair.generateSecp256k1KeyPair()

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
        val keyPair = KMMECKeyPair.generateSecp256k1KeyPair()
        val wrongKeyPair = KMMECKeyPair.generateSecp256k1KeyPair()

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

        assertTrue(
            KMMECDSA.verify(
                type = type,
                data = wrongText.encodeToByteArray(),
                publicKey = keyPair.publicKey,
                signature = signature
            )
        )
        assertTrue(
            KMMECDSA.verify(
                type = type,
                data = text.encodeToByteArray(),
                publicKey = wrongKeyPair.publicKey,
                signature = signature
            )
        )
        assertTrue(
            KMMECDSA.verify(
                type = type,
                data = text.encodeToByteArray(),
                publicKey = keyPair.publicKey,
                signature = wrongSignature
            )
        )
    }

    @Test
    fun testVerifySameSignatureInAllImplementations() {
        val type = ECDSAType.ECDSA_SHA256
        val hexEncodedPrivateKey = "0123fbf1050c3fc060b709fdcf240e766a41190c40afc5ac7a702961df8313c0"
        val hexEncodedSignature =
            "30450221008a78c557dfc18275b5c800281ef8d26d2b40572b9c1442d708c610f50f797bd302207e44e340f787df7ab1299dabfc988e4c02fcaca0f68dbe813050f4b8641fa739"
        val privateKey = KMMECPrivateKey.secp256k1FromBytes(hexEncodedPrivateKey.decodeHex())
        val signature = hexEncodedSignature.decodeHex()

        assertTrue(
            KMMECDSA.verify(
                type = type,
                data = testData,
                publicKey = privateKey.getPublicKey(),
                signature = signature
            )
        )
    }

    @Test
    fun testShortCoordinates() {
        // x starts with 0, so it is possible to lose it during BigInteger transformations
        val x = byteArrayOf(
            0,
            11,
            29,
            14,
            70,
            52,
            -97,
            -68,
            -30,
            57,
            -95,
            86,
            82,
            1,
            97,
            -11,
            -93,
            77,
            106,
            -92,
            54,
            39,
            -112,
            115,
            -54,
            -39,
            36,
            61,
            90,
            -128,
            36,
            44
        )
        val y = byteArrayOf(
            -95,
            -92,
            79,
            20,
            -8,
            74,
            -117,
            98,
            41,
            -123,
            -120,
            64,
            -39,
            -42,
            56,
            8,
            60,
            95,
            27,
            -33,
            -71,
            -57,
            93,
            74,
            -120,
            31,
            56,
            18,
            -50,
            90,
            -58,
            -22
        )
        val publicKey = KMMECPublicKey.secp256k1FromByteCoordinates(x, y)

        assertTrue { publicKey.getCurvePoint().x.bytes().size == 32 }
        assertTrue { publicKey.getCurvePoint().y.bytes().size == 32 }
        assertTrue { publicKey.getEncoded().size == ECConfig.PUBLIC_KEY_BYTE_SIZE }
        assertTrue { publicKey.getEncoded().toHex().length == ECConfig.PUBLIC_KEY_BYTE_SIZE * 2 }
    }

    @Test
    fun testCompressAndDecompressPublicKey() {
        for (publicKey in Secp256k1TestVectors.publicKeysFromSecp256k1TestVectors()) {
            val compressedPublicKey = publicKey.getEncodedCompressed()

            val uncompressedKey = KMMECPublicKey.secp256k1FromCompressed(compressedPublicKey)

            assertEquals(compressedPublicKey.size, 33)
            assertEquals(uncompressedKey, publicKey)
        }
    }

    @Test
    fun testCreationOfPrivateKeyFromCornerCasesFail() {
        val invalidDWithHexEncodings = listOf(
            Pair(BigInteger.ZERO, "0000000000000000000000000000000000000000000000000000000000000000"),
            Pair(BigInteger.ONE, "0000000000000000000000000000000000000000000000000000000000000001"),
            Pair(ECConfig.n, "fffffffffffffffffffffffffffffffebaaedce6af48a03bbfd25e8cd0364141"),
            Pair(
                ECConfig.n.multiply(BigInteger.TWO),
                "01fffffffffffffffffffffffffffffffd755db9cd5e9140777fa4bd19a06c8282"
            ),
            Pair(
                ECConfig.n.multiply(BigInteger.TEN),
                "09fffffffffffffffffffffffffffffff34ad4a102d8d642557e37b180221e8c8a"
            ),
            Pair(
                ECConfig.n.add(BigInteger.ONE),
                "fffffffffffffffffffffffffffffffebaaedce6af48a03bbfd25e8cd0364142",
            )
        )

        for (d in invalidDWithHexEncodings) {
            assertFailsWith<ECPrivateKeyInitializationException> {
                KMMECPrivateKey.secp256k1FromBigInteger(d.first)
            }

            if (d.second.length == 64) {
                assertFailsWith<ECPrivateKeyInitializationException> {
                    KMMECPrivateKey.secp256k1FromBytes(d.second.decodeHex())
                }
            } else {
                assertFailsWith<ECPrivateKeyDecodingException> {
                    KMMECPrivateKey.secp256k1FromBytes(d.second.decodeHex())
                }
            }
        }
    }

    @Test
    fun testCreationOfPublicKeyFromInfinityFails() {
        /*
        It's impossible to create an infinity point using methods:
          * EC.toPublicKeyFromBytes
          * EC.toPublicKeyFromCompressed
          * EC.toPublicKeyFromByteCoordinates
          * EC.toPublicKeyFromBigIntegerCoordinates
        As it's even impossible to represent infinity point in encoded bytes and a pair of BigIntegers.

        The only ability to create it is from a private key with 𝑑𝐴 = 0,
        which should be impossible itself.
        */

        assertFailsWith<ECPrivateKeyInitializationException> {
            val private = KMMECPrivateKey.secp256k1FromBigInteger(BigInteger.ZERO)
            private.getPublicKey()
        }

        assertFailsWith<ECPrivateKeyInitializationException> {
            val private = KMMECPrivateKey.secp256k1FromBytes("0000000000000000000000000000000000000000000000000000000000000000".decodeHex())
            private.getPublicKey()
        }
    }

    @Test
    fun testCreationOfPublicKeyFromValueOutOfSecp256K1Fails() {
        data class TestVectorPk(
            val hexEncoded: String,
            val hexEncodedCompressed: String,
            val point: KMMECPoint
        )

        val pointsOutOfCurve = listOf(
            // Just random point ouf of the Secp256K1 curve
            TestVectorPk(
                hexEncoded = "0400000000000000000000000000000000000000000000000000000000000fcab7000000000000000000000000000000000000000000000000000000000000b17a",
                hexEncodedCompressed = "0200000000000000000000000000000000000000000000000000000000000fcab7",
                point = KMMECPoint(BigInteger.fromLong(1034935), BigInteger.fromLong(45434))
            ),

            // (0, 0) point
            TestVectorPk(
                hexEncoded = "0400000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000",
                hexEncodedCompressed = "020000000000000000000000000000000000000000000000000000000000000000",
                point = KMMECPoint(BigInteger.fromLong(0), BigInteger.fromLong(0))
            )
        )

        for (tv in pointsOutOfCurve) {
            assertFails("Expected toPublicKeyFromBytes to throw exception on point ${tv.point} which is out of Secp256k1") {
                KMMECPublicKey.secp256k1FromBytes(tv.hexEncoded.decodeHex())
            }

            assertFails("Expected toPublicKeyFromCompressed to throw exception on point ${tv.point} which is out of Secp256k1") {
                KMMECPublicKey.secp256k1FromCompressed(tv.hexEncodedCompressed.decodeHex())
            }

            assertFails("Expected toPublicKeyFromByteCoordinates to throw exception on point ${tv.point} which is out of Secp256k1") {
                KMMECPublicKey.secp256k1FromByteCoordinates(tv.point.x.bytes(), tv.point.y.bytes())
            }

            assertFails("Expected toPublicKeyFromBigIntegerCoordinates to throw exception on point ${tv.point} which is out of Secp256k1") {
                KMMECPublicKey.secp256k1FromBigIntegerCoordinates(tv.point.x.coordinate, tv.point.y.coordinate)
            }
        }
    }
}
