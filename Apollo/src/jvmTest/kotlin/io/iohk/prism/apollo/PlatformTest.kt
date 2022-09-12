package io.iohk.prism.apollo

import org.junit.Test
import kotlin.test.assertTrue

class PlatformTest {

    @Test
    fun testPlatformName() {
        println(Platform.OS)
        assertTrue(Platform.OS.contains("JVM"), "Check JVM is mentioned")
    }
}