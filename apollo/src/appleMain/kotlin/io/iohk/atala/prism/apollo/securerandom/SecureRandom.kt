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
) : SecureRandomInterface {

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
        override fun generateSeed(numBytes: Int): ByteArray {
            return IOHKSecureRandomGeneration.randomDataWithLength(numBytes.toLong()).toByteArray()
        }
    }
}
