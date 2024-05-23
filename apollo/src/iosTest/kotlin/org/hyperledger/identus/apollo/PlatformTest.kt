package io.iohk.atala.prism.apollo

import kotlin.test.Test
import kotlin.test.assertTrue

class PlatformTest {

    @Test
    fun testPlatformName() {
        assertTrue(io.iohk.atala.prism.apollo.Platform.OS.contains("iOS"), "Check iOS is mentioned")
    }
}
