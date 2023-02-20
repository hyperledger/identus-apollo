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

actual class KMMECPrivateKey(val nativeValue: BCECPrivateKey) : KMMECPrivateKeyCommon(privateKeyD(nativeValue)) {

    override fun getEncoded(): ByteArray {
        val byteList = this.d.toJavaBigInteger().toUnsignedByteArray()
        val padding = ByteArray(ECConfig.PRIVATE_KEY_BYTE_SIZE - byteList.size) { 0 }
        return padding + byteList
    }

    fun getPublicKey(): KMMECPublicKey {
        val ecParameterSpec = ECNamedCurveTable.getParameterSpec(KMMEllipticCurve.SECP256k1.value)
        val q = ecParameterSpec.g.multiply( this.nativeValue.d)
        val pupSpec = ECPublicKeySpec(q, ecParameterSpec)
        val provider = BouncyCastleProvider()
        val keyFactory = KeyFactory.getInstance("EC", provider)
        return KMMECPublicKey(keyFactory.generatePublic(pupSpec) as BCECPublicKey)
    }

    companion object {
        fun secp256k1FromBigInteger(d: BigInteger): KMMECPrivateKey {
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
            return KMMECPrivateKey(keyFactory.generatePrivate(spec) as BCECPrivateKey)
        }

        private fun privateKeyD(privateKey: BCECPrivateKey): BigInteger {
            return privateKey.d.toKotlinBigInteger()
        }
    }
}
