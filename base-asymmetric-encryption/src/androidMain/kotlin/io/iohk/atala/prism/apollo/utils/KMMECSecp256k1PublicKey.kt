package io.iohk.atala.prism.apollo.utils

import com.ionspin.kotlin.bignum.integer.BigInteger
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey
import org.bouncycastle.jce.ECNamedCurveTable
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.bouncycastle.jce.spec.ECNamedCurveSpec
import java.security.KeyFactory
import java.security.spec.ECParameterSpec
import java.security.spec.ECPoint
import java.security.spec.ECPublicKeySpec

actual class KMMECSecp256k1PublicKey(nativeValue: BCECPublicKey) : KMMECPublicKey(nativeValue), KMMECSecp256k1PublicKeyCommon {

    actual val ecPoint: KMMECPoint
        get() = computeCurvePoint(nativeValue)

    init {
        // This check is a preventive step, the underlying platform specific libraries seem to throw their own errors in this case.
        if (!isPointOnSecp256k1Curve(ecPoint)) {
            throw ECPublicKeyInitializationException("ECPoint corresponding to a public key doesn't belong to Secp256k1 curve")
        }
    }

    /**
     * @return a point from the Secp256k1 elliptic curve representing this public key
     */
    override fun getCurvePoint(): KMMECPoint = ecPoint

    override fun hashCode(): Int {
        return getEncoded().hashCode()
    }

    override fun equals(other: Any?): Boolean {
        return when (other) {
            is KMMECSecp256k1PublicKey -> getEncoded().contentEquals(other.getEncoded())
            else -> false
        }
    }

    actual companion object : KMMECSecp256k1PublicKeyCommonStaticInterface {

        override fun secp256k1FromBigIntegerCoordinates(x: BigInteger, y: BigInteger): KMMECSecp256k1PublicKey {
            val ecPoint = ECPoint(x.toJavaBigInteger(), y.toJavaBigInteger())
            if (!isPointOnSecp256k1Curve(KMMECPoint(x, y))) {
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
            return KMMECSecp256k1PublicKey(keyFactory.generatePublic(spec) as BCECPublicKey)
        }

        override fun secp256k1FromCompressed(compressed: ByteArray): KMMECSecp256k1PublicKey {
            require(compressed.size == ECConfig.PUBLIC_KEY_COMPRESSED_BYTE_SIZE) {
                "Compressed byte array's expected length is ${ECConfig.PUBLIC_KEY_COMPRESSED_BYTE_SIZE}, but got ${compressed.size}"
            }
            val ecParameterSpec = ECNamedCurveTable.getParameterSpec(KMMEllipticCurve.SECP256k1.value)
            val bouncyCastlePoint = ecParameterSpec.curve.decodePoint(compressed)
            val point = ECPoint(
                bouncyCastlePoint.xCoord.toBigInteger(),
                bouncyCastlePoint.yCoord.toBigInteger()
            )
            return secp256k1FromBigIntegerCoordinates(point.affineX.toKotlinBigInteger(), point.affineY.toKotlinBigInteger())
        }
    }
}
