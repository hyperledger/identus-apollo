package io.iohk.prism.apollo.hashing.sha2

import io.iohk.prism.apollo.hashing.BaseHashTests
import io.iohk.prism.apollo.hashing.SHA512_256
import io.iohk.prism.apollo.hashing.internal.toHexString
import kotlin.test.Test
import kotlin.test.assertEquals

class SHA512_256Tests : BaseHashTests() {
    override val valueForHash: List<String>
        get() = listOf(
            "c672b8d1ef56ed28ab87c3622c5114069bdd3ad7b8f9737498d0c01ecef0967a",
            "53048e2681941ef99b2e29b76b4c7dabe4c2d0c634fc6d46e0e2f13107e7af23",
            "bde8e1f9f19bb9fd3406c90ec6bc47bd36d8ada9f11880dbc8a22a7078b6a461",
            "3928e184fb8690f840da3988121d31be65cb9d3ef83ee6146feac861e19b563a",
            "455e518824bc0601f9fb858ff5c37d417d67c2f8e0df2babe4808858aea830f8",
            "835f9207766637f832cb3022f9d386b8b9426876f398d6b013a4925cc752806d"
        )

    override fun hash(stringToHash: String): String {
        val hash = SHA512_256()
        return hash.digest(stringToHash.encodeToByteArray()).toHexString()
    }

    @Test
    fun test_Strings() {
        for (i in stringsToHash.indices) {
            assertEquals(valueForHash[i], hash(stringsToHash[i]), "failed with hashing ${stringsToHash[i]}")
        }
        assertEquals("fc3189443f9c268f626aea08a756abe7b726b05f701cb08222312ccfd6710a26", hash("abcdefghijklmnopqrstuvwxyz"))
        assertEquals("cdf1cc0effe26ecc0c13758f7b4a48e000615df241284185c39eb05d355bb9c8", hash("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"))
        assertEquals("2c9fdbc0c90bdd87612ee8455474f9044850241dc105b1e8b94b8ddf5fac9148", hash("12345678901234567890123456789012345678901234567890123456789012345678901234567890"))
    }

    @Test
    fun test_MillionA() {
        assertEquals("9a59a052930187a97038cae692f30708aa6491923ef5194394dc68d56c74fb21", hash("a".repeat(1_000_000)), "failed with 1 million a")
    }

    /**
     * The following tests from:
     *  - https://csrc.nist.gov/CSRC/media/Projects/Cryptographic-Standards-and-Guidelines/documents/examples/SHA512_224.pdf
     *  - https://csrc.nist.gov/CSRC/media/Projects/Cryptographic-Standards-and-Guidelines/documents/examples/SHA2_Additional.pdf
     */
    @Test
    fun test_nist0Byte() {
        val hash = SHA512_256()
        assertEquals("c672b8d1ef56ed28ab87c3622c5114069bdd3ad7b8f9737498d0c01ecef0967a", hash.digest(ByteArray(0)).toHexString())
    }

    @Test
    fun test_nist111x0() {
        val hash = SHA512_256()
        assertEquals("5192ee5471d8a02ffc34bce87142df77aaef777dde522cc171af66e95a006a15", hash.digest(ByteArray(111) { 0 }).toHexString())
    }

    @Test
    fun test_nist112x0() {
        val hash = SHA512_256()
        assertEquals("ae534ff4eb3f2c1e11a16c566148e7aece987752797a8a555b75fb64ff58d54a", hash.digest(ByteArray(112) { 0 }).toHexString())
    }

    @Test
    fun test_nist113x0() {
        val hash = SHA512_256()
        assertEquals("20ce9c21bb5edbffae72135f58bab9fbabb2754614514a72888995c120556552", hash.digest(ByteArray(113) { 0 }).toHexString())
    }

    @Test
    fun test_nist122x0() {
        val hash = SHA512_256()
        assertEquals("2491eba0847e4daf54295002b1f18856582cf1e2ab6e9552847f49d1bc1e1d2d", hash.digest(ByteArray(122) { 0 }).toHexString())
    }

    @Test
    fun test_nist1000x00() {
        val hash = SHA512_256()
        assertEquals("4d7f9c6ab0204db4286fc0bf1ac45f01c2fe656c9650cef1892c2d128cf68221", hash.digest(ByteArray(1000) { 0 }).toHexString())
    }

    @Test
    fun test_nist1000xA() {
        val hash = SHA512_256()
        assertEquals("6ad592c8991fa0fc0fc78b6c2e73f3b55db74afeb1027a5aeacb787fb531e64a", hash.digest(ByteArray(1000) { 'A'.code.toByte() }).toHexString())
    }

    @Test
    fun test_nist1005xU() {
        val hash = SHA512_256()
        assertEquals("bf1fa2390bab18685fb16564339085bed2b980b8b31dedca9fbc8cc846299f96", hash.digest(ByteArray(1005) { 'U'.code.toByte() }).toHexString())
    }

    @Test
    fun test_nist1million() {
        val hash = SHA512_256()
        assertEquals("8b620ff17fd0414c7c3567704f9e275a5c37801720c75dc05cf81558e4a0f965", hash.digest(ByteArray(1000000) { 0 }).toHexString())
    }
}
