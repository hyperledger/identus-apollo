package io.iohk.atala.prism.apollo.utils

import java.security.PublicKey

actual class KMMEdPublicKey(val nativeValue: PublicKey) {
    actual fun verify(message: ByteArray, sig: ByteArray): Boolean {
        TODO("Not yet implemented")
    }
}
