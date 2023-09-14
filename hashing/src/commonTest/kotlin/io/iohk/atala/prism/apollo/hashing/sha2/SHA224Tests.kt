package io.iohk.atala.prism.apollo.hashing.sha2

import io.iohk.atala.prism.apollo.hashing.BaseHashTests
import io.iohk.atala.prism.apollo.hashing.SHA224
import io.iohk.atala.prism.apollo.hashing.internal.toBinary
import io.iohk.atala.prism.apollo.hashing.internal.toHexString
import kotlinx.coroutines.test.runTest
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals

class SHA224Tests : BaseHashTests() {
    override val valueForHash: List<String>
        get() = listOf(
            "d14a028c2a3a2bc9476102bb288234c415a2b01f828ea62ac5b3e42f",
            "23097d223405d8228642a477bda255b32aadbce4bda0b3f7e36c9da7",
            "75388b16512776cc5dba5da1fd890150b0c6455cb4f58b1952522525",
            "c97ca9a559850ce97a04a96def6d99a9e0e0e2ab14e6b8df265fc0b3",
            "abd37534c7d9a2efb9465de931cd7055ffdb8879563ae98078d6d6d5",
            "4176f330539b0ed8b0b6b5dea7c8e47a18fc4daf3f53920355b0926a"
        )

    override fun hash(stringToHash: String): String {
        val hash = SHA224()
        return hash.digest(stringToHash.encodeToByteArray()).toHexString()
    }

    @Test
    fun test_Strings() {
        for (i in stringsToHash.indices) {
            assertEquals(valueForHash[i], hash(stringsToHash[i]), "failed with hashing ${stringsToHash[i]}")
        }
        assertEquals("45a5f72c39c5cff2522eb3429799e49e5f44b356ef926bcf390dccc2", hash("abcdefghijklmnopqrstuvwxyz"))
        assertEquals("bff72b4fcb7d75e5632900ac5f90d219e05e97a7bde72e740db393d9", hash("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"))
        assertEquals("b50aecbe4e9bb0b57bc5f3ae760a8e01db24f203fb3cdcd13148046e", hash("12345678901234567890123456789012345678901234567890123456789012345678901234567890"))
    }

    @Test
    fun test_MillionA() {
        assertEquals("20794655980c91d8bbb4c1ea97618a4bf03f42581948b2ee4ee7ad67", hash("a".repeat(1_000_000)), "failed with 1 million a")
    }

    @Test
    @Ignore
    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    fun test_VeryLong() = runTest {
        val hash = SHA224()
        repeat(16_777_216) {
            hash.update("abcdefghbcdefghicdefghijdefghijkefghijklfghijklmghijklmnhijklmno".encodeToByteArray())
        }
        assertEquals("b5989713ca4fe47a009f8621980b34e6d63ed3063b2a0a2c867d8a85", hash.digest().toHexString())
    }

    /**
     * The following tests from: https://csrc.nist.gov/CSRC/media/Projects/Cryptographic-Standards-and-Guidelines/documents/examples/SHA2_Additional.pdf
     */
    @Test
    fun test_nist1Byte() {
        val hash = SHA224()
        assertEquals("e33f9d75e6ae1369dbabf81b96b4591ae46bba30b591a6b6c62542b5", hash.digest(ByteArray(1) { 0xff.toByte() }).toHexString())
    }

    @Test
    fun test_nist4Bytes() {
        val hash = SHA224()
        assertEquals("fd19e74690d291467ce59f077df311638f1c3a46e510d0e49a67062d", hash.digest("e5e09924".toBinary()).toHexString())
    }

    @Test
    fun test_nist56BytesOfZero() {
        val hash = SHA224()
        assertEquals("5c3e25b69d0ea26f260cfae87e23759e1eca9d1ecc9fbf3c62266804", hash.digest(ByteArray(56) { 0 }).toHexString())
    }

    @Test
    fun test_nist1000Q() {
        val hash = SHA224()
        assertEquals("3706197f66890a41779dc8791670522e136fafa24874685715bd0a8a", hash.digest(ByteArray(1000) { 'Q'.code.toByte() }).toHexString())
    }

    @Test
    fun test_nist1000A() {
        val hash = SHA224()
        assertEquals("a8d0c66b5c6fdfd836eb3c6d04d32dfe66c3b1f168b488bf4c9c66ce", hash.digest(ByteArray(1000) { 'A'.code.toByte() }).toHexString())
    }

    @Test
    fun test_nist1005x99() {
        val hash = SHA224()
        assertEquals("cb00ecd03788bf6c0908401e0eb053ac61f35e7e20a2cfd7bd96d640", hash.digest(ByteArray(1005) { 0x99.toByte() }).toHexString())
    }

    @Test
    fun test_nist1million() {
        val hash = SHA224()
        assertEquals("3a5d74b68f14f3a4b2be9289b8d370672d0b3d2f53bc303c59032df3", hash.digest(ByteArray(1000000) { 0 }).toHexString())
    }

    @Test
    @Ignore // Java Heap Size
    fun test_nist536870912A() {
        val hash = SHA224()
        assertEquals("c4250083cf8230bf21065b3014baaaf9f76fecefc21f91cf237dedc9", hash.digest(ByteArray(0x20000000) { 'A'.code.toByte() }).toHexString())
    }

    @Test
    @Ignore // Java Heap Size
    fun test_nist1090519040x00() {
        val hash = SHA224()
        assertEquals("014674abc5cb980199935695af22fab683748f4261d4c6492b77c543", hash.digest(ByteArray(0x41000000) { 0 }).toHexString())
    }

    @Test
    @Ignore // Java Heap Size
    fun test_nist1090519040x84() {
        val hash = SHA224()
        assertEquals("a654b50b767a8323c5b519f467d8669837142881dc7ad368a7d5ef8f", hash.digest(ByteArray(0x6000003f) { 0x84.toByte() }).toHexString())
    }
}
