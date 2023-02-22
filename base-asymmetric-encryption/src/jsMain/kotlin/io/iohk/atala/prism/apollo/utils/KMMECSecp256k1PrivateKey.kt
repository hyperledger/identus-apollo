package io.iohk.atala.prism.apollo.utils

import com.ionspin.kotlin.bignum.integer.BigInteger
import io.iohk.atala.prism.apollo.utils.external.BN
import io.iohk.atala.prism.apollo.utils.external.ec

actual class KMMECSecp256k1PrivateKey(nativeValue: BN) : KMMECPrivateKey(nativeValue), Encodable {

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
        val ecjs = ec("secp256k1")
        val keyPair = ecjs.keyFromPrivate(this.nativeValue.toString("hex"))
        return KMMECSecp256k1PublicKey(keyPair.getPublic())
    }

    override fun getEncoded(): ByteArray {
        val byteList = nativeValue.toArray().map { it.toByte() }
        val padding = ByteArray(ECConfig.PRIVATE_KEY_BYTE_SIZE - byteList.size) { 0 }
        return padding + byteList
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
            return KMMECSecp256k1PrivateKey(BN(d.toString()))
        }

        private fun privateKeyD(privateKey: BN): BigInteger {
            return BigInteger.parseString(privateKey.toString())
        }
    }
}
