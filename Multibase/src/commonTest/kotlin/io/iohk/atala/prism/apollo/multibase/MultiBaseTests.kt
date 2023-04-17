package io.iohk.atala.prism.apollo.multibase

import kotlin.test.Test
import kotlin.test.assertEquals

// TODO: add the rest of unit test
class MultiBaseTests {
    @Test
    fun testMultibaseBase16() {
        assertEquals("f57656c636f6d6520746f20494f4721", MultiBase.encode(MultiBase.Base.BASE16, "Welcome to IOG!"))
        assertEquals("Welcome to IOG!", MultiBase.decode("f57656c636f6d6520746f20494f4721").decodeToString())

        assertEquals("F57656C636F6D6520746F20494F4721", MultiBase.encode(MultiBase.Base.BASE16_UPPER, "Welcome to IOG!"))
        assertEquals("Welcome to IOG!", MultiBase.decode("F57656C636F6D6520746F20494F4721").decodeToString())
    }
}
