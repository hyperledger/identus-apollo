package io.iohk.atala.prism.apollo

import com.ionspin.kotlin.bignum.integer.BigInteger
import com.ionspin.kotlin.bignum.integer.Sign
import io.iohk.atala.prism.apollo.keys.ECPoint
import io.iohk.atala.prism.apollo.keys.ECPrivateKeyDecodingException
import io.iohk.atala.prism.apollo.keys.ECPrivateKeyInitializationException
import io.iohk.atala.prism.apollo.util.BytesOps.hexToBytes
import kotlin.test.*

class ECTest {
    val testData =
        byteArrayOf(-107, 101, 68, 118, 27, 74, 29, 50, -32, 72, 47, -127, -49, 3, -8, -55, -63, -66, 46, 125)

    @Test
    fun testGeneration() {
        val keyPair = EC.generateKeyPair()
        assertEquals(keyPair.privateKey.getEncoded().size, ECConfig.PRIVATE_KEY_BYTE_SIZE)
        assertEquals(keyPair.privateKey.getHexEncoded().length, ECConfig.PRIVATE_KEY_BYTE_SIZE * 2)
        assertEquals(keyPair.publicKey.getEncoded().size, ECConfig.PUBLIC_KEY_BYTE_SIZE)
        assertEquals(keyPair.publicKey.getHexEncoded().length, ECConfig.PUBLIC_KEY_BYTE_SIZE * 2)
    }

    @Test
    fun testPrivateKeyFromEncoded() {
        val keyPair = EC.generateKeyPair()
        val encodedPrivateKey = keyPair.privateKey.getEncoded()
        val d = BigInteger.fromByteArray(encodedPrivateKey, Sign.POSITIVE)

        assertEquals(keyPair.privateKey, EC.toPrivateKeyFromBytes(encodedPrivateKey))
        assertEquals(keyPair.privateKey, EC.toPrivateKeyFromBigInteger(d))
    }

    @Test
    fun testPublicKeyFromEncoded() {
        val keyPair = EC.generateKeyPair()
        val encodedPublicKey = keyPair.publicKey.getEncoded()
        val curvePoint = keyPair.publicKey.getCurvePoint()

        // Modulus for Secp256k1. See https://en.bitcoin.it/wiki/Secp256k1
        val modulus = BigInteger.fromUByteArray(
            hexToBytes("fffffffffffffffffffffffffffffffffffffffffffffffffffffffefffffc2f").toUByteArray(),
            Sign.POSITIVE
        )
        val x = curvePoint.x.coordinate
        val y = curvePoint.y.coordinate
        assertTrue(
            (y * y).mod(modulus) == (x * x * x + 7).mod(modulus),
            "Public key point should follow the elliptic curve equation"
        )

        assertEquals(keyPair.publicKey, EC.toPublicKeyFromBytes(encodedPublicKey))
        assertEquals(keyPair.publicKey, EC.toPublicKeyFromBigIntegerCoordinates(x, y))
        assertEquals(keyPair.publicKey, EC.toPublicKeyFromByteCoordinates(x.toByteArray(), y.toByteArray()))
    }

    @Test
    fun testGeneratePublicKeyFromPrivateKey() {
        val keyPair = EC.generateKeyPair()

        assertEquals(keyPair.publicKey, EC.toPublicKeyFromPrivateKey(keyPair.privateKey))
    }

    @Test
    fun testGenerateSamePrivateKeyAcrossAllImplementations() {
        val hexEncodedPrivateKey = "933c25b9e0b10b0618517edeb389b1b5ba5e781f377af6f573a1af354d008034"

        val privateKey = EC.toPrivateKeyFromBytes(hexToBytes(hexEncodedPrivateKey))

        assertEquals(hexEncodedPrivateKey, privateKey.getHexEncoded())
    }

    @Test
    fun testGenerateSamePublicKeyAcrossAllImplementations() {
        val hexEncodedPublicKey =
            "0477d650217424671208f06ed816dab6c09e6b08c4da0f2f46ead049dd5fbd1c82cd23343346003d4c7faf24ed6314bf340e7882941fd69929526cc889a0f93a1c"

        val publicKey = EC.toPublicKeyFromBytes(hexToBytes(hexEncodedPublicKey))

        assertEquals(hexEncodedPublicKey, publicKey.getHexEncoded())
    }

    @Test
    fun testSignAndVerifyText() {
        val keyPair = EC.generateKeyPair()
        val text = "The quick brown fox jumps over the lazy dog"

        val signature = EC.signText(text, keyPair.privateKey)

        assertTrue(EC.verifyText(text, keyPair.publicKey, signature))
    }

    @Test
    fun testSignAndVerifyData() {
        val keyPair = EC.generateKeyPair()

        val signature = EC.signBytes(testData, keyPair.privateKey)

        assertTrue(EC.verifyBytes(testData, keyPair.publicKey, signature))
    }

    @Test
    fun testNotVerifyWrongInput() {
        val keyPair = EC.generateKeyPair()
        val wrongKeyPair = EC.generateKeyPair()
        val text = "The quick brown fox jumps over the lazy dog"
        val wrongText = "Wrong text"

        val signature = EC.signText(text, keyPair.privateKey)
        val wrongSignature = EC.signText(wrongText, keyPair.privateKey)

        assertFalse(EC.verifyText(wrongText, keyPair.publicKey, signature))
        assertFalse(EC.verifyText(text, wrongKeyPair.publicKey, signature))
        assertFalse(EC.verifyText(text, keyPair.publicKey, wrongSignature))
    }

    @Test
    fun testVerifySameSignatureInAllImplementations() {
        val hexEncodedPrivateKey = "0123fbf1050c3fc060b709fdcf240e766a41190c40afc5ac7a702961df8313c0"
        val hexEncodedSignature =
            "30450221008a78c557dfc18275b5c800281ef8d26d2b40572b9c1442d708c610f50f797bd302207e44e340f787df7ab1299dabfc988e4c02fcaca0f68dbe813050f4b8641fa739"
        val privateKey = EC.toPrivateKeyFromBytes(hexToBytes(hexEncodedPrivateKey))
        val signature = EC.toSignatureFromBytes(hexToBytes(hexEncodedSignature))

        assertTrue(EC.verifyBytes(testData, EC.toPublicKeyFromPrivateKey(privateKey), signature))
    }

    @Test
    fun testPrimeIsCorrect() {
        val p = BigInteger(2).pow(256) - BigInteger(2).pow(32) - BigInteger(2).pow(9) - BigInteger(2).pow(8) -
            BigInteger(2).pow(7) - BigInteger(2).pow(6) - BigInteger(2).pow(4) - BigInteger.ONE

        assertTrue { ECConfig.p == p }
    }

    @Test
    fun testCorrectSecp256k1Verifying() {
        val keyPair = EC.generateKeyPair()

        assertTrue { EC.isSecp256k1(keyPair.publicKey.getCurvePoint()) }
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
        val publicKey = EC.toPublicKeyFromByteCoordinates(x, y)

        assertTrue { publicKey.getCurvePoint().x.bytes().size == 32 }
        assertTrue { publicKey.getCurvePoint().y.bytes().size == 32 }
        assertTrue { publicKey.getEncoded().size == ECConfig.PUBLIC_KEY_BYTE_SIZE }
        assertTrue { publicKey.getHexEncoded().length == ECConfig.PUBLIC_KEY_BYTE_SIZE * 2 }
    }

    @Test
    fun testCompressAndDecompressPublicKey() {
        for (publicKey in Secp256k1TestVectors.publicKeysFromSecp256k1TestVectors()) {
            val compressedPublicKey = publicKey.getEncodedCompressed()

            val uncompressedKey = EC.toPublicKeyFromCompressed(compressedPublicKey)

            assertEquals(compressedPublicKey.size, 33)
            assertEquals(uncompressedKey, publicKey)
        }
    }

    // prevent:
    // - 𝑑𝐴 = 0
    // - 𝑑𝐴 = 1
    // - 𝑑𝐴 mod n = 0
    // - 𝑑𝐴 >= n
    // where 𝑑𝐴 is a private key
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
                EC.toPrivateKeyFromBigInteger(d.first)
            }

            if (d.second.length == 64) {
                assertFailsWith<ECPrivateKeyInitializationException> {
                    EC.toPrivateKeyFromBytes(hexToBytes(d.second))
                }
            } else {
                assertFailsWith<ECPrivateKeyDecodingException> {
                    EC.toPrivateKeyFromBytes(hexToBytes(d.second))
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
            EC.toPublicKeyFromPrivateKey(EC.toPrivateKeyFromBigInteger(BigInteger.ZERO))
        }

        assertFailsWith<ECPrivateKeyInitializationException> {
            EC.toPublicKeyFromPrivateKey(EC.toPrivateKeyFromBytes(hexToBytes("0000000000000000000000000000000000000000000000000000000000000000")))
        }
    }

    @Test
    fun testCreationOfPublicKeyFromValueOutOfSecp256K1Fails() {
        data class TestVectorPk(
            val hexEncoded: String,
            val hexEncodedCompressed: String,
            val point: ECPoint
        )

        val pointsOutOfCurve = listOf(
            // Just random point ouf of the Secp256K1 curve
            TestVectorPk(
                hexEncoded = "0400000000000000000000000000000000000000000000000000000000000fcab7000000000000000000000000000000000000000000000000000000000000b17a",
                hexEncodedCompressed = "0200000000000000000000000000000000000000000000000000000000000fcab7",
                point = ECPoint(BigInteger.fromLong(1034935), BigInteger.fromLong(45434))
            ),

            // (0, 0) point
            TestVectorPk(
                hexEncoded = "0400000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000",
                hexEncodedCompressed = "020000000000000000000000000000000000000000000000000000000000000000",
                point = ECPoint(BigInteger.fromLong(0), BigInteger.fromLong(0))
            )
        )

        for (tv in pointsOutOfCurve) {
            assertFails("Expected toPublicKeyFromBytes to throw exception on point ${tv.point} which is out of Secp256k1") {
                EC.toPublicKeyFromBytes(hexToBytes(tv.hexEncoded))
            }

            assertFails("Expected toPublicKeyFromCompressed to throw exception on point ${tv.point} which is out of Secp256k1") {
                EC.toPublicKeyFromCompressed(hexToBytes(tv.hexEncodedCompressed))
            }

            assertFails("Expected toPublicKeyFromByteCoordinates to throw exception on point ${tv.point} which is out of Secp256k1") {
                EC.toPublicKeyFromByteCoordinates(tv.point.x.bytes(), tv.point.y.bytes())
            }

            assertFails("Expected toPublicKeyFromBigIntegerCoordinates to throw exception on point ${tv.point} which is out of Secp256k1") {
                EC.toPublicKeyFromBigIntegerCoordinates(tv.point.x.coordinate, tv.point.y.coordinate)
            }
        }
    }

    @Test
    fun testZeroSignatureVerificationFails() {
        /* "3006020100020100" is a signature corresponding to (r, s) = (0, 0)

         "3006020100020100" hex received as
         bytesToHex(sun.security.util.ECUtil.encodeSignature(hexToBytes("00"))).
         ECUtil.encodeSignature encodes raw bytes representing
         concatenation of r bytes with s bytes to DerValue,
         what a ECDSA signature is*/
        assertFailsWith<IllegalArgumentException> {
            EC.toSignatureFromBytes(hexToBytes("3006020100020100"))
        }
    }
}
