package io.iohk.atala.prism.apollo.securerandom

actual class SecureRandom actual constructor(
    actual val seed: ByteArray
) : SecureRandomInterface {
    private val jvmSecureRandom: java.security.SecureRandom = java.security.SecureRandom.getInstanceStrong()

    init {
        jvmSecureRandom.setSeed(seed)
    }

    override fun nextBytes(size: Int): ByteArray {
        val bytes = ByteArray(size)
        jvmSecureRandom.nextBytes(bytes)
        return bytes
    }

    actual companion object : SecureRandomStaticInterface {
        private val jvmSecureRandom: java.security.SecureRandom = java.security.SecureRandom.getInstanceStrong()

        override fun generateSeed(numBytes: Int): ByteArray {
            return jvmSecureRandom.generateSeed(numBytes)
        }
    }
}
