package io.iohk.atala.prism.apollo.utils

import com.ionspin.kotlin.bignum.integer.BigInteger
import com.ionspin.kotlin.bignum.integer.Sign

@OptIn(ExperimentalUnsignedTypes::class)
actual class KMMECPrivateKey(val nativeValue: UByteArray) : KMMECPrivateKeyCommon(BigInteger.fromUByteArray(nativeValue, Sign.POSITIVE)) {

    override fun getEncoded(): ByteArray {
        return nativeValue.toByteArray()
    }

    companion object {
        @OptIn(ExperimentalUnsignedTypes::class)
        @Throws(ECPrivateKeyDecodingException::class)
        fun secp256k1FromBytes(encoded: ByteArray): KMMECPrivateKey {
            if (encoded.size != ECConfig.PRIVATE_KEY_BYTE_SIZE) {
                throw ECPrivateKeyDecodingException("Expected encoded byte length to be ${ECConfig.PRIVATE_KEY_BYTE_SIZE}, but got ${encoded.size}")
            }

            val d = BigInteger.fromByteArray(encoded, Sign.POSITIVE)
            return KMMECPrivateKey(d.toByteArray().padStart(ECConfig.PRIVATE_KEY_BYTE_SIZE, 0).toUByteArray())
        }
    }
}
