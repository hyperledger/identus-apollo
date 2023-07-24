package io.iohk.atala.prism.apollo.utils

import cocoapods.IOHKCryptoKit.Ed25519
import kotlinx.cinterop.ObjCObjectVar
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.value
import platform.Foundation.NSError

public actual class KMMEdPublicKey(val raw: ByteArray) {

    @Throws(RuntimeException::class)
    actual fun verify(message: ByteArray, sig: ByteArray): Boolean {
        memScoped {
            val errorRef = alloc<ObjCObjectVar<NSError?>>()
            val result = Ed25519.verifyWithPublicKey(raw.toNSData(), sig.toNSData(), message.toNSData(), errorRef.ptr)
            errorRef.value?.let { throw RuntimeException(it.localizedDescription()) }
            return result?.boolValue ?: throw RuntimeException("Null result")
        }
    }
}
