package io.iohk.atala.prism.apollo.securerandom

import io.iohk.atala.prism.apollo.utils.toByteArray
import js.typedarrays.Uint8Array
import web.crypto.crypto

/**
 * The SecureRandom class provides a platform-specific implementation for generating secure random numbers.
 */
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
        val arr = Uint8Array(size)
        return crypto.getRandomValues(arr).buffer.toByteArray()
    }

    actual companion object : SecureRandomStaticInterface {
        override fun generateSeed(numBytes: Int): ByteArray {
            val arr = Uint8Array(numBytes)
            return crypto.getRandomValues(arr).buffer.toByteArray()
        }
    }
}
