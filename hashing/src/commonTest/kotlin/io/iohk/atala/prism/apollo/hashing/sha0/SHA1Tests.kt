package io.iohk.atala.prism.apollo.hashing.sha0

import io.iohk.atala.prism.apollo.hashing.BaseHashTests
import io.iohk.atala.prism.apollo.hashing.SHA1
import io.iohk.atala.prism.apollo.hashing.internal.JsIgnore
import io.iohk.atala.prism.apollo.hashing.internal.toHexString
import kotlinx.coroutines.test.runTest
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals

@JsIgnore
class SHA1Tests : BaseHashTests() {
    override val valueForHash: List<String>
        get() = listOf(
            "da39a3ee5e6b4b0d3255bfef95601890afd80709",
            "a9993e364706816aba3e25717850c26c9cd0d89d",
            "84983e441c3bd26ebaae4aa1f95129e5e54670f1",
            "a49b2446a02c645bf419f995b67091253a04a259",
            "86f7e437faa5a7fce15d1ddcb9eaeaea377667b8",
            "b85d6468bd3a73794bceaf812239cc1fe460ab95"
        )

    override fun hash(stringToHash: String): String {
        val hash = SHA1()
        return hash.digest(stringToHash.encodeToByteArray()).toHexString()
    }

    @Test
    fun test_Strings() {
        for (i in stringsToHash.indices) {
            assertEquals(valueForHash[i], hash(stringsToHash[i]), "failed with hashing ${stringsToHash[i]}")
        }
        assertEquals("32d10c7b8cf96570ca04ce37f2a19d84240d3a89", hash("abcdefghijklmnopqrstuvwxyz"))
        assertEquals("761c457bf73b14d27e9e9265c46f4b4dda11f940", hash("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"))
        assertEquals("50abf5706a150990a08b2c5ea40fa0e585554732", hash("12345678901234567890123456789012345678901234567890123456789012345678901234567890"))
    }

    @Test
    fun test_MillionA() {
        assertEquals("34aa973cd4c4daa4f61eeb2bdbad27316534016f", hash("a".repeat(1_000_000)))
    }

    @Test
    @Ignore
    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    fun test_VeryLong() = runTest {
        val hash = SHA1()
        repeat(16_777_216) {
            hash.update("abcdefghbcdefghicdefghijdefghijkefghijklfghijklmghijklmnhijklmno".encodeToByteArray())
        }
        assertEquals("7789f0c9ef7bfc40d93311143dfbe69e2017f592", hash.digest().toHexString())
    }
}
