package io.iohk.atala.prism.apollo.utils

import io.iohk.atala.prism.apollo.securerandom.SecureRandom

interface IVGeneration {
    /**
     * Generate random data with specified size
     *
     * @param size the size of the random generated data
     */
    fun createRandomIV(size: Int): ByteArray = SecureRandom().nextBytes(size)
}
