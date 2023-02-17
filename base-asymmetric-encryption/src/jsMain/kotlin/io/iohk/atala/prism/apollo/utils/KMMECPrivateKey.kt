package io.iohk.atala.prism.apollo.utils

import com.ionspin.kotlin.bignum.integer.BigInteger
import com.ionspin.kotlin.bignum.integer.Sign
import io.iohk.atala.prism.apollo.utils.external.BN
import io.iohk.atala.prism.apollo.utils.external.ec

actual class KMMECPrivateKey(val nativeValue: BN) : KMMECPrivateKeyCommon(BigInteger.parseString(nativeValue.toString())) {

    fun getEncoded(): ByteArray {
        val byteList = nativeValue.toArray().map { it.toByte() }
        val padding = ByteArray(ECConfig.PRIVATE_KEY_BYTE_SIZE - byteList.size) { 0 }
        return padding + byteList
    }

    fun secp256k1PublicKey(): KMMECPublicKey {
        val ecjs = ec("secp256k1")
        val keyPair = ecjs.keyFromPrivate(this.nativeValue.toString("hex"))
        return KMMECPublicKey(keyPair.getPublic())
    }

    companion object {
        fun secp256k1FromBytes(encoded: ByteArray): KMMECPrivateKey {
            if (encoded.size != ECConfig.PRIVATE_KEY_BYTE_SIZE) {
                throw ECPrivateKeyDecodingException("Expected encoded byte length to be ${ECConfig.PRIVATE_KEY_BYTE_SIZE}, but got ${encoded.size}")
            }
            val d = BigInteger.fromByteArray(encoded, Sign.POSITIVE)
            return KMMECPrivateKey(BN(d.toString()))
        }
    }
}
