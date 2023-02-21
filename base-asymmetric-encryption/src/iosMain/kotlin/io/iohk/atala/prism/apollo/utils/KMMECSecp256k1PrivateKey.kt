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
actual class KMMECSecp256k1PrivateKey(nativeValue: UByteArray) : KMMECPrivateKey(nativeValue), Encodable {

    actual val d: BigInteger
        get() = privateKeyD(nativeValue)

    init {
        if (d < BigInteger.TWO || d >= ECConfig.n) {
            throw ECPrivateKeyInitializationException(
                "Private key D should be in range [2; ${ECConfig.n})"
            )
        }
    }

    actual fun getPublicKey(): KMMECSecp256k1PublicKey {
        return memScoped {
            val context = secp256k1_context_create((SECP256K1_CONTEXT_SIGN or SECP256K1_CONTEXT_VERIFY).convert())
            memScope.defer {
                secp256k1_context_destroy(context)
            }
            val privkey = this@KMMECSecp256k1PrivateKey.getEncoded().toUByteArray().toCArrayPointer(this)
            val publicKey = alloc<secp256k1_pubkey>()
            if (secp256k1_ec_pubkey_create(context, publicKey.ptr, privkey) != 1) {
                error("Invalid private key")
            }

            val publicKeyBytes = publicKey.data.toUByteArray(ECConfig.PUBLIC_KEY_BYTE_SIZE)
            KMMECSecp256k1PublicKey(publicKeyBytes)
        }
    }

    override fun getEncoded(): ByteArray {
        return nativeValue.toByteArray()
    }

    override fun hashCode(): Int {
        return getEncoded().hashCode()
    }

    override fun equals(other: Any?): Boolean {
        return when (other) {
            is KMMECSecp256k1PrivateKey -> getEncoded().contentEquals(other.getEncoded())
            else -> false
        }
    }

    actual companion object : KMMECSecp256k1PrivateKeyCommonStaticInterface {
        override fun secp256k1FromBigInteger(d: BigInteger): KMMECSecp256k1PrivateKey {
            return KMMECSecp256k1PrivateKey(d.toByteArray().padStart(ECConfig.PRIVATE_KEY_BYTE_SIZE, 0).toUByteArray())
        }

        @OptIn(ExperimentalUnsignedTypes::class)
        @Throws(ECPrivateKeyDecodingException::class)
        override fun secp256k1FromBytes(encoded: ByteArray): KMMECSecp256k1PrivateKey {
            if (encoded.size != ECConfig.PRIVATE_KEY_BYTE_SIZE) {
                throw ECPrivateKeyDecodingException("Expected encoded byte length to be ${ECConfig.PRIVATE_KEY_BYTE_SIZE}, but got ${encoded.size}")
            }

            val d = BigInteger.fromByteArray(encoded, Sign.POSITIVE)
            return KMMECSecp256k1PrivateKey(d.toByteArray().padStart(ECConfig.PRIVATE_KEY_BYTE_SIZE, 0).toUByteArray())
        }

        private fun privateKeyD(privateKey: UByteArray): BigInteger {
            return BigInteger.fromUByteArray(privateKey, Sign.POSITIVE)
        }
    }
}
