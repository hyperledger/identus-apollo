package io.iohk.atala.prism.apollo.utils

/* ktlint-disable */
import com.ionspin.kotlin.bignum.integer.BigInteger
import com.ionspin.kotlin.bignum.integer.Sign
import io.iohk.atala.prism.apollo.securerandom.SecureRandom
import kotlinx.cinterop.alloc
import kotlinx.cinterop.convert
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import secp256k1.*
/* ktlint-disable */

actual class KMMECSecp256k1KeyPair actual constructor(actual val privateKey: KMMECSecp256k1PrivateKey, actual val publicKey: KMMECSecp256k1PublicKey) {

    @OptIn(ExperimentalUnsignedTypes::class)
    internal constructor(privateNative: ByteArray, publicNative: ByteArray) : this(KMMECSecp256k1PrivateKey(privateNative.toUByteArray()), KMMECSecp256k1PublicKey(publicNative.toUByteArray()))

    @OptIn(ExperimentalUnsignedTypes::class)
    internal constructor(privateNative: UByteArray, publicNative: UByteArray) : this(KMMECSecp256k1PrivateKey(privateNative), KMMECSecp256k1PublicKey(publicNative))

    actual companion object : Secp256k1KeyPairGeneration {
        @OptIn(ExperimentalUnsignedTypes::class)
        override fun generateSecp256k1KeyPair(): KMMECSecp256k1KeyPair {
            return memScoped {
                // Context
                val context = secp256k1_context_create((SECP256K1_CONTEXT_SIGN or SECP256K1_CONTEXT_VERIFY).convert())
                this.defer {
                    secp256k1_context_destroy(context)
                }

                // Private Key
                var privateKey: ByteArray
                do {
                    privateKey = SecureRandom.generateSeed(32)
                    val privateKeyInt = BigInteger.fromByteArray(privateKey, Sign.POSITIVE)
                } while (privateKeyInt >= ECConfig.n)

                // Public Key
                val publicKey = alloc<secp256k1_pubkey>()
                if (secp256k1_ec_pubkey_create(context, publicKey.ptr, privateKey.toUByteArray().toCArrayPointer(this)) != 1) {
                    error("Invalid private key")
                }

                val privateKeyBytes = privateKey.toUByteArray()
                val publicKeyBytes = publicKey.data.toUByteArray(ECConfig.PUBLIC_KEY_BYTE_SIZE)

                KMMECSecp256k1KeyPair(privateKeyBytes, publicKeyBytes)
            }
        }
    }
}
