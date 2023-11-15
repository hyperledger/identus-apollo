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
        assertEquals("MZXW6===", "foo".base32UpperPadEncoded)
        assertEquals("foo", "MZXW6===".base32UpperPadDecoded)
    }

    @Test
    fun testEncodeBase32_RFC_4648_2() {
        assertEquals("MZXW6YQ=", "foob".base32UpperPadEncoded)
        assertEquals("foob", "MZXW6YQ=".base32UpperPadDecoded)
    }

    @Test
    fun testEncodeBase32_RFC_4648_3() {
        assertEquals("MZXW6YTB", "fooba".base32UpperPadEncoded)
        assertEquals("fooba", "MZXW6YTB".base32UpperPadDecoded)
    }

    @Test
    fun testEncodeBase32_RFC_4648_4() {
        assertEquals("MZXW6YTBOI======", "foobar".base32UpperPadEncoded)
        assertEquals("foobar", "MZXW6YTBOI======".base32UpperPadDecoded)
    }

    @Test
    fun testEncodeBase32_RFC_4648_5() {
        assertEquals("", "".base32Encoded)
    }

    @Test
    fun testEncodeBase32_RFC_4648_6() {
        assertEquals("CO======", "f".base32HexUpperPadEncoded)
        assertEquals("f", "CO======".base32HexUpperPadDecoded)
    }

    @Test
    fun testEncodeBase32_RFC_4648_7() {
        assertEquals("CPNG====", "fo".base32HexUpperPadEncoded)
        assertEquals("fo", "CPNG====".base32HexUpperPadDecoded)
    }

    @Test
    fun testEncodeBase32_RFC_4648_8() {
        assertEquals("CPNMU===", "foo".base32HexUpperPadEncoded)
        assertEquals("foo", "CPNMU===".base32HexUpperPadDecoded)
    }

    @Test
    fun testEncodeBase32_RFC_4648_9() {
        assertEquals("CPNMUOG=", "foob".base32HexUpperPadEncoded)
        assertEquals("foob", "CPNMUOG=".base32HexUpperPadDecoded)
    }

    @Test
    fun testEncodeBase32_RFC_4648_10() {
        assertEquals("CPNMUOJ1", "fooba".base32HexUpperPadEncoded)
        assertEquals("fooba", "CPNMUOJ1".base32HexUpperPadDecoded)
    }

    @Test
    fun testEncodeBase32_RFC_4648_11() {
        assertEquals("CPNMUOJ1E8======", "foobar".base32HexUpperPadEncoded)
        assertEquals("foobar", "CPNMUOJ1E8======".base32HexUpperPadDecoded)
    }
}
