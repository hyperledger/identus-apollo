package io.iohk.atala.prism.apollo.securerandom

import io.iohk.atala.prism.apollo.utils.toByteArray
import kotlinx.cinterop.ExperimentalForeignApi
import swift.secureRandomGeneration.IOHKSecureRandomGeneration

/**
 * The SecureRandom class provides a platform-specific implementation for generating secure random numbers.
 */
@OptIn(ExperimentalForeignApi::class)
actual class SecureRandom actual constructor(
    actual val seed: ByteArray
) : io.iohk.atala.prism.apollo.securerandom.SecureRandomInterface {

    /**
     * Generates a specified number of secure random bytes.
     *
     * @param size The number of random bytes to generate.
     * @return A byte array containing the generated random bytes.
     */
    override fun nextBytes(size: Int): ByteArray {
        return IOHKSecureRandomGeneration.randomDataWithLength(size.toLong()).toByteArray()
    }

    actual companion object : SecureRandomStaticInterface {
        /**
         * Generates a random seed of the specified length in bytes.
         *
         * @param numBytes The length of the seed in bytes.
         * @return The generated seed as a ByteArray.
         */
        override fun generateSeed(numBytes: Int): ByteArray {
            return IOHKSecureRandomGeneration.randomDataWithLength(numBytes.toLong()).toByteArray()
        }
    }
}
