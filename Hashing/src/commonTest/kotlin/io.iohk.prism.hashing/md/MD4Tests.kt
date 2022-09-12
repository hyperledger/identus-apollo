package io.iohk.prism.hashing.md

import io.iohk.prism.hashing.BaseHashTests
import io.iohk.prism.hashing.MD4
import io.iohk.prism.hashing.internal.toHexString
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class MD4Tests: BaseHashTests() {
    override val valueForHash: List<String>
        get() = listOf(
            "31d6cfe0d16ae931b73c59d7e0c089c0",
            "a448017aaf21d8525fc10ae87aa6729d",
            "4691a9ec81b1a6bd1ab8557240b245c5",
            "2102d1d94bd58ebf5aa25c305bb783ad",
            "bde52cb31de33e46245e05fbdbd6fb24",
            "025e6dc9825f2c1dd5c450e2dd0eb683"
        )

    override fun hash(stringToHash: String): String {
        val hash = MD4()
        return hash.digest(stringToHash.encodeToByteArray()).toHexString()
    }

    @Test
    fun test_Strings() {
        for (i in stringsToHash.indices) {
            assertEquals(valueForHash[i], hash(stringsToHash[i]), "failed with hashing ${stringsToHash[i]}")
        }
        assertEquals("d79e1c308aa5bbcdeea8ed63df412da9", hash("abcdefghijklmnopqrstuvwxyz"))
        assertEquals("043f8582f241db351ce627e153e7f0e4", hash("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"))
        assertEquals("e33b4ddc9c38f2199c3e7b164fcc0536", hash("12345678901234567890123456789012345678901234567890123456789012345678901234567890"))
    }

    @Test
    fun test_MillionA() {
        assertEquals("bbce80cc6bb65e5c6745e30d4eeca9a4", hash("a".repeat(1_000_000)))
    }

    @Test
    fun test_VeryLong() = runTest {
        val hash = MD4()
        repeat(16_777_216) {
            hash.update("abcdefghbcdefghicdefghijdefghijkefghijklfghijklmghijklmnhijklmno".encodeToByteArray())
        }
        assertEquals("699057dc7272ba3db0e32f09b8ab8442",  hash.digest().toHexString())
    }
}
