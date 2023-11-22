package io.iohk.atala.prism.apollo.secp256k1

import io.iohk.atala.prism.apollo.utils.toHex
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.DeferScope
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.MemScope
import kotlinx.cinterop.UByteVar
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.alloc
import kotlinx.cinterop.allocArray
import kotlinx.cinterop.convert
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.pin
import kotlinx.cinterop.ptr
import kotlinx.cinterop.readBytes
import secp256k1.SECP256K1_CONTEXT_SIGN
import secp256k1.SECP256K1_CONTEXT_VERIFY
import secp256k1.SECP256K1_FLAGS_BIT_CONTEXT_SIGN
import secp256k1.SECP256K1_FLAGS_BIT_CONTEXT_VERIFY
import secp256k1.SECP256K1_FLAGS_TYPE_CONTEXT
import secp256k1.secp256k1_context
import secp256k1.secp256k1_context_create
import secp256k1.secp256k1_context_destroy
import secp256k1.secp256k1_ec_privkey_tweak_mul
import secp256k1.secp256k1_ec_pubkey_parse
import secp256k1.secp256k1_ec_seckey_negate
import secp256k1.secp256k1_ec_seckey_tweak_add
import secp256k1.secp256k1_ec_seckey_verify
import secp256k1.secp256k1_ecdsa_signature
import secp256k1.secp256k1_ecdsa_signature_normalize
import secp256k1.secp256k1_ecdsa_signature_parse_compact
import secp256k1.secp256k1_ecdsa_signature_parse_der
import secp256k1.secp256k1_ecdsa_signature_serialize_compact
import secp256k1.secp256k1_ecdsa_verify
import secp256k1.secp256k1_pubkey

@OptIn(ExperimentalForeignApi::class)
open class Secp256k1 {
    val ctx: CPointer<secp256k1_context> by lazy {
        secp256k1_context_create((SECP256K1_FLAGS_TYPE_CONTEXT or SECP256K1_FLAGS_BIT_CONTEXT_SIGN or SECP256K1_FLAGS_BIT_CONTEXT_VERIFY).toUInt())
            ?: error("Could not create secp256k1 context")
    }

    /**
     * Convert an ECDSA signature to a normalized lower-S form (bitcoin standardness rule).
     * Returns the normalized signature and a boolean set to true if the input signature was not normalized.
     *
     * @param sig signature that should be normalized.
     */
    fun signatureNormalize(sig: ByteArray): Pair<ByteArray, Boolean> {
        require(sig.size >= 64) { "invalid signature ${Hex.encode(sig)}" }
        memScoped {
            val nSig = allocSignature(sig)
            val isHighS = secp256k1_ecdsa_signature_normalize(ctx, nSig.ptr, nSig.ptr)
            return Pair(serializeSignature(nSig), isHighS == 1)
        }
    }

    /**
     * Verify the validity of a private key.
     */
    fun secKeyVerify(privateKey: ByteArray): Boolean {
        if (privateKey.size != 32) return false
        memScoped {
            val nPrivkey = toNat(privateKey)
            return secp256k1_ec_seckey_verify(ctx, nPrivkey) == 1
        }
    }

    /**
     * Negate the given private key.
     */
    fun privateKeyNegate(privateKey: ByteArray): ByteArray {
        require(privateKey.size == 32)
        memScoped {
            val negated = privateKey.copyOf()
            val negPriv = toNat(negated)
            secp256k1_ec_seckey_negate(ctx, negPriv).requireSuccess("secp256k1_ec_seckey_negate() failed")
            return negated
        }
    }

    /**
     * Tweak a private key by adding tweak to it.
     */
    fun privateKeyTweakAdd(privateKey: ByteArray, tweak: ByteArray): ByteArray {
        require(privateKey.size == 32)
        memScoped {
            val added = privateKey.copyOf()
            val natAdd = toNat(added)
            val natTweak = toNat(tweak)
            secp256k1_ec_seckey_tweak_add(
                ctx,
                natAdd,
                natTweak
            ).requireSuccess("secp256k1_ec_seckey_tweak_add() failed")
            return added
        }
    }

    /**
     * Tweak a private key by multiplying it by a tweak.
     */
    fun privateKeyTweakMul(privateKey: ByteArray, tweak: ByteArray): ByteArray {
        require(privateKey.size == 32)
        memScoped {
            val multiplied = privateKey.copyOf()
            val natMul = toNat(multiplied)
            val natTweak = toNat(tweak)
            secp256k1_ec_privkey_tweak_mul(
                ctx,
                natMul,
                natTweak
            ).requireSuccess("secp256k1_ec_privkey_tweak_mul() failed")
            return multiplied
        }
    }

    /**
     * Serialize a public key to compact form (33 bytes).
     */
    fun publicKeyCompress(publicKey: ByteArray): ByteArray {
        return when {
            publicKey.size == 33 && (publicKey[0] == 2.toByte() || publicKey[0] == 3.toByte()) -> publicKey
            publicKey.size == 65 && publicKey[0] == 4.toByte() -> {
                val compressed = publicKey.copyOf(33)
                compressed[0] = if (publicKey.last() % 2 == 0) 2.toByte() else 3.toByte()
                compressed
            }
            else -> throw Secp256k1Exception("invalid public key")
        }
    }

    /**
     * Serialize a public key to compact form (33 bytes).
     */
    fun verify(publicKey: ByteArray, signature: ByteArray, data: ByteArray): Boolean {
        return memScoped {
            // Context
            val context = secp256k1_context_create((SECP256K1_CONTEXT_SIGN or SECP256K1_CONTEXT_VERIFY).convert())

            // Public Key Alloc
            val publicKeyPinned = publicKey.asUByteArray().pin()
            val natPub = publicKeyPinned.addressOf(0)
            val nPublicKey = alloc<secp256k1_pubkey>()

            if (secp256k1_ec_pubkey_parse(context, nPublicKey.ptr, natPub, publicKey.size.convert()) != 1) {
                throw Secp256k1Exception("secp256k1_ec_pubkey_parse() failed")
            }

            // Message
            val messagePinned = data.toUByteArray().pin()
            val nMessage = messagePinned.addressOf(0)

            // Signature
            val sig = alloc<secp256k1_ecdsa_signature>()
            val sigPinned = signature.toUByteArray().pin()
            val nativeBytes = sigPinned.addressOf(0)
            val result =
                when {
                    signature.size == 64 -> secp256k1_ecdsa_signature_parse_compact(context, sig.ptr, nativeBytes)
                    signature.size < 64 -> throw Secp256k1Exception("Unknown signature format")
                    else -> secp256k1_ecdsa_signature_parse_der(context, sig.ptr, nativeBytes, signature.size.convert())
                }
            if (result != 1) {
                throw Secp256k1Exception("cannot parse signature (size = ${signature.size} sig = ${signature.toHex()}")
            }

            this.defer {
                secp256k1_context_destroy(context)
                publicKeyPinned.unpin()
                messagePinned.unpin()
                sigPinned.unpin()
            }
            return secp256k1_ecdsa_verify(context, sig.ptr, nMessage, nPublicKey.ptr) == 1
        }
    }

    /**
     * Delete the secp256k1 context from dynamic memory.
     */
    private fun cleanup() {
        secp256k1_context_destroy(ctx)
    }

    /**
     * Kotlin destructor to destroy context automatically from dynamic memory.
     */
    protected fun finalize() {
        // finalization logic
        cleanup()
    }

    // Helper Methods

    protected fun Int.requireSuccess(message: String): Int = if (this != 1) throw Secp256k1Exception(message) else this

    protected fun MemScope.allocSignature(input: ByteArray): secp256k1_ecdsa_signature {
        val sig = alloc<secp256k1_ecdsa_signature>()
        val nativeBytes = toNat(input)

        val result =
            when {
                input.size == 64 -> secp256k1_ecdsa_signature_parse_compact(ctx, sig.ptr, nativeBytes)
                input.size < 64 -> throw Secp256k1Exception("Unknown signature format")
                else -> secp256k1_ecdsa_signature_parse_der(ctx, sig.ptr, nativeBytes, input.size.convert())
            }
        result.requireSuccess("cannot parse signature (size = ${input.size} sig = ${Hex.encode(input)}")
        return sig
    }

    protected fun MemScope.serializeSignature(signature: secp256k1_ecdsa_signature): ByteArray {
        val natOutput = allocArray<UByteVar>(64)
        secp256k1_ecdsa_signature_serialize_compact(
            ctx,
            natOutput,
            signature.ptr
        ).requireSuccess("secp256k1_ecdsa_signature_serialize_compact() failed")
        return natOutput.readBytes(64)
    }

    protected fun MemScope.allocPublicKey(pubkey: ByteArray): secp256k1_pubkey {
        val natPub = toNat(pubkey)
        val pub = alloc<secp256k1_pubkey>()

        secp256k1_ec_pubkey_parse(
            ctx,
            pub.ptr,
            natPub,
            pubkey.size.convert()
        ).requireSuccess("secp256k1_ec_pubkey_parse() failed")
        return pub
    }

    @OptIn(ExperimentalUnsignedTypes::class)
    protected fun DeferScope.toNat(bytes: ByteArray): CPointer<UByteVar> {
        val ubytes = bytes.asUByteArray()
        val pinned = ubytes.pin()
        this.defer { pinned.unpin() }
        return pinned.addressOf(0)
    }
}
