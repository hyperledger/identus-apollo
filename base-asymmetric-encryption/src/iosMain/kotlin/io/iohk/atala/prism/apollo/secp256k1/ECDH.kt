package io.iohk.atala.prism.apollo.secp256k1

import kotlinx.cinterop.UByteVar
import kotlinx.cinterop.allocArray
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.readBytes

class ECDH : Secp256k1() {
    /**
     * Compute an elliptic curve Diffie-Hellman secret.
     */
    fun ecdh(privateKey: ByteArray, publicKey: ByteArray): ByteArray {
        require(privateKey.size == 32)
        require(publicKey.size == 33 || publicKey.size == 65)
        memScoped {
            val nPubkey = allocPublicKey(publicKey)
            val nPrivkey = toNat(privateKey)
            val output = allocArray<UByteVar>(32)
            secp256k1_ecdh(ctx, output, nPubkey.ptr, nPrivkey, null, null).requireSuccess("secp256k1_ecdh() failed")
            return output.readBytes(32)
        }
    }
}
