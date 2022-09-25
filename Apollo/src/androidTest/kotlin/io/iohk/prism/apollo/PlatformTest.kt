package io.iohk.prism.apollo

import org.junit.Test
import kotlin.test.assertTrue

class PlatformTest {

    @Test
    fun testPlatformName() {
        assertTrue(Platform.OS.startsWith("Android"), "Check Android is mentioned")
    }
}
