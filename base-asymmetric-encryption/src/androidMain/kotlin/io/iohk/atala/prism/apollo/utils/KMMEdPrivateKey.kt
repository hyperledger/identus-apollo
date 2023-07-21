package io.iohk.atala.prism.apollo.utils

import java.security.PrivateKey

actual class KMMEdPrivateKey(val nativeValue: PrivateKey) {
    actual fun sign(message: ByteArray): ByteArray {
        throw NotImplementedError("Not implemented")
    }
}
