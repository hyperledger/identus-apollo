package io.iohk.atala.prism.apollo

import kotlin.test.Test
import kotlin.test.assertTrue

class PlatformTest {

    @Test
    fun testPlatformName() {
        println(Platform.OS)
        assertTrue(Platform.OS.lowercase().contains("watchos"), "Check watchOS is mentioned")
    }
}
