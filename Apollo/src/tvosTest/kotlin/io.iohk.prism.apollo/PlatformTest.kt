package io.iohk.prism.apollo

import kotlin.test.Test
import kotlin.test.assertTrue

class PlatformTest {

    @Test
    fun testPlatformName() {
        println(Platform.OS)
        assertTrue(Platform.OS.lowercase().contains("tvos"), "Check tvOS is mentioned")
    }
}
