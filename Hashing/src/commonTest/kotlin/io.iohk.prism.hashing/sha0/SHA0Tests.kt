package io.iohk.prism.hashing.sha0

import io.iohk.prism.hashing.BaseHashTests
import io.iohk.prism.hashing.SHA0
import io.iohk.prism.hashing.internal.toHexString
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class SHA0Tests : BaseHashTests() {
    override val valueForHash: List<String>
        get() = listOf(
            "f96cea198ad1dd5617ac084a3d92c6107708c0ef",
            "0164b8a914cd2a5e74c4f7ff082c4d97f1edf880",
            "d2516ee1acfa5baf33dfc1c471e438449ef134c8",
            "459f83b95db2dc87bb0f5b513a28f900ede83237",
            "37f297772fae4cb1ba39b6cf9cf0381180bd62f2",
            "0a7c923bfb995b989344ac75092770daab723c96"
        )

    override fun hash(stringToHash: String): String {
        val hash = SHA0()
        return hash.digest(stringToHash.encodeToByteArray()).toHexString()
    }

    @Test
    fun test_Strings() {
        for (i in stringsToHash.indices) {
            assertEquals(valueForHash[i], hash(stringsToHash[i]), "failed with hashing ${stringsToHash[i]}")
        }
        assertEquals("b40ce07a430cfd3c033039b9fe9afec95dc1bdcd", hash("abcdefghijklmnopqrstuvwxyz"))
        assertEquals("79e966f7a3a990df33e40e3d7f8f18d2caebadfa", hash("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"))
        assertEquals("4aa29d14d171522ece47bee8957e35a41f3e9cff", hash("12345678901234567890123456789012345678901234567890123456789012345678901234567890"))
    }

    @Test
    fun test_MillionA() {
        assertEquals("3232affa48628a26653b5aaa44541fd90d690603", hash("a".repeat(1_000_000)))
    }

    @Test
    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    fun test_VeryLong() = runTest {
        val hash = SHA0()
        repeat(16_777_216) {
            hash.update("abcdefghbcdefghicdefghijdefghijkefghijklfghijklmghijklmnhijklmno".encodeToByteArray())
        }
        assertEquals("bd18f2e7736c8e6de8b5abdfdeab948f5171210c", hash.digest().toHexString())
    }
}
