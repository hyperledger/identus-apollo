package io.iohk.atala.prism.apollo.utils

import com.ionspin.kotlin.bignum.integer.BigInteger
import com.ionspin.kotlin.bignum.integer.Sign

// TODO(Create KMMSecp256k1PrivateKey to contains all below implementation to better separate responsibilities)

interface KMMECPrivateKeyCommonStaticInterface {
    fun secp256k1FromBigInteger(d: BigInteger): KMMECPrivateKey

    @Throws(ECPrivateKeyDecodingException::class)
    fun secp256k1FromBytes(encoded: ByteArray): KMMECPrivateKey {
        if (encoded.size != ECConfig.PRIVATE_KEY_BYTE_SIZE) {
            throw ECPrivateKeyDecodingException("Expected encoded byte length to be ${ECConfig.PRIVATE_KEY_BYTE_SIZE}, but got ${encoded.size}")
        }

        return secp256k1FromBigInteger(BigInteger.fromByteArray(encoded, Sign.POSITIVE))
    }
}

abstract class KMMECPrivateKeyCommon(val d: BigInteger) : Encodable {
    abstract fun getPublicKey(): KMMECPublicKey
}

expect class KMMECPrivateKey : KMMECPrivateKeyCommon {
    companion object : KMMECPrivateKeyCommonStaticInterface
}
