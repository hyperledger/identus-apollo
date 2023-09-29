package io.iohk.atala.prism.apollo.utils

public expect class KMMEdPrivateKey {
    fun sign(message: ByteArray): ByteArray
}
