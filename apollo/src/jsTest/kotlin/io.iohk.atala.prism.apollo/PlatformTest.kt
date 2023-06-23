package io.iohk.atala.prism.apollo

import kotlin.test.Test
import kotlin.test.assertTrue

class PlatformTest {

    @Test
    fun testPlatformName() {
        assertTrue(Platform.OS.contains("JS"), "Check JS is mentioned")
    }
}
