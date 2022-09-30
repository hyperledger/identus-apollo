package io.iohk.prism.hashing.sha2

import io.iohk.prism.hashing.BaseHashTests
import io.iohk.prism.hashing.SHA256
import io.iohk.prism.hashing.internal.JsIgnore
import io.iohk.prism.hashing.internal.toBinary
import io.iohk.prism.hashing.internal.toHexString
import kotlinx.coroutines.test.runTest
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals

class SHA256Tests : BaseHashTests() {
    override val valueForHash: List<String>
        get() = listOf(
            "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855",
            "ba7816bf8f01cfea414140de5dae2223b00361a396177a9cb410ff61f20015ad",
            "248d6a61d20638b8e5c026930c3e6039a33ce45964ff2167f6ecedd419db06c1",
            "cf5b16a778af8380036ce59e7b0492370b249b11e8f07a51afac45037afee9d1",
            "ca978112ca1bbdcafac231b39a23dc4da786eff8147c4e72b9807785afee48bb",
            "2ff100b36c386c65a1afc462ad53e25479bec9498ed00aa5a04de584bc25301b"
        )

    override fun hash(stringToHash: String): String {
        val hash = SHA256()
        return hash.digest(stringToHash.encodeToByteArray()).toHexString()
    }

    @Test
    fun test_Strings() {
        for (i in stringsToHash.indices) {
            assertEquals(valueForHash[i], hash(stringsToHash[i]), "failed with hashing ${stringsToHash[i]}")
        }
        assertEquals("71c480df93d6ae2f1efad1447c66c9525e316218cf51fc8d9ed832f2daf18b73", hash("abcdefghijklmnopqrstuvwxyz"))
        assertEquals("db4bfcbd4da0cd85a60c3c37d3fbd8805c77f15fc6b1fdfe614ee0a7c8fdb4c0", hash("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"))
        assertEquals("f371bc4a311f2b009eef952dd83ca80e2b60026c8e935592d0f9c308453c813e", hash("12345678901234567890123456789012345678901234567890123456789012345678901234567890"))
    }

    @Test
    fun test_MillionA() {
        assertEquals("cdc76e5c9914fb9281a1c7e284d73e67f1809a48a497200e046d39ccc7112cd0", hash("a".repeat(1_000_000)), "failed with 1 million a")
    }

    @Test
    @JsIgnore
    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    fun test_VeryLong() = runTest {
        val hash = SHA256()
        repeat(16_777_216) {
            hash.update("abcdefghbcdefghicdefghijdefghijkefghijklfghijklmghijklmnhijklmno".encodeToByteArray())
        }
        assertEquals("50e72a0e26442fe2552dc3938ac58658228c0cbfb1d2ca872ae435266fcd055e", hash.digest().toHexString())
    }

    /**
     * The following tests from:
     *  - https://csrc.nist.gov/CSRC/media/Projects/Cryptographic-Standards-and-Guidelines/documents/examples/SHA256.pdf
     *  - https://csrc.nist.gov/CSRC/media/Projects/Cryptographic-Standards-and-Guidelines/documents/examples/SHA2_Additional.pdf
     */
    @Test
    fun test_nist1Byte() {
        val hash = SHA256()
        assertEquals("68325720aabd7c82f30f554b313d0570c95accbb7dc4b5aae11204c08ffe732b", hash.digest(ByteArray(1) { 0xbd.toByte() }).toHexString())
    }

    @Test
    fun test_nist4Bytes() {
        val hash = SHA256()
        assertEquals("7abc22c0ae5af26ce93dbb94433a0e0b2e119d014f8e7f65bd56c61ccccd9504", hash.digest("c98c8e55".toBinary()).toHexString())
    }

    @Test
    fun test_nist55BytesOfZero() {
        val hash = SHA256()
        assertEquals("02779466cdec163811d078815c633f21901413081449002f24aa3e80f0b88ef7", hash.digest(ByteArray(55) { 0 }).toHexString())
    }

    @Test
    fun test_nist56BytesOfZero() {
        val hash = SHA256()
        assertEquals("d4817aa5497628e7c77e6b606107042bbba3130888c5f47a375e6179be789fbb", hash.digest(ByteArray(56) { 0 }).toHexString())
    }

    @Test
    fun test_nist57BytesOfZero() {
        val hash = SHA256()
        assertEquals("65a16cb7861335d5ace3c60718b5052e44660726da4cd13bb745381b235a1785", hash.digest(ByteArray(57) { 0 }).toHexString())
    }

    @Test
    fun test_nist64BytesOfZero() {
        val hash = SHA256()
        assertEquals("f5a5fd42d16a20302798ef6ed309979b43003d2320d9f0e8ea9831a92759fb4b", hash.digest(ByteArray(64) { 0 }).toHexString())
    }

    @Test
    fun test_nist1000x00() {
        val hash = SHA256()
        assertEquals("541b3e9daa09b20bf85fa273e5cbd3e80185aa4ec298e765db87742b70138a53", hash.digest(ByteArray(1000) { 0 }).toHexString())
    }

    @Test
    fun test_nist1000xA() {
        val hash = SHA256()
        assertEquals("c2e686823489ced2017f6059b8b239318b6364f6dcd835d0a519105a1eadd6e4", hash.digest(ByteArray(1000) { 'A'.code.toByte() }).toHexString())
    }

    @Test
    fun test_nist1005xU() {
        val hash = SHA256()
        assertEquals("f4d62ddec0f3dd90ea1380fa16a5ff8dc4c54b21740650f24afc4120903552b0", hash.digest(ByteArray(1005) { 'U'.code.toByte() }).toHexString())
    }

    @Test
    fun test_nist1million() {
        val hash = SHA256()
        assertEquals("d29751f2649b32ff572b5e0a9f541ea660a50f94ff0beedfb0b692b924cc8025", hash.digest(ByteArray(1000000) { 0 }).toHexString())
    }

    @Test
    @Ignore // Java Heap Size
    fun test_nist536870912xZ() {
        val hash = SHA256()
        assertEquals("15a1868c12cc53951e182344277447cd0979536badcc512ad24c67e9b2d4f3dd", hash.digest(ByteArray(0x20000000) { 'Z'.code.toByte() }).toHexString())
    }

    @Test
    @Ignore // Java Heap Size
    fun test_nist1090519040x00() {
        val hash = SHA256()
        assertEquals("461c19a93bd4344f9215f5ec64357090342bc66b15a148317d276e31cbc20b53", hash.digest(ByteArray(0x41000000) { 0 }).toHexString())
    }

    @Test
    @Ignore // Java Heap Size
    fun test_nist1610612798xB() {
        val hash = SHA256()
        assertEquals("c23ce8a7895f4b21ec0daf37920ac0a262a220045a03eb2dfed48ef9b05aabea", hash.digest(ByteArray(0x6000003e) { 'B'.code.toByte() }).toHexString())
    }
}
