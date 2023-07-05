package io.iohk.atala.prism.apollo.derivation

import com.ionspin.kotlin.bignum.integer.BigInteger
import kotlin.random.Random
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull

class HDKeyTest {
    @Test
    fun testConstructorWithSeed_thenNonNullValues() {
        val seed = Random.Default.nextBytes(64)
        val depth = 1
        val childIndex = BigInteger(0)

        val hdKey = HDKey(seed, depth, childIndex)

        assertNotNull(hdKey.privateKey)
        assertNotNull(hdKey.chainCode)
        assertEquals(depth, hdKey.depth)
        assertEquals(childIndex, hdKey.childIndex)
    }

    @Test
    fun testDerive_whenIncorrectPath_thenThrowError() {
        val seed = Random.Default.nextBytes(64)
        val depth = 1
        val childIndex = BigInteger(0)

        val hdKey = HDKey(seed, depth, childIndex)
        val path = "x/0"

        assertFailsWith(Error::class) {
            hdKey.derive(path)
        }
    }

    @Ignore
    @Test
    fun testDerive_thenHDDeriveOk() {
        val seed = Random.Default.nextBytes(64)
        val depth = 1
        val childIndex = BigInteger(0)

        val hdKey = HDKey(seed, depth, childIndex)
        val path = "m/44'/0'/0'/0/0"

        val hdKeyResult = hdKey.derive(path)
        assertNotNull(hdKeyResult.privateKey)
        assertNotNull(hdKeyResult.chainCode)
        assertEquals(depth, hdKeyResult.depth)
        assertEquals(childIndex, hdKeyResult.childIndex)
    }

    @Test
    fun testDeriveChild_whenIncorrectPath_thenThrowError() {
        val seed = Random.Default.nextBytes(64)
        val depth = 1
        val childIndex = BigInteger(HDKey.HARDENED_OFFSET)

        val hdKey = HDKey(seed, depth, childIndex)

        assertFailsWith(Exception::class) {
            hdKey.deriveChild(childIndex)
        }
    }

    @Test
    fun testGetKMMSecp256k1PrivateKey_thenPrivateKeyNonNull() {
        val seed = Random.Default.nextBytes(64)
        val depth = 1
        val childIndex = BigInteger(0)

        val hdKey = HDKey(seed, depth, childIndex)
        val key = hdKey.getKMMSecp256k1PrivateKey()
        assertNotNull(key)
    }
}
