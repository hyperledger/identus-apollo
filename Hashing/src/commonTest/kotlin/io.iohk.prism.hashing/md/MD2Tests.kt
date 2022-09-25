package io.iohk.prism.hashing.md

import io.iohk.prism.hashing.BaseHashTests
import io.iohk.prism.hashing.MD2
import io.iohk.prism.hashing.internal.toHexString
import kotlinx.coroutines.test.runTest
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals

class MD2Tests : BaseHashTests() {
    override val valueForHash: List<String>
        get() = listOf(
            "8350e5a3e24c153df2275c9f80692773",
            "da853b0d3f88d99b30283a69e6ded6bb",
            "0dff6b398ad5a62ac8d97566b80c3a7f",
            "2c194d0376411dc0b8485d3abe2a4b6b",
            "32ec01ec4a6dac72c0ab96fb34c0b5d1",
            "7769040d32d6bc4474932bfee7a30ff0"
        )

    override fun hash(stringToHash: String): String {
        val hash = MD2()
        return hash.digest(stringToHash.encodeToByteArray()).toHexString()
    }

    @Test
    fun test_Strings() {
        for (i in stringsToHash.indices) {
            assertEquals(valueForHash[i], hash(stringsToHash[i]), "failed with hashing ${stringsToHash[i]}")
        }
        assertEquals("4e8ddff3650292ab5a4108c3aa47940b", hash("abcdefghijklmnopqrstuvwxyz"))
        assertEquals("da33def2a42df13975352846c30338cd", hash("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"))
        assertEquals("d5976f79d83d3a0dc9806c3c66f3efd8", hash("12345678901234567890123456789012345678901234567890123456789012345678901234567890"))
    }

    @Test
    fun test_MillionA() {
        assertEquals("8c0a09ff1216ecaf95c8130953c62efd", hash("a".repeat(1_000_000)))
    }

    @Test
    @Ignore // takes too long
    fun test_VeryLong() = runTest {
        val hash = MD2()
        repeat(16_777_216) {
            hash.update("abcdefghbcdefghicdefghijdefghijkefghijklfghijklmghijklmnhijklmno".encodeToByteArray())
        }
        assertEquals("596d0463369fda2f80ed901edd462eff", hash.digest().toHexString())
    }
}
