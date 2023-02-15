package io.iohk.atala.prism.apollo.utils

import com.ionspin.kotlin.bignum.integer.BigInteger
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey
import org.bouncycastle.jce.ECNamedCurveTable
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.bouncycastle.jce.spec.ECNamedCurveSpec
import java.security.KeyFactory
import java.security.spec.ECParameterSpec
import java.security.spec.ECPublicKeySpec

actual class KMMECPublicKey(val nativeValue: BCECPublicKey) : KMMECPublicKeyCommon(computeCurvePoint(nativeValue)) {
    actual companion object : KMMECPublicKeyCommonStatic {
        fun secp256k1FromBigIntegerCoordinates(x: BigInteger, y: BigInteger): KMMECPublicKey {
            val ecPoint = java.security.spec.ECPoint(x.toJavaBigInteger(), y.toJavaBigInteger())
            if (!KMMECPublicKey.isPointOnSecp256k1Curve(KMMECPoint(x, y))) {
                throw ECPublicKeyInitializationException("ECPoint corresponding to a public key doesn't belong to Secp256k1 curve")
            }
            val ecParameterSpec = ECNamedCurveTable.getParameterSpec(KMMEllipticCurve.SECP256k1.value)
            val ecNamedCurveSpec: ECParameterSpec = ECNamedCurveSpec(
                ecParameterSpec.name,
                ecParameterSpec.curve,
                ecParameterSpec.g,
                ecParameterSpec.n
            )
            val spec = ECPublicKeySpec(ecPoint, ecNamedCurveSpec)
            val provider = BouncyCastleProvider()
            val keyFactory = KeyFactory.getInstance("EC", provider)
            return KMMECPublicKey(keyFactory.generatePublic(spec) as BCECPublicKey)
        }

        private fun computeCurvePoint(key: BCECPublicKey): KMMECPoint {
            val javaPoint = key.w
            return KMMECPoint(javaPoint.affineX.toKotlinBigInteger(), javaPoint.affineY.toKotlinBigInteger())
        }
    }
}
