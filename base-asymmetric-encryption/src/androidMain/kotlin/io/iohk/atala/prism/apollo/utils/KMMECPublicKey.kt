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

actual class KMMECPublicKey(val nativeValue: BCECPublicKey) : KMMECPublicKeyCommon(computeCurvePoint(nativeValue)) {
    actual companion object : KMMECPublicKeyCommonStaticInterface {

        override fun secp256k1FromBigIntegerCoordinates(x: BigInteger, y: BigInteger): KMMECPublicKey {
            val ecPoint = ECPoint(x.toJavaBigInteger(), y.toJavaBigInteger())
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

        override fun secp256k1FromCompressed(compressed: ByteArray): KMMECPublicKey {
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

        override fun secp256k1FromBytes(encoded: ByteArray): KMMECPublicKey {
            val expectedLength = 1 + 2 * ECConfig.PRIVATE_KEY_BYTE_SIZE
            require(encoded.size == expectedLength) {
                "Encoded byte array's expected length is $expectedLength, but got ${encoded.size} bytes"
            }
            require(encoded[0].toInt() == 0x04) {
                "First byte was expected to be 0x04, but got ${encoded[0]}"
            }

            val xBytes = encoded.copyOfRange(1, 1 + ECConfig.PRIVATE_KEY_BYTE_SIZE)
            val yBytes = encoded.copyOfRange(1 + ECConfig.PRIVATE_KEY_BYTE_SIZE, encoded.size)
            return secp256k1FromByteCoordinates(xBytes, yBytes)
        }

        private fun computeCurvePoint(key: BCECPublicKey): KMMECPoint {
            val javaPoint = key.w
            return KMMECPoint(javaPoint.affineX.toKotlinBigInteger(), javaPoint.affineY.toKotlinBigInteger())
        }
    }
}
