package io.iohk.atala.prism.apollo.ecdsa

/* ktlint-disable */
import io.iohk.atala.prism.apollo.hashing.SHA256
import io.iohk.atala.prism.apollo.hashing.SHA384
import io.iohk.atala.prism.apollo.hashing.SHA512
import io.iohk.atala.prism.apollo.secp256k1.Secp256k1Exception
import io.iohk.atala.prism.apollo.utils.KMMECPrivateKey
import io.iohk.atala.prism.apollo.utils.KMMECPublicKey
import io.iohk.atala.prism.apollo.utils.toHex
import kotlinx.cinterop.UByteVar
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.alloc
import kotlinx.cinterop.allocArray
import kotlinx.cinterop.convert
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.pin
import kotlinx.cinterop.ptr
import kotlinx.cinterop.readBytes
import secp256k1.*
/* ktlint-disable */

actual object KMMECDSA {
    @OptIn(ExperimentalUnsignedTypes::class)
    actual fun sign(
        type: ECDSAType,
        data: ByteArray,
        privateKey: KMMECPrivateKey
    ): ByteArray {
        val hashedData = when (type) {
            ECDSAType.ECDSA_SHA256 -> SHA256().digest(data)
            ECDSAType.ECDSA_SHA384 -> SHA384().digest(data)
            ECDSAType.ECDSA_SHA512 -> SHA512().digest(data)
        }
        memScoped {
            // Context
            val context = secp256k1_context_create((SECP256K1_CONTEXT_SIGN or SECP256K1_CONTEXT_VERIFY).convert())

            val privateKeyPinned = privateKey.nativeValue.pin()
            val nPrivateKey = privateKeyPinned.addressOf(0)

            val messagePinned = hashedData.toUByteArray().pin()
            val nMessage = messagePinned.addressOf(0)

            val nSig = alloc<secp256k1_ecdsa_signature>()
            if (secp256k1_ecdsa_sign(context, nSig.ptr, nMessage, nPrivateKey, null, null) != 1) {
                throw Secp256k1Exception("secp256k1_ecdsa_sign() failed")
            }
            val natOutput = allocArray<UByteVar>(64)

            if(secp256k1_ecdsa_signature_serialize_compact(context, natOutput, nSig.ptr) != 1) {
                throw Secp256k1Exception("secp256k1_ecdsa_signature_serialize_compact() failed")
            }

            this.defer {
                secp256k1_context_destroy(context)
                privateKeyPinned.unpin()
                messagePinned.unpin()
            }
            return natOutput.readBytes(64)
        }
    }

    @OptIn(ExperimentalUnsignedTypes::class)
    actual fun verify(
        type: ECDSAType,
        data: ByteArray,
        publicKey: KMMECPublicKey,
        signature: ByteArray
    ): Boolean {
        val hashedData = when (type) {
            ECDSAType.ECDSA_SHA256 -> SHA256().digest(data)
            ECDSAType.ECDSA_SHA384 -> SHA384().digest(data)
            ECDSAType.ECDSA_SHA512 -> SHA512().digest(data)
        }
        memScoped {
            // Context
            val context = secp256k1_context_create((SECP256K1_CONTEXT_SIGN or SECP256K1_CONTEXT_VERIFY).convert())

            // Public Key Alloc
            val publicKeyPinned = publicKey.getEncoded().asUByteArray().pin()
            val natPub = publicKeyPinned.addressOf(0)
            val nPublicKey = alloc<secp256k1_pubkey>()

            if (secp256k1_ec_pubkey_parse(context, nPublicKey.ptr, natPub, publicKey.nativeValue.size.convert()) != 1) {
                throw Secp256k1Exception("secp256k1_ec_pubkey_parse() failed")
            }

            // Message
            val messagePinned = hashedData.toUByteArray().pin()
            val nMessage = messagePinned.addressOf(0)

            // Signature
            val sig = alloc<secp256k1_ecdsa_signature>()
            val sigPinned = signature.toUByteArray().pin()
            val nativeBytes = sigPinned.addressOf(0)
            val result = when {
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
}
