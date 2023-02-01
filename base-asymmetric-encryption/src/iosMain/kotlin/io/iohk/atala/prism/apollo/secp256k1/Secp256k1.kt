package io.iohk.atala.prism.apollo.secp256k1

/* ktlint-disable */
import kotlin.random.Random
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.DeferScope
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
import kotlinx.cinterop.toCValues
import kotlinx.cinterop.value
import platform.posix.size_tVar
// import io.iohk.atala.prism.apollo.kmmsecp256k1.*
import secp256k1.*
/* ktlint-disable */

open class Secp256k1 {

    val ctx: CPointer<secp256k1_context> by lazy {
        secp256k1_context_create((SECP256K1_FLAGS_TYPE_CONTEXT or SECP256K1_FLAGS_BIT_CONTEXT_SIGN or SECP256K1_FLAGS_BIT_CONTEXT_VERIFY).toUInt())
            ?: error("Could not create secp256k1 context")
    }

    /**
     * Generate Secp256k1 KeyPair
     *
     * @return pair where first is PrivateKey and second is PublicKey
     */
    fun generateKeyPair(): Pair<ByteArray, ByteArray> {
        fun randomBytes(length: Int): ByteArray {
            val buffer = ByteArray(length)
            Random.Default.nextBytes(buffer)
            return buffer
        }
        val privateKey = randomBytes(32)
        val publicKey = publicKeyCreate(privateKey)
        return Pair(privateKey, publicKey)
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
     * Get the public key corresponding to the given private key.
     * Returns the uncompressed public key (65 bytes).
     */
    fun publicKeyCreate(privateKey: ByteArray): ByteArray {
        require(privateKey.size == 32)
        memScoped {
            val nPrivkey = toNat(privateKey)
            val nPubkey = alloc<secp256k1_pubkey>()
            secp256k1_ec_pubkey_create(ctx, nPubkey.ptr, nPrivkey).requireSuccess("secp256k1_ec_pubkey_create() failed")
            return serializePubkey(nPubkey)
        }
    }

    /**
     * Parse a serialized public key.
     * Returns the uncompressed public key (65 bytes).
     */
    fun publicKeyParse(publicKey: ByteArray): ByteArray {
        require(publicKey.size == 33 || publicKey.size == 65)
        memScoped {
            val nPubkey = allocPublicKey(publicKey)
            return serializePubkey(nPubkey)
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
     * Negate the given public key.
     * Returns the uncompressed public key (65 bytes).
     */
    fun publicKeyNegate(publicKey: ByteArray): ByteArray {
        require(publicKey.size == 33 || publicKey.size == 65)
        memScoped {
            val nPubkey = allocPublicKey(publicKey)
            secp256k1_ec_pubkey_negate(ctx, nPubkey.ptr).requireSuccess("secp256k1_ec_pubkey_negate() failed")
            return serializePubkey(nPubkey)
        }
    }

    /**
     * Tweak a public key by adding tweak times the generator to it.
     * Returns the uncompressed public key (65 bytes).
     */
    fun publicKeyTweakAdd(publicKey: ByteArray, tweak: ByteArray): ByteArray {
        require(publicKey.size == 33 || publicKey.size == 65)
        memScoped {
            val nPubkey = allocPublicKey(publicKey)
            val nTweak = toNat(tweak)
            secp256k1_ec_pubkey_tweak_add(
                ctx,
                nPubkey.ptr,
                nTweak
            ).requireSuccess("secp256k1_ec_pubkey_tweak_add() failed")
            return serializePubkey(nPubkey)
        }
    }

    /**
     * Tweak a public key by multiplying it by a tweak value.
     * Returns the uncompressed public key (65 bytes).
     */
    fun publicKeyTweakMul(publicKey: ByteArray, tweak: ByteArray): ByteArray {
        require(publicKey.size == 33 || publicKey.size == 65)
        memScoped {
            val nPubkey = allocPublicKey(publicKey)
            val nTweak = toNat(tweak)
            secp256k1_ec_pubkey_tweak_mul(
                ctx,
                nPubkey.ptr,
                nTweak
            ).requireSuccess("secp256k1_ec_pubkey_tweak_mul() failed")
            return serializePubkey(nPubkey)
        }
    }

    /**
     * Add a number of public keys together.
     * Returns the uncompressed public key (65 bytes).
     */
    fun publicKeyCombine(publicKeys: Array<ByteArray>): ByteArray {
        publicKeys.forEach { require(it.size == 33 || it.size == 65) }
        memScoped {
            val nPubkeys = publicKeys.map { allocPublicKey(it).ptr }
            val combined = alloc<secp256k1_pubkey>()
            secp256k1_ec_pubkey_combine(ctx, combined.ptr, nPubkeys.toCValues(), publicKeys.size.convert()).requireSuccess(
                "secp256k1_ec_pubkey_combine() failed"
            )
            return serializePubkey(combined)
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

        val result = when {
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

    protected fun MemScope.serializePubkey(pubkey: secp256k1_pubkey): ByteArray {
        val serialized = allocArray<UByteVar>(65)
        val outputLen = alloc<size_tVar>()
        outputLen.value = 65.convert()
        secp256k1_ec_pubkey_serialize(
            ctx,
            serialized,
            outputLen.ptr,
            pubkey.ptr,
            SECP256K1_EC_UNCOMPRESSED.convert()
        ).requireSuccess("secp256k1_ec_pubkey_serialize() failed")
        return serialized.readBytes(outputLen.value.convert())
    }

    @OptIn(ExperimentalUnsignedTypes::class)
    protected fun DeferScope.toNat(bytes: ByteArray): CPointer<UByteVar> {
        val ubytes = bytes.asUByteArray()
        val pinned = ubytes.pin()
        this.defer { pinned.unpin() }
        return pinned.addressOf(0)
    }
}
