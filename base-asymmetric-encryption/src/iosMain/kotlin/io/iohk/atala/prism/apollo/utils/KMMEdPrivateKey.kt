package io.iohk.atala.prism.apollo.utils

import swift.cryptoKit.Ed25519

public actual class KMMEdPrivateKey(val raw: ByteArray) {
    @Throws(RuntimeException::class)
    public constructor() : this(Ed25519.createPrivateKey().success()?.toByteArray() ?: throw RuntimeException("Null result"))

    @Throws(RuntimeException::class)
    actual fun sign(message: ByteArray): ByteArray {
        val result = Ed25519.signWithPrivateKey(raw.toNSData(), message.toNSData())
        result.failure()?.let { throw RuntimeException(it.localizedDescription()) }
        return result.success()?.toByteArray() ?: throw RuntimeException("Null result")
    }

    @Throws(RuntimeException::class)
    public fun publicKey(): KMMEdPublicKey {
        val result = Ed25519.publicKeyWithPrivateKey(raw.toNSData())
        result.failure()?.let { throw RuntimeException(it.localizedDescription()) }
        val publicRaw = result.success()?.toByteArray() ?: throw RuntimeException("Null result")
        return KMMEdPublicKey(publicRaw)
    }
}
