package io.iohk.atala.prism.apollo.hashing

import io.iohk.atala.prism.apollo.hashing.external.pbkdf2
import io.iohk.atala.prism.apollo.hashing.external.sha512
import org.khronos.webgl.Uint8Array
import org.khronos.webgl.get

actual class PBKDF2SHA512 {
    actual companion object {
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
}
