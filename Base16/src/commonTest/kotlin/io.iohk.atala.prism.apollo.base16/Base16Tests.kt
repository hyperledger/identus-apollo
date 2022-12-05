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
}
