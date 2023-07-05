package io.iohk.atala.prism.apollo.derivation

import com.ionspin.kotlin.bignum.integer.BigInteger
import com.ionspin.kotlin.bignum.integer.toBigInteger
import io.iohk.atala.prism.apollo.derivation.HDKey.Companion.HARDENED_OFFSET
import io.iohk.atala.prism.apollo.utils.KMMECSecp256k1KeyPair
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull

class HDKeyTest {

    @Test
    fun testConstructor_whenSeedIncorrectLength_thenThrowException() {
        val seed = Random.Default.nextBytes(32)
        val depth = 1
        val childIndex = BigInteger(HARDENED_OFFSET)

        assertFailsWith(IllegalArgumentException::class) {
            HDKey(seed, depth, childIndex)
        }
    }

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
    fun testDerive_whenIncorrectPath_thenThrowException() {
        val seed = Random.Default.nextBytes(64)
        val depth = 1
        val childIndex = BigInteger(0)

        val hdKey = HDKey(seed, depth, childIndex)
        val path = "x/0"

        assertFailsWith(Error::class) {
            hdKey.derive(path)
        }
    }

    @Test
    fun testDerive_thenHDDeriveOk() {
        val keyPair = KMMECSecp256k1KeyPair.generateSecp256k1KeyPair()
        val chainCode = Random.Default.nextBytes(32)
        val seed = keyPair.privateKey.getEncoded() + chainCode
        val depth = 1
        val childIndex: BigInteger = HARDENED_OFFSET.toBigInteger()

        val hdKey = HDKey(seed, depth, childIndex)
        val path = "m/44'/0'/0'"

        val hdKeyResult = hdKey.derive(path)
        assertNotNull(hdKeyResult.privateKey)
        assertNotNull(hdKeyResult.chainCode)
        assertEquals(4, hdKeyResult.depth)
        assertEquals(childIndex, hdKeyResult.childIndex)
    }

    @Test
    fun testDeriveChild_whenNoChainCode_thenThrowException() {
        val keyPair = KMMECSecp256k1KeyPair.generateSecp256k1KeyPair()
        val depth = 1
        val childIndex = BigInteger(HARDENED_OFFSET)

        val hdKey = HDKey(
            privateKey = keyPair.privateKey.getEncoded(),
            depth = depth,
            childIndex = childIndex
        )

        assertFailsWith(Exception::class) {
            hdKey.deriveChild(childIndex)
        }
    }

    @Test
    fun testDeriveChild_whenPrivateKeyNotHardened_thenThrowException() {
        val keyPair = KMMECSecp256k1KeyPair.generateSecp256k1KeyPair()
        val depth = 1
        val childIndex = BigInteger(1)

        val hdKey = HDKey(
            privateKey = keyPair.privateKey.getEncoded(),
            depth = depth,
            childIndex = childIndex
        )

        assertFailsWith(Exception::class) {
            hdKey.deriveChild(childIndex)
        }
    }

    @Test
    fun testDeriveChild_whenPrivateKeyNotRightLength_thenThrowException() {
        val depth = 1
        val childIndex = BigInteger(1)

        val hdKey = HDKey(
            privateKey = Random.Default.nextBytes(33),
            depth = depth,
            childIndex = childIndex
        )

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
