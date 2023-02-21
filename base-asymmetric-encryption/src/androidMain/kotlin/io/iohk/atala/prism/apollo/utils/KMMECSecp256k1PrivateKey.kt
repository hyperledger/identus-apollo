package io.iohk.atala.prism.apollo.utils

import com.ionspin.kotlin.bignum.integer.BigInteger
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPrivateKey
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey
import org.bouncycastle.jce.ECNamedCurveTable
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.bouncycastle.jce.spec.ECNamedCurveSpec
import org.bouncycastle.jce.spec.ECPublicKeySpec
import java.security.KeyFactory
import java.security.spec.ECParameterSpec
import java.security.spec.ECPrivateKeySpec

actual class KMMECSecp256k1PrivateKey(nativeValue: BCECPrivateKey) : KMMECPrivateKey(nativeValue), Encodable {

    actual val d: BigInteger
        get() = privateKeyD(nativeValue)

    init {
        if (d < BigInteger.TWO || d >= ECConfig.n)
            throw ECPrivateKeyInitializationException(
                "Private key D should be in range [2; ${ECConfig.n})"
            )
    }

    actual fun getPublicKey(): KMMECSecp256k1PublicKey {
        val ecParameterSpec = ECNamedCurveTable.getParameterSpec(KMMEllipticCurve.SECP256k1.value)
        val q = ecParameterSpec.g.multiply(this.nativeValue.d)
        val pupSpec = ECPublicKeySpec(q, ecParameterSpec)
        val provider = BouncyCastleProvider()
        val keyFactory = KeyFactory.getInstance("EC", provider)
        return KMMECSecp256k1PublicKey(keyFactory.generatePublic(pupSpec) as BCECPublicKey)
    }

    override fun getEncoded(): ByteArray {
        val byteList = this.d.toJavaBigInteger().toUnsignedByteArray()
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
            val ecParameterSpec = ECNamedCurveTable.getParameterSpec(KMMEllipticCurve.SECP256k1.value)
            val ecNamedCurveSpec: ECParameterSpec = ECNamedCurveSpec(
                ecParameterSpec.name,
                ecParameterSpec.curve,
                ecParameterSpec.g,
                ecParameterSpec.n
            )
            val provider = BouncyCastleProvider()
            val keyFactory = KeyFactory.getInstance("EC", provider)
            val spec = ECPrivateKeySpec(d.toJavaBigInteger(), ecNamedCurveSpec)
            return KMMECSecp256k1PrivateKey(keyFactory.generatePrivate(spec) as BCECPrivateKey)
        }

        private fun privateKeyD(privateKey: BCECPrivateKey): BigInteger {
            return privateKey.d.toKotlinBigInteger()
        }
    }
}
