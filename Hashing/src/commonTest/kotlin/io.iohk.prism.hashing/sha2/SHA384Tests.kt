package io.iohk.prism.hashing.sha2

import io.iohk.prism.hashing.BaseHashTests
import io.iohk.prism.hashing.SHA384
import io.iohk.prism.hashing.internal.toHexString
import kotlinx.coroutines.test.runTest
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals

class SHA384Tests : BaseHashTests() {
    override val valueForHash: List<String>
        get() = listOf(
            "38b060a751ac96384cd9327eb1b1e36a21fdb71114be07434c0cc7bf63f6e1da274edebfe76f65fbd51ad2f14898b95b",
            "cb00753f45a35e8bb5a03d699ac65007272c32ab0eded1631a8b605a43ff5bed8086072ba1e7cc2358baeca134c825a7",
            "3391fdddfc8dc7393707a65b1b4709397cf8b1d162af05abfe8f450de5f36bc6b0455a8520bc4e6f5fe95b1fe3c8452b",
            "09330c33f71147e83d192fc782cd1b4753111b173b3b05d22fa08086e3b0f712fcc7c71a557e2db966c3e9fa91746039",
            "54a59b9f22b0b80880d8427e548b7c23abd873486e1f035dce9cd697e85175033caa88e6d57bc35efae0b5afd3145f31",
            "bdc0f4a6e0d7de88f374e6c2562441d856aeabed3f52553103f55eca811f64b422c7cb47a8067f123e45c1a8ee303635"
        )

    override fun hash(stringToHash: String): String {
        val hash = SHA384()
        return hash.digest(stringToHash.encodeToByteArray()).toHexString()
    }

    @Test
    fun test_Strings() {
        for (i in stringsToHash.indices) {
            assertEquals(valueForHash[i], hash(stringsToHash[i]), "failed with hashing ${stringsToHash[i]}")
        }
        assertEquals("feb67349df3db6f5924815d6c3dc133f091809213731fe5c7b5f4999e463479ff2877f5f2936fa63bb43784b12f3ebb4", hash("abcdefghijklmnopqrstuvwxyz"))
        assertEquals("1761336e3f7cbfe51deb137f026f89e01a448e3b1fafa64039c1464ee8732f11a5341a6f41e0c202294736ed64db1a84", hash("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"))
        assertEquals("b12932b0627d1c060942f5447764155655bd4da0c9afa6dd9b9ef53129af1b8fb0195996d2de9ca0df9d821ffee67026", hash("12345678901234567890123456789012345678901234567890123456789012345678901234567890"))
    }

    @Test
    fun test_MillionA() {
        assertEquals("9d0e1809716474cb086e834e310a4a1ced149e9c00f248527972cec5704c2a5b07b8b3dc38ecc4ebae97ddd87f3d8985", hash("a".repeat(1_000_000)), "failed with 1 million a")
    }

    @Test
    fun test_VeryLong() = runTest {
        val hash = SHA384()
        repeat(16_777_216) {
            hash.update("abcdefghbcdefghicdefghijdefghijkefghijklfghijklmghijklmnhijklmno".encodeToByteArray())
        }
        assertEquals("5441235cc0235341ed806a64fb354742b5e5c02a3c5cb71b5f63fb793458d8fdae599c8cd8884943c04f11b31b89f023", hash.digest().toHexString())
    }

    /**
     * The following tests from:
     *  - https://csrc.nist.gov/CSRC/media/Projects/Cryptographic-Standards-and-Guidelines/documents/examples/SHA384.pdf
     *  - https://csrc.nist.gov/CSRC/media/Projects/Cryptographic-Standards-and-Guidelines/documents/examples/SHA2_Additional.pdf
     */
    @Test
    fun test_nist0Byte() {
        val hash = SHA384()
        assertEquals("38b060a751ac96384cd9327eb1b1e36a21fdb71114be07434c0cc7bf63f6e1da274edebfe76f65fbd51ad2f14898b95b", hash.digest(ByteArray(0)).toHexString())
    }

    @Test
    fun test_nist111x0() {
        val hash = SHA384()
        assertEquals("435770712c611be7293a66dd0dc8d1450dc7ff7337bfe115bf058ef2eb9bed09cee85c26963a5bcc0905dc2df7cc6a76", hash.digest(ByteArray(111) { 0 }).toHexString())
    }

    @Test
    fun test_nist112x0() {
        val hash = SHA384()
        assertEquals("3e0cbf3aee0e3aa70415beae1bd12dd7db821efa446440f12132edffce76f635e53526a111491e75ee8e27b9700eec20", hash.digest(ByteArray(112) { 0 }).toHexString())
    }

    @Test
    fun test_nist113x0() {
        val hash = SHA384()
        assertEquals("6be9af2cf3cd5dd12c8d9399ec2b34e66034fbd699d4e0221d39074172a380656089caafe8f39963f94cc7c0a07e3d21", hash.digest(ByteArray(113) { 0 }).toHexString())
    }

    @Test
    fun test_nist122x0() {
        val hash = SHA384()
        assertEquals("12a72ae4972776b0db7d73d160a15ef0d19645ec96c7f816411ab780c794aa496a22909d941fe671ed3f3caee900bdd5", hash.digest(ByteArray(122) { 0 }).toHexString())
    }

    @Test
    fun test_nist1000x00() {
        val hash = SHA384()
        assertEquals("aae017d4ae5b6346dd60a19d52130fb55194b6327dd40b89c11efc8222292de81e1a23c9b59f9f58b7f6ad463fa108ca", hash.digest(ByteArray(1000) { 0 }).toHexString())
    }

    @Test
    fun test_nist1000xA() {
        val hash = SHA384()
        assertEquals("7df01148677b7f18617eee3a23104f0eed6bb8c90a6046f715c9445ff43c30d69e9e7082de39c3452fd1d3afd9ba0689", hash.digest(ByteArray(1000) { 'A'.code.toByte() }).toHexString())
    }

    @Test
    fun test_nist1005xU() {
        val hash = SHA384()
        assertEquals("1bb8e256da4a0d1e87453528254f223b4cb7e49c4420dbfa766bba4adba44eeca392ff6a9f565bc347158cc970ce44ec", hash.digest(ByteArray(1005) { 'U'.code.toByte() }).toHexString())
    }

    @Test
    fun test_nist1million() {
        val hash = SHA384()
        assertEquals("8a1979f9049b3fff15ea3a43a4cf84c634fd14acad1c333fecb72c588b68868b66a994386dc0cd1687b9ee2e34983b81", hash.digest(ByteArray(1000000) { 0 }).toHexString())
    }

    @Test
    @Ignore // Java Heap Size
    fun test_nist536870912xZ() {
        val hash = SHA384()
        assertEquals("18aded227cc6b562cc7fb259e8f404549e52914531aa1c5d85167897c779cc4b25d0425fd1590e40bd763ec3f4311c1a", hash.digest(ByteArray(0x20000000) { 'Z'.code.toByte() }).toHexString())
    }

    @Test
    @Ignore // Java Heap Size
    fun test_nist1090519040x00() {
        val hash = SHA384()
        assertEquals("83ab05ca483abe3faa597ad524d31291ae827c5be2b3efcb6391bfed31ccd937b6135e0378c6c7f598857a7c516f207a", hash.digest(ByteArray(0x41000000) { 0 }).toHexString())
    }

    @Test
    @Ignore // Java Heap Size
    fun test_nist1610612798xB() {
        val hash = SHA384()
        assertEquals("cf852304f8d80209351b37ce69ca7dcf34972b4edb7817028ec55ab67ad3bc96eecb8241734258a85d2afce65d4571e2", hash.digest(ByteArray(0x6000003e) { 'B'.code.toByte() }).toHexString())
    }
}
