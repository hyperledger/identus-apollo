package io.iohk.prism.apollo

import kotlin.test.Test
import kotlin.test.assertTrue

class PlatformTest {

    @Test
    fun testPlatformName() {
        assertTrue(Platform.OS.contains("iOS"), "Check iOS is mentioned")
    }
}
