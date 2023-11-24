package io.iohk.atala.prism.apollo.securerandom

import kotlin.test.Test
import kotlin.test.assertTrue

class SecureRandomTests {
    @Test
    fun testRandomSize() {
        val random = SecureRandom().nextBytes(5)
        assertTrue(random.size == 5)
    }
}
