package io.iohk.atala.prism.apollo.utils

import cocoapods.IOHKCryptoKit.Ed25519
import kotlinx.cinterop.ObjCObjectVar
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.value
import platform.Foundation.NSError

public actual class KMMEdPrivateKey(val raw: ByteArray = Ed25519.createPrivateKey().toByteArray()) {

    @Throws(RuntimeException::class)
    public fun sign(data: ByteArray): ByteArray {
        memScoped {
            val errorRef = alloc<ObjCObjectVar<NSError?>>()
            val result = Ed25519.signWithPrivateKey(raw.toNSData(), data.toNSData(), errorRef.ptr)
            errorRef.value?.let { throw RuntimeException(it.localizedDescription()) }
            return result?.toByteArray() ?: throw RuntimeException("Null result")
        }
    }

    @Throws(RuntimeException::class)
    public fun publicKey(): KMMEdPublicKey {
        memScoped {
            val errorRef = alloc<ObjCObjectVar<NSError?>>()
            val result = Ed25519.publicKeyWithPrivateKey(raw.toNSData(), errorRef.ptr)
            errorRef.value?.let { throw RuntimeException(it.localizedDescription()) }
            val publicRaw = result?.toByteArray() ?: throw RuntimeException("Null result")
            return KMMEdPublicKey(publicRaw)
        }
    }
}
