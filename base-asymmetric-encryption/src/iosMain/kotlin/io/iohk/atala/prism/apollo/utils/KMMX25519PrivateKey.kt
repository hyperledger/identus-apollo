package io.iohk.atala.prism.apollo.utils

import cocoapods.IOHKCryptoKit.X25519
import kotlinx.cinterop.ObjCObjectVar
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.value
import platform.Foundation.NSError

actual class KMMX25519PrivateKey {
    public val raw: ByteArray

    constructor(raw: ByteArray) {
        this.raw = raw
    }

    constructor() {
        this.raw = X25519.createPrivateKey().toByteArray()
    }

    @Throws(RuntimeException::class)
    public fun publicKey(): KMMX25519PublicKey {
        memScoped {
            val errorRef = alloc<ObjCObjectVar<NSError?>>()
            val result = X25519.publicKeyWithPrivateKey(raw.toNSData(), errorRef.ptr)
            errorRef.value?.let { throw RuntimeException(it.localizedDescription()) }
            val publicRaw = result?.toByteArray() ?: throw RuntimeException("Null result")
            return KMMX25519PublicKey(publicRaw)
        }
    }
}
