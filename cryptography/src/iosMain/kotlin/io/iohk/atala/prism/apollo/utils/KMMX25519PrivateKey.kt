package io.iohk.atala.prism.apollo.utils

import swift.cryptoKit.X25519

actual class KMMX25519PrivateKey(val raw: ByteArray) {
    @Throws(RuntimeException::class)
    public constructor() : this(X25519.createPrivateKey().success()?.toByteArray() ?: throw RuntimeException("Null result"))

    @Throws(RuntimeException::class)
    public fun publicKey(): KMMX25519PublicKey {
        val result = X25519.publicKeyWithPrivateKey(raw.toNSData())
        result.failure()?.let { throw RuntimeException(it.localizedDescription()) }
        val publicRaw = result.success()?.toByteArray() ?: throw RuntimeException("Null result")
        return KMMX25519PublicKey(publicRaw)
    }
}
