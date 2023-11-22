package io.iohk.atala.prism.apollo.secp256k1

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.UByteVar
import kotlinx.cinterop.allocArray
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.readBytes
import secp256k1.secp256k1_ecdh

class ECDH : Secp256k1() {
    /**
     * Compute an elliptic curve Diffie-Hellman secret.
     */
    @OptIn(ExperimentalForeignApi::class)
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
