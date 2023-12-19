package io.iohk.atala.prism.apollo.hashing

import org.bitcoinj.crypto.PBKDF2SHA512

/**
 * The PBKDF2SHA512 class provides a platform-specific implementation for
 * deriving a key from a password using the PBKDF2 algorithm with SHA-512
 * hash function.
 */
actual class PBKDF2SHA512 {
    actual companion object {
        /**
         * Derives a key from a password using the PBKDF2 algorithm with SHA-512 hash function.
         *
         * @param p The password used for key derivation.
         * @param s The salt value.
         * @param c The iteration count.
         * @param dkLen The desired length of the derived key in bytes.
         * @return The derived key as a ByteArray.
         */
        actual fun derive(
            p: String,
            s: String,
            c: Int,
            dkLen: Int
        ): ByteArray {
            return PBKDF2SHA512.derive(p, s, c, dkLen)
        }
    }
}
