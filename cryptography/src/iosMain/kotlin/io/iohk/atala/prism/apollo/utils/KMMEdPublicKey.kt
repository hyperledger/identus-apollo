package io.iohk.atala.prism.apollo.utils

import kotlinx.cinterop.ExperimentalForeignApi
import swift.cryptoKit.Ed25519

@OptIn(ExperimentalForeignApi::class)
public actual class KMMEdPublicKey(val raw: ByteArray = ByteArray(0)) {
    @Throws(RuntimeException::class)
    actual fun verify(message: ByteArray, sig: ByteArray): Boolean {
        val result = Ed25519.verifyWithPublicKey(raw.toNSData(), sig.toNSData(), message.toNSData())
        result.failure()?.let { throw RuntimeException(it.localizedDescription()) }
        return result.success()?.boolValue ?: throw RuntimeException("Null result")
    }
}
