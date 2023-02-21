package io.iohk.atala.prism.apollo.utils

/* ktlint-disable */
import com.ionspin.kotlin.bignum.integer.BigInteger
import com.ionspin.kotlin.bignum.integer.Sign
import kotlinx.cinterop.alloc
import kotlinx.cinterop.convert
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import secp256k1.*
/* ktlint-disable */

@OptIn(ExperimentalUnsignedTypes::class)
actual class KMMECPrivateKey(val nativeValue: UByteArray) : KMMECPrivateKeyCommon(BigInteger.fromUByteArray(nativeValue, Sign.POSITIVE)) {

    override fun getEncoded(): ByteArray {
        return nativeValue.toByteArray()
    }

    override fun getPublicKey(): KMMECPublicKey {
        return memScoped {
            val context = secp256k1_context_create((SECP256K1_CONTEXT_SIGN or SECP256K1_CONTEXT_VERIFY).convert())
            memScope.defer {
                secp256k1_context_destroy(context)
            }
            val privkey = this@KMMECPrivateKey.getEncoded().toUByteArray().toCArrayPointer(this)
            val publicKey = alloc<secp256k1_pubkey>()
            if (secp256k1_ec_pubkey_create(context, publicKey.ptr, privkey) != 1) {
                error("Invalid private key")
            }

            val publicKeyBytes = publicKey.data.toUByteArray(ECConfig.PUBLIC_KEY_BYTE_SIZE)
            KMMECPublicKey(publicKeyBytes)
        }
    }

    actual companion object : KMMECPrivateKeyCommonStaticInterface {
        override fun secp256k1FromBigInteger(d: BigInteger): KMMECPrivateKey {
            return KMMECPrivateKey(d.toByteArray().padStart(ECConfig.PRIVATE_KEY_BYTE_SIZE, 0).toUByteArray())
        }
        
        @OptIn(ExperimentalUnsignedTypes::class)
        @Throws(ECPrivateKeyDecodingException::class)
        override fun secp256k1FromBytes(encoded: ByteArray): KMMECPrivateKey {
            if (encoded.size != ECConfig.PRIVATE_KEY_BYTE_SIZE) {
                throw ECPrivateKeyDecodingException("Expected encoded byte length to be ${ECConfig.PRIVATE_KEY_BYTE_SIZE}, but got ${encoded.size}")
            }

            val d = BigInteger.fromByteArray(encoded, Sign.POSITIVE)
            return KMMECPrivateKey(d.toByteArray().padStart(ECConfig.PRIVATE_KEY_BYTE_SIZE, 0).toUByteArray())
        }
    }
}
