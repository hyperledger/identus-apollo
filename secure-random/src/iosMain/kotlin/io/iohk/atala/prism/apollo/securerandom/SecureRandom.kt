package io.iohk.atala.prism.apollo.securerandom

import io.iohk.atala.prism.apollo.utils.toByteArray
import kotlinx.cinterop.ExperimentalForeignApi
import swift.secureRandomGeneration.IOHKSecureRandomGeneration

actual class SecureRandom actual constructor(
    actual val seed: ByteArray
) : SecureRandomInterface {

    @OptIn(ExperimentalForeignApi::class)
    override fun nextBytes(size: Int): ByteArray {
        return IOHKSecureRandomGeneration.randomDataWithLength(size.toLong()).toByteArray()
    }

    actual companion object : SecureRandomStaticInterface {
        @OptIn(ExperimentalForeignApi::class)
        override fun generateSeed(numBytes: Int): ByteArray {
            return IOHKSecureRandomGeneration.randomDataWithLength(numBytes.toLong()).toByteArray()
        }
    }
}
