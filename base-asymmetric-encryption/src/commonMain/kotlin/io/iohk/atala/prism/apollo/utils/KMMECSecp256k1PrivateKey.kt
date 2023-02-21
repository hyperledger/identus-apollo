package io.iohk.atala.prism.apollo.utils

import com.ionspin.kotlin.bignum.integer.BigInteger
import com.ionspin.kotlin.bignum.integer.Sign

interface KMMECSecp256k1PrivateKeyCommonStaticInterface {

    fun secp256k1FromBigInteger(d: BigInteger): KMMECSecp256k1PrivateKey

    @Throws(ECPrivateKeyDecodingException::class)
    fun secp256k1FromBytes(encoded: ByteArray): KMMECSecp256k1PrivateKey {
        if (encoded.size != ECConfig.PRIVATE_KEY_BYTE_SIZE) {
            throw ECPrivateKeyDecodingException("Expected encoded byte length to be ${ECConfig.PRIVATE_KEY_BYTE_SIZE}, but got ${encoded.size}")
        }

        return secp256k1FromBigInteger(BigInteger.fromByteArray(encoded, Sign.POSITIVE))
    }
}

expect class KMMECSecp256k1PrivateKey : KMMECPrivateKey, Encodable {
    val d: BigInteger

    fun getPublicKey(): KMMECSecp256k1PublicKey

    companion object : KMMECSecp256k1PrivateKeyCommonStaticInterface
}
