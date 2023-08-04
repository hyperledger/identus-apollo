package io.iohk.atala.prism.apollo.derivation

import com.ionspin.kotlin.bignum.integer.BigInteger
import io.iohk.atala.prism.apollo.base64.base64UrlDecodedBytes
import io.iohk.atala.prism.apollo.derivation.HDKey.Companion.HARDENED_OFFSET
import kotlin.random.Random
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class HDKeyTest {

    lateinit var seed: ByteArray
    lateinit var privateKey: String
    lateinit var derivedPrivateKey: String
    var childIndex = BigInteger(0)

    @BeforeTest
    fun setup() {
        seed =
            "e8uNN7LRH5mEUcxa7FhxDAgWGLh8P94WEOD0jUdaJ2mSU1o02u-Lzao50elV32XvYT0ux9jWuBVECpFAz2ckKw".base64UrlDecodedBytes
        privateKey = "96ViMAl0_N1Xm5RJesQxC2NvxhNc4ZkwPyVevZ4akDI"
        derivedPrivateKey = "xURclKhT6as1Tb9vg4AJRRLPAMWb9dYTTthDvXEKjMc"
    }

    @Test
    fun testConstructor_whenSeedIncorrectLength_thenThrowException() {
        val depth = 1
        childIndex = BigInteger(HARDENED_OFFSET)
        seed = seed.sliceArray(IntRange(0, 60))

        assertFailsWith(IllegalArgumentException::class) {
            HDKey(seed, depth, BigIntegerWrapper(childIndex))
        }
    }

    @Test
    fun testConstructorWithSeed_thenRightPrivateKey() {
        val depth = 0

        val hdKey = HDKey(seed = seed, depth = depth, childIndex = BigIntegerWrapper(childIndex))

        assertNotNull(hdKey.privateKey)
        assertTrue(privateKey.base64UrlDecodedBytes.contentEquals(hdKey.privateKey!!))
        assertNotNull(hdKey.chainCode)
        assertEquals(depth, hdKey.depth)
        assertEquals(BigIntegerWrapper(childIndex), hdKey.childIndex)
    }

    @Test
    fun testDerive_whenIncorrectPath_thenThrowException() {
        val depth = 1
        val hdKey = HDKey(seed, depth, BigIntegerWrapper(childIndex))
        val path = "x/0"

        assertFailsWith(Error::class) {
            hdKey.derive(path)
        }
    }

    @Test
    fun testDerive_whenCorrectPath_thenDeriveOk() {
        val depth = 1

        val hdKey = HDKey(seed, depth, BigIntegerWrapper(childIndex))
        val path = "m/0'/0'/0'"

        val derPrivateKey = hdKey.derive(path)
        assertTrue(derivedPrivateKey.base64UrlDecodedBytes.contentEquals(derPrivateKey.privateKey!!))
    }

    @Test
    fun testDeriveChild_whenNoChainCode_thenThrowException() {
        val depth = 1
        val hdKey = HDKey(
            privateKey = privateKey.encodeToByteArray(),
            depth = depth,
            childIndex = BigIntegerWrapper(childIndex)
        )

        assertFailsWith(Exception::class) {
            hdKey.deriveChild(BigIntegerWrapper(childIndex))
        }
    }

    @Test
    fun testDeriveChild_whenPrivateKeyNotHardened_thenThrowException() {
        val depth = 1
        val hdKey = HDKey(
            privateKey = privateKey.encodeToByteArray(),
            depth = depth,
            childIndex = BigIntegerWrapper(childIndex)
        )

        assertFailsWith(Exception::class) {
            hdKey.deriveChild(BigIntegerWrapper(childIndex))
        }
    }

    @Test
    fun testDeriveChild_whenPrivateKeyNotRightLength_thenThrowException() {
        val depth = 1
        childIndex = BigInteger(1)

        val hdKey = HDKey(
            privateKey = Random.Default.nextBytes(33),
            depth = depth,
            childIndex = BigIntegerWrapper(childIndex)
        )

        assertFailsWith(Exception::class) {
            hdKey.deriveChild(BigIntegerWrapper(childIndex))
        }
    }

    @Test
    fun testGetKMMSecp256k1PrivateKey_thenPrivateKeyNonNull() {
        val depth = 1

        val hdKey = HDKey(seed, depth, BigIntegerWrapper(childIndex))
        val key = hdKey.getKMMSecp256k1PrivateKey()
        assertNotNull(key)
    }
}
