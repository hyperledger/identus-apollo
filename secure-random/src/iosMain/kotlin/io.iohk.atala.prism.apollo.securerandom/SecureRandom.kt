package io.iohk.atala.prism.apollo.securerandom

import cocoapods.IOHKSecureRandomGeneration.IOHKSecureRandomGeneration
import io.iohk.atala.prism.apollo.utils.toByteArray

actual class SecureRandom actual constructor(
    actual val seed: ByteArray
) : SecureRandomInterface {

    override fun nextBytes(size: Int): ByteArray {
        return IOHKSecureRandomGeneration.randomDataWithLength(size.toLong()).toByteArray()
    }

    actual companion object : SecureRandomStaticInterface {
        override fun generateSeed(numBytes: Int): ByteArray {
            return IOHKSecureRandomGeneration.randomDataWithLength(numBytes.toLong()).toByteArray()
        }
    }
}
