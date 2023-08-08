package io.iohk.atala.prism.apollo.securerandom

import io.iohk.atala.prism.apollo.utils.toByteArray
import swift.secureRandomGeneration.IOHKSecureRandomGeneration

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
