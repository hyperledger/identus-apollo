package io.iohk.atala.prism.apollo.base16

import kotlin.test.Test
import kotlin.test.assertEquals

class Base16Tests {

    @Test
    fun testEncodeBase16() {
        assertEquals("57656C636F6D6520746F20494F4721".lowercase(), "Welcome to IOG!".base16Encoded)
    }

    @Test
    fun testDecodeBase16() {
        assertEquals("Welcome to IOG!", "57656C636F6D6520746F20494F4721".lowercase().base16Decoded)
    }

    @Test
    fun testEncodeBase16Upper() {
        assertEquals("57656C636F6D6520746F20494F4721", "Welcome to IOG!".base16UpperEncoded)
    }

    @Test
    fun testDecodeBase16Upper() {
        assertEquals("Welcome to IOG!", "57656C636F6D6520746F20494F4721".base16UpperDecoded)
    }

    @Test
    fun testEncodeBase16RFC_4648() {
        assertEquals("", "".base16Encoded)
    }

    @Test
    fun testEncodeBase16RFC_4648_1() {
        assertEquals("66", "f".base16Encoded)
    }

    @Test
    fun testEncodeBase16RFC_4648_2() {
        assertEquals("666F", "fo".base16Encoded.uppercase())
    }

    @Test
    fun testEncodeBase16RFC_4648_3() {
        assertEquals("666F6F", "foo".base16Encoded.uppercase())
    }

    @Test
    fun testEncodeBase16RFC_4648_4() {
        assertEquals("666F6F62", "foob".base16Encoded.uppercase())
    }

    @Test
    fun testEncodeBase16RFC_4648_5() {
        assertEquals("666F6F6261", "fooba".base16Encoded.uppercase())
    }

    @Test
    fun testEncodeBase16RFC_4648_6() {
        assertEquals("666F6F626172", "foobar".base16Encoded.uppercase())
    }
}
