package io.iohk.prism.hashing.sha3

import io.iohk.prism.hashing.BaseHashTests
import io.iohk.prism.hashing.SHA3_256
import io.iohk.prism.hashing.internal.toHexString
import kotlinx.coroutines.test.runTest
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals

class SHA3_256Tests : BaseHashTests() {
    override val valueForHash: List<String>
        get() = listOf(
            "a7ffc6f8bf1ed76651c14756a061d662f580ff4de43b49fa82d80a4b80f8434a",
            "3a985da74fe225b2045c172d6bd390bd855f086e3e9d525b46bfe24511431532",
            "41c0dba2a9d6240849100376a8235e2c82e1b9998a999e21db32dd97496d3376",
            "916f6061fe879741ca6469b43971dfdb28b1a32dc36cb3254e812be27aad1d18",
            "80084bf2fba02475726feb2cab2d8215eab14bc6bdd8bfb2c8151257032ecd8b",
            "3706569f9a29d62991ebe62f080ea3fac18034d2fffd23b136c10f7148fceb38"
        )

    override fun hash(stringToHash: String): String {
        val hash = SHA3_256()
        return hash.digest(stringToHash.encodeToByteArray()).toHexString()
    }

    @Test
    fun test_Strings() {
        for (i in stringsToHash.indices) {
            assertEquals(valueForHash[i], hash(stringsToHash[i]), "failed with hashing ${stringsToHash[i]}")
        }
        assertEquals("7cab2dc765e21b241dbc1c255ce620b29f527c6d5e7f5f843e56288f0d707521", hash("abcdefghijklmnopqrstuvwxyz"))
        assertEquals("a79d6a9da47f04a3b9a9323ec9991f2105d4c78a7bc7beeb103855a7a11dfb9f", hash("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"))
        assertEquals("293e5ce4ce54ee71990ab06e511b7ccd62722b1beb414f5ff65c8274e0f5be1d", hash("12345678901234567890123456789012345678901234567890123456789012345678901234567890"))
    }

    @Test
    fun test_MillionA() {
        assertEquals("5c8875ae474a3634ba4fd55ec85bffd661f32aca75c6d699d0cdcb6c115891c1", hash("a".repeat(1_000_000)))
    }

    @Test
    @Ignore // takes too long
    fun test_VeryLong() = runTest {
        val hash = SHA3_256()
        repeat(16_777_216) {
            hash.update("abcdefghbcdefghicdefghijdefghijkefghijklfghijklmghijklmnhijklmno".encodeToByteArray())
        }
        assertEquals("ecbbc42cbf296603acb2c6bc0410ef4378bafb24b710357f12df607758b33e2b", hash.digest().toHexString())
    }
}
