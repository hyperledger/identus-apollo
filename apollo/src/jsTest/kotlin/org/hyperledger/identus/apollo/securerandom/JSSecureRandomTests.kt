package org.hyperledger.identus.apollo.securerandom

import kotlin.test.Test
import kotlin.test.fail

class JSSecureRandomTests {
    @Test
    fun test_SecureRandom_not_failing() {
        try {
            SecureRandom().nextBytes(16)
            SecureRandom.generateSeed(16)
        } catch (ex: Exception) {
            fail(ex.message)
        }
    }
}
