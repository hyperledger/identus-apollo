package io.iohk.atala.prism.apollo.secp256k1

import fr.acinq.secp256k1.Secp256k1Native

class ECDH {
    /**
     * Compute an elliptic curve Diffie-Hellman secret.
     */
    fun ecdh(privateKey: ByteArray, publicKey: ByteArray): ByteArray {
        require(privateKey.size == 32)
        require(publicKey.size == 33 || publicKey.size == 65)
        return Secp256k1Native.ecdh(privateKey, publicKey)
    }
}
