package io.iohk.atala.prism.apollo.base32

import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

class Base32Tests {
    @Test
    fun testEncodeBase32() {
        assertEquals("K5SWYY3PNVSSA5DPEBEU6RZB".lowercase(), "Welcome to IOG!".base32Encoded)
    }

    @Test
    fun testDecodeBase32() {
        assertEquals("Welcome to IOG!", "K5SWYY3PNVSSA5DPEBEU6RZB".lowercase().base32Decoded)
    }

    @Test
    fun testEncodeBase32WithPadding() {
        assertEquals("K5SWYY3PNVSSA5DPEBEU6RZB".lowercase(), "Welcome to IOG!".base32PadEncoded)
    }

    @Test
    fun testDecodeBase32WithPadding() {
        assertEquals("Welcome to IOG!", "K5SWYY3PNVSSA5DPEBEU6RZB".lowercase().base32PadDecoded)
    }

    @Test
    fun testEncodeBase32Upper() {
        assertEquals("K5SWYY3PNVSSA5DPEBEU6RZB", "Welcome to IOG!".base32UpperEncoded)
    }

    @Test
    fun testDecodeBase32Upper() {
        assertEquals("Welcome to IOG!", "K5SWYY3PNVSSA5DPEBEU6RZB".base32UpperDecoded)
    }

    @Test
    fun testEncodeBase32UpperWithPadding() {
        "".encodeToByteArray()
        assertEquals("K5SWYY3PNVSSA5DPEBEU6RZB", "Welcome to IOG!".base32UpperPadEncoded)
    }

    @Test
    fun testDecodeBase32UpperWithPadding() {
        assertEquals("Welcome to IOG!", "K5SWYY3PNVSSA5DPEBEU6RZB".base32UpperPadDecoded)
    }

    @Test
    fun testDecodeBase32HexUpper() {
        // Hex value of "Welcome to IOG!" in hex
        val expected = byteArrayOf(1, 17, -107, -115, -107, -71, -47, -55, -123, -79, -91, -23, -108, -127, -107, -39, -107, -55, -27, -47, -95, -91, -71, -100, -124, -124, -124)
        assertContentEquals(expected, "8him6pbeehp62r39f9ii0pbmclp7it38d5n6e89144".uppercase().base32HexUpperDecodedBytes)
    }

    @Test
    fun testDecodeBase32HexUpperWithPadding() {
        // Hex value of "Welcome to IOG!" in hex
        val expected = byteArrayOf(1, 17, -107, -115, -107, -71, -47, -55, -123, -79, -91, -23, -108, -127, -107, -39, -107, -55, -27, -47, -95, -91, -71, -100, -124, -124, -124)
        assertContentEquals(expected, "8him6pbeehp62r39f9ii0pbmclp7it38d5n6e89144".uppercase().base32HexUpperPadDecodedBytes)
    }

    @Test
    fun testEncodeBase32_RFC_4648_1() {
        assertEquals("foo".base32UpperPadEncoded, "MZXW6===")
    }

    @Test
    fun testEncodeBase32_RFC_4648_2() {
        assertEquals("foob".base32UpperPadEncoded, "MZXW6YQ=")
    }

    @Test
    fun testEncodeBase32_RFC_4648_3() {
        assertEquals("fooba".base32UpperPadEncoded, "MZXW6YTB")
    }

    @Test
    fun testEncodeBase32_RFC_4648_4() {
        assertEquals("foobar".base32UpperPadEncoded, "MZXW6YTBOI======")
    }

    @Test
    fun testEncodeBase32_RFC_4648_5() {
        assertEquals("".base32Encoded, "")
    }

    @Test
    fun testEncodeBase32_RFC_4648_6() {
        assertEquals("f".base32HexUpperPadEncoded, "CO======")
    }

    @Test
    fun testEncodeBase32_RFC_4648_7() {
        assertEquals("fo".base32HexUpperPadEncoded, "CPNG====")
    }

    @Test
    fun testEncodeBase32_RFC_4648_8() {
        assertEquals("foo".base32HexUpperPadEncoded, "CPNMU===")
    }

    @Test
    fun testEncodeBase32_RFC_4648_9() {
        assertEquals("foob".base32HexUpperPadEncoded, "CPNMUOG=")
    }

    @Test
    fun testEncodeBase32_RFC_4648_10() {
        assertEquals("fooba".base32HexUpperPadEncoded, "CPNMUOJ1")
    }

    @Test
    fun testEncodeBase32_RFC_4648_11() {
        assertEquals("foobar".base32HexUpperPadEncoded, "CPNMUOJ1E8======")
    }
}
