package io.iohk.prism.apollo.hashing.md

import io.iohk.prism.apollo.hashing.BaseHashTests
import io.iohk.prism.apollo.hashing.MD5
import io.iohk.prism.apollo.hashing.internal.toHexString
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class MD5Tests : BaseHashTests() {
    override val valueForHash: List<String>
        get() = listOf(
            "d41d8cd98f00b204e9800998ecf8427e",
            "900150983cd24fb0d6963f7d28e17f72",
            "8215ef0796a20bcaaae116d3876c664a",
            "03dd8807a93175fb062dfb55dc7d359c",
            "0cc175b9c0f1b6a831c399e269772661",
            "2782e38354c31d1b1d6dfb6f4ccb2d2e"
        )

    override fun hash(stringToHash: String): String {
        val hash = MD5()
        return hash.digest(stringToHash.encodeToByteArray()).toHexString()
    }

    @Test
    fun test_Strings() {
        for (i in stringsToHash.indices) {
            assertEquals(valueForHash[i], hash(stringsToHash[i]), "failed with hashing ${stringsToHash[i]}")
        }
        assertEquals("c3fcd3d76192e4007dfb496cca67e13b", hash("abcdefghijklmnopqrstuvwxyz"))
        assertEquals("d174ab98d277d9f5a5611c2c9f419d9f", hash("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"))
        assertEquals("57edf4a22be3c955ac49da2e2107b67a", hash("12345678901234567890123456789012345678901234567890123456789012345678901234567890"))
    }

    @Test
    fun test_MillionA() {
        assertEquals("7707d6ae4e027c70eea2a935c2296f21", hash("a".repeat(1_000_000)))
    }

    @Test
    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    fun test_VeryLong() = runTest {
        val hash = MD5()
        repeat(16_777_216) {
            hash.update("abcdefghbcdefghicdefghijdefghijkefghijklfghijklmghijklmnhijklmno".encodeToByteArray())
        }
        assertEquals("d338139169d50f55526194c790ec0448", hash.digest().toHexString())
    }
}
