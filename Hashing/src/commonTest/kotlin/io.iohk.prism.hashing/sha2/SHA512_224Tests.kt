package io.iohk.prism.hashing.sha2

import io.iohk.prism.hashing.BaseHashTests
import io.iohk.prism.hashing.SHA512_224
import io.iohk.prism.hashing.internal.toHexString
import kotlin.test.Test
import kotlin.test.assertEquals

class SHA512_224Tests : BaseHashTests() {
    override val valueForHash: List<String>
        get() = listOf(
            "6ed0dd02806fa89e25de060c19d3ac86cabb87d6a0ddd05c333b84f4",
            "4634270f707b6a54daae7530460842e20e37ed265ceee9a43e8924aa",
            "e5302d6d54bb242275d1e7622d68df6eb02dedd13f564c13dbda2174",
            "23fec5bb94d60b23308192640b0c453335d664734fe40e7268674af9",
            "d5cdb9ccc769a5121d4175f2bfdd13d6310e0d3d361ea75d82108327",
            "fc9be3101845460350061160d05d1092d5d2eb72d62efcaa4f453bf7"
        )

    override fun hash(stringToHash: String): String {
        val hash = SHA512_224()
        return hash.digest(stringToHash.encodeToByteArray()).toHexString()
    }

    @Test
    fun test_Strings() {
        for (i in stringsToHash.indices) {
            assertEquals(valueForHash[i], hash(stringsToHash[i]), "failed with hashing ${stringsToHash[i]}")
        }
        assertEquals("ff83148aa07ec30655c1b40aff86141c0215fe2a54f767d3f38743d8", hash("abcdefghijklmnopqrstuvwxyz"))
        assertEquals("a8b4b9174b99ffc67d6f49be9981587b96441051e16e6dd036b140d3", hash("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"))
        assertEquals("ae988faaa47e401a45f704d1272d99702458fea2ddc6582827556dd2", hash("12345678901234567890123456789012345678901234567890123456789012345678901234567890"))
    }

    @Test
    fun test_MillionA() {
        assertEquals("37ab331d76f0d36de422bd0edeb22a28accd487b7a8453ae965dd287", hash("a".repeat(1_000_000)), "failed with 1 million a")
    }

    /**
     * The following tests from:
     *  - https://csrc.nist.gov/CSRC/media/Projects/Cryptographic-Standards-and-Guidelines/documents/examples/SHA512_224.pdf
     *  - https://csrc.nist.gov/CSRC/media/Projects/Cryptographic-Standards-and-Guidelines/documents/examples/SHA2_Additional.pdf
     */
    @Test
    fun test_nist0Byte() {
        val hash = SHA512_224()
        assertEquals("6ed0dd02806fa89e25de060c19d3ac86cabb87d6a0ddd05c333b84f4", hash.digest(ByteArray(0)).toHexString())
    }

    @Test
    fun test_nist111x0() {
        val hash = SHA512_224()
        assertEquals("a23413341d5c14ac3dd1d7136796abe8d0e228f3e4ab4d3ed2c95902", hash.digest(ByteArray(111) { 0 }).toHexString())
    }

    @Test
    fun test_nist112x0() {
        val hash = SHA512_224()
        assertEquals("1fea579628bc0eb589647ec098d5eae4c29d158ea8285ef6ae53810d", hash.digest(ByteArray(112) { 0 }).toHexString())
    }

    @Test
    fun test_nist113x0() {
        val hash = SHA512_224()
        assertEquals("d9b583f4ca8fbc5c582566d356a1ac4285bfc60edcbbfc607ea4ef5a", hash.digest(ByteArray(113) { 0 }).toHexString())
    }

    @Test
    fun test_nist122x0() {
        val hash = SHA512_224()
        assertEquals("c80537aeddb88c3eb9fc5d7d287f571806c9ccdeb7d819260ddf9ae8", hash.digest(ByteArray(122) { 0 }).toHexString())
    }

    @Test
    fun test_nist1000x00() {
        val hash = SHA512_224()
        assertEquals("9109bfe74891b1fdc9ef4947024024fbd702c85df1756a016b136df7", hash.digest(ByteArray(1000) { 0 }).toHexString())
    }

    @Test
    fun test_nist1000xA() {
        val hash = SHA512_224()
        assertEquals("3000c31a7ab8e9c760257073c4d3be370fab6d1d28eb027c6d874f29", hash.digest(ByteArray(1000) { 'A'.code.toByte() }).toHexString())
    }

    @Test
    fun test_nist1005xU() {
        val hash = SHA512_224()
        assertEquals("9d980f5f97041c4e9b84b2b91c10ad8e8de73635ab8b81071a77c6c6", hash.digest(ByteArray(1005) { 'U'.code.toByte() }).toHexString())
    }

    @Test
    fun test_nist1million() {
        val hash = SHA512_224()
        assertEquals("7576f5b118a2ddc31ab05c641f04027fed5f1cbb65894d17ec664466", hash.digest(ByteArray(1000000) { 0 }).toHexString())
    }
}
