package io.iohk.prism.hashing.sha3

import io.iohk.prism.hashing.BaseHashTests
import io.iohk.prism.hashing.SHA3_224
import io.iohk.prism.hashing.internal.toHexString
import kotlinx.coroutines.test.runTest
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals

class SHA3_224Tests : BaseHashTests() {
    override val valueForHash: List<String>
        get() = listOf(
            "6b4e03423667dbb73b6e15454f0eb1abd4597f9a1b078e3f5b5a6bc7",
            "e642824c3f8cf24ad09234ee7d3c766fc9a3a5168d0c94ad73b46fdf",
            "8a24108b154ada21c9fd5574494479ba5c7e7ab76ef264ead0fcce33",
            "543e6868e1666c1a643630df77367ae5a62a85070a51c14cbf665cbc",
            "9e86ff69557ca95f405f081269685b38e3a819b309ee942f482b6a8b",
            "b6091c08b046b400e6e03caec49ec3d023c0607db87848919b47ce0b"
        )

    override fun hash(stringToHash: String): String {
        val hash = SHA3_224()
        return hash.digest(stringToHash.encodeToByteArray()).toHexString()
    }

    @Test
    fun test_Strings() {
        for (i in stringsToHash.indices) {
            assertEquals(valueForHash[i], hash(stringsToHash[i]), "failed with hashing ${stringsToHash[i]}")
        }
        assertEquals("5cdeca81e123f87cad96b9cba999f16f6d41549608d4e0f4681b8239", hash("abcdefghijklmnopqrstuvwxyz"))
        assertEquals("a67c289b8250a6f437a20137985d605589a8c163d45261b15419556e", hash("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"))
        assertEquals("0526898e185869f91b3e2a76dd72a15dc6940a67c8164a044cd25cc8", hash("12345678901234567890123456789012345678901234567890123456789012345678901234567890"))
    }

    @Test
    fun test_MillionA() {
        assertEquals("d69335b93325192e516a912e6d19a15cb51c6ed5c15243e7a7fd653c", hash("a".repeat(1_000_000)))
    }

    @Test
    @Ignore // takes too long
    fun test_VeryLong() = runTest {
        val hash = SHA3_224()
        repeat(16_777_216) {
            hash.update("abcdefghbcdefghicdefghijdefghijkefghijklfghijklmghijklmnhijklmno".encodeToByteArray())
        }
        assertEquals("c6d66e77ae289566afb2ce39277752d6da2a3c46010f1e0a0970ff60", hash.digest().toHexString())
    }
}
