package io.iohk.atala.prism.apollo.ed25519

import io.iohk.atala.prism.apollo.utils.KMMEdPrivateKey
import io.iohk.atala.prism.apollo.utils.KMMEdPublicKey

/**
 * Ed25519 is a variation of EdDSA
 */
object Ed25519 {
    fun sign(data: ByteArray, privateKey: KMMEdPrivateKey): ByteArray {
        throw NotImplementedError("Will be implemented in the future")
    }

    fun verify(data: ByteArray, publicKey: KMMEdPublicKey, signature: ByteArray): Boolean {
        throw NotImplementedError("Will be implemented in the future")
    }
}
