package io.iohk.atala.prism.apollo.utils

public expect class KMMEdPrivateKey {
    fun publicKey(): KMMEdPublicKey
    fun sign(message: ByteArray): ByteArray
}
