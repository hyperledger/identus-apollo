package io.iohk.atala.prism.apollo.secp256k1

import kotlinx.cinterop.UByteVar
import kotlinx.cinterop.alloc
import kotlinx.cinterop.allocArray
import kotlinx.cinterop.convert
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.readBytes
import kotlinx.cinterop.value
import platform.posix.size_tVar
/* ktlint-disable */
// import io.iohk.atala.prism.apollo.kmmsecp256k1.*
import secp256k1.*
/* ktlint-disable */

class ECDSA : Secp256k1() {
    /**
     * Create a normalized ECDSA signature.
     *
     * @param message message to sign.
     * @param privateKey signer's private key.
     */
    fun sign(message: ByteArray, privateKey: ByteArray): ByteArray {
        require(privateKey.size == 32)
        require(message.size == 32)
        memScoped {
            val nPrivateKey = toNat(privateKey)
            val nMessage = toNat(message)
            val nSig = alloc<secp256k1_ecdsa_signature>()
            secp256k1_ecdsa_sign(
                ctx,
                nSig.ptr,
                nMessage,
                nPrivateKey,
                null,
                null
            ).requireSuccess("secp256k1_ecdsa_sign() failed")
            return serializeSignature(nSig)
        }
    }

    /**
     * Verify an ECDSA signature.
     *
     * @param signature signature using either compact encoding (64 bytes) or der-encoding.
     * @param message message signed.
     * @param publicKey signer's public key.
     */
    fun verify(signature: ByteArray, message: ByteArray, publicKey: ByteArray): Boolean {
        require(message.size == 32)
        require(publicKey.size == 33 || publicKey.size == 65)
        memScoped {
            val nPublicKey = allocPublicKey(publicKey)
            val nMessage = toNat(message)
            val nSig = allocSignature(signature)
            return secp256k1_ecdsa_verify(ctx, nSig.ptr, nMessage, nPublicKey.ptr) == 1
        }
    }

    /**
     * Recover a public key from an ECDSA signature.
     *
     * @param sig ecdsa compact signature (64 bytes).
     * @param message message signed.
     * @param recid recoveryId (should have been provided with the signature to allow recovery).
     */
    fun ecdsaRecover(sig: ByteArray, message: ByteArray, recid: Int): ByteArray {
        require(sig.size == 64)
        require(message.size == 32)
        memScoped {
            val nSig = toNat(sig)
            val rSig = alloc<secp256k1_ecdsa_recoverable_signature>()
            secp256k1_ecdsa_recoverable_signature_parse_compact(
                ctx,
                rSig.ptr,
                nSig,
                recid
            ).requireSuccess("secp256k1_ecdsa_recoverable_signature_parse_compact() failed")
            val nMessage = toNat(message)
            val pubkey = alloc<secp256k1_pubkey>()
            secp256k1_ecdsa_recover(
                ctx,
                pubkey.ptr,
                rSig.ptr,
                nMessage
            ).requireSuccess("secp256k1_ecdsa_recover() failed")
            return serializePubkey(pubkey)
        }
    }

    /**
     * Convert a compact ECDSA signature (64 bytes) to a der-encoded ECDSA signature.
     */
    fun compact2der(sig: ByteArray): ByteArray {
        require(sig.size == 64)
        memScoped {
            val nSig = allocSignature(sig)
            val natOutput = allocArray<UByteVar>(73)
            val len = alloc<size_tVar>()
            len.value = 73.convert()
            secp256k1_ecdsa_signature_serialize_der(
                ctx,
                natOutput,
                len.ptr,
                nSig.ptr
            ).requireSuccess("secp256k1_ecdsa_signature_serialize_der() failed")
            return natOutput.readBytes(len.value.toInt())
        }
    }
}
