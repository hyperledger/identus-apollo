package io.iohk.atala.prism.apollo.utils

public expect class KMMEdPublicKey {
    fun verify(message: ByteArray, sig: ByteArray): Boolean
}
