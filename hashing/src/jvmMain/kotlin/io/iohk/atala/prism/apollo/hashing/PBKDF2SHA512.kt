package io.iohk.atala.prism.apollo.hashing

import org.bitcoinj.crypto.PBKDF2SHA512

actual class PBKDF2SHA512 {
    actual companion object {
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
