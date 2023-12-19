package io.iohk.atala.prism.apollo.securerandom

import io.iohk.atala.prism.apollo.utils._require
import io.iohk.atala.prism.apollo.utils.global
import io.iohk.atala.prism.apollo.utils.isNode
import js.typedarrays.Uint8Array

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
        return if (isNode) {
            _require("crypto").getRandomValues(arr)
        } else {
            global.crypto.getRandomValues(arr)
        }
    }

    actual companion object : SecureRandomStaticInterface {
        override fun generateSeed(numBytes: Int): ByteArray {
            val arr = Uint8Array(numBytes)
            return if (isNode) {
                _require("crypto").getRandomValues(arr)
            } else {
                global.crypto.getRandomValues(arr)
            }
        }
    }
}
