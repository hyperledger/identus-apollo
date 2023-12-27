package io.iohk.atala.prism.apollo.hashing

import io.iohk.atala.prism.apollo.hashing.external.pbkdf2
import io.iohk.atala.prism.apollo.hashing.external.sha512
import org.khronos.webgl.Uint8Array
import org.khronos.webgl.get

/**
 * The PBKDF2SHA512 class provides a platform-specific implementation for
 * deriving a key from a password using the PBKDF2 algorithm with SHA-512
 * hash function.
 */
actual object PBKDF2SHA512 {
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
        val opts = js("{}")
        opts.c = 2048
        opts.dkLen = 64
        val derived = pbkdf2(sha512, p, s, opts).buffer
        val uint8Array = Uint8Array(derived)
        val byteArray = ByteArray(uint8Array.length)
        for (i in 0 until uint8Array.length) {
            byteArray[i] = uint8Array[i].toByte()
        }
        return byteArray
    }
}
