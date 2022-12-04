package io.iohk.atala.prism.apollo

import kotlin.test.Test
import kotlin.test.assertTrue

class PlatformTest {

    @Test
    fun testPlatformName() {
        assertTrue(Platform.OS.lowercase().contains("mac"), "Check macOS is mentioned")
    }
}
