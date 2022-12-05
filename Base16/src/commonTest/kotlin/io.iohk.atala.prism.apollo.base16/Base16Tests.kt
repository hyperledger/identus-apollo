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
}
