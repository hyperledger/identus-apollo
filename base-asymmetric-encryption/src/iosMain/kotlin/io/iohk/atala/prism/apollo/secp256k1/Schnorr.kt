package io.iohk.atala.prism.apollo.secp256k1

import kotlinx.cinterop.UByteVar
import kotlinx.cinterop.alloc
import kotlinx.cinterop.allocArray
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.readBytes

class Schnorr : Secp256k1() {
    /**
     * Create a Schnorr signature.
     *
     * @param data message to sign.
     * @param sec signer's private key.
     * @param auxrand32 32 bytes of fresh randomness (optional).
     */
    fun sign(data: ByteArray, sec: ByteArray, auxrand32: ByteArray?): ByteArray {
        require(sec.size == 32)
        require(data.size == 32)
        auxrand32?.let { require(it.size == 32) }
        memScoped {
            val nSec = toNat(sec)
            val nData = toNat(data)
            val nAuxrand32 = auxrand32?.let { toNat(it) }
            val nSig = allocArray<UByteVar>(64)
            val keypair = alloc<secp256k1_keypair>()
            secp256k1_keypair_create(ctx, keypair.ptr, nSec).requireSuccess("secp256k1_keypair_create() failed")
            secp256k1_schnorrsig_sign32(
                ctx,
                nSig,
                nData,
                keypair.ptr,
                nAuxrand32
            ).requireSuccess("secp256k1_ecdsa_sign() failed")
            return nSig.readBytes(64)
        }
    }

    /**
     * Verify a Schnorr signature.
     *
     * @param signature 64 bytes signature.
     * @param data message signed.
     * @param pub signer's x-only public key (32 bytes).
     */
    fun verify(signature: ByteArray, data: ByteArray, pub: ByteArray): Boolean {
        require(signature.size == 64)
        require(data.size == 32)
        require(pub.size == 32)
        memScoped {
            val nPub = toNat(pub)
            val pubkey = alloc<secp256k1_xonly_pubkey>()
            secp256k1_xonly_pubkey_parse(ctx, pubkey.ptr, nPub).requireSuccess("secp256k1_xonly_pubkey_parse() failed")
            val nData = toNat(data)
            val nSig = toNat(signature)
            return secp256k1_schnorrsig_verify(ctx, nSig, nData, 32, pubkey.ptr) == 1
        }
    }
}
