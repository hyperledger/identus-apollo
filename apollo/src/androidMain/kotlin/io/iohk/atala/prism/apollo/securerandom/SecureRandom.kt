package io.iohk.atala.prism.apollo.securerandom

import android.os.Build

actual class SecureRandom actual constructor(
    actual val seed: ByteArray
) : SecureRandomInterface {
    private val jvmSecureRandom: java.security.SecureRandom
        get() {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                java.security.SecureRandom.getInstanceStrong()
            } else {
                java.security.SecureRandom(seed)
            }
        }

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            jvmSecureRandom.setSeed(seed)
        }
    }

    override fun nextBytes(size: Int): ByteArray {
        val bytes = ByteArray(size)
        jvmSecureRandom.nextBytes(bytes)
        return bytes
    }

    actual companion object : SecureRandomStaticInterface {
        private val jvmSecureRandom: java.security.SecureRandom
            get() {
                return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    java.security.SecureRandom.getInstanceStrong()
                } else {
                    java.security.SecureRandom()
                }
            }

        override fun generateSeed(numBytes: Int): ByteArray {
            return jvmSecureRandom.generateSeed(numBytes)
        }
    }
}
