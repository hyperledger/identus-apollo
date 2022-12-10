package io.iohk.atala.prism.apollo

import org.spongycastle.jce.ECNamedCurveTable
import org.spongycastle.jce.provider.BouncyCastleProvider
import org.spongycastle.jce.spec.ECNamedCurveSpec
import org.spongycastle.jce.spec.ECPublicKeySpec
import java.lang.IllegalStateException
import java.math.BigInteger
import java.security.PrivateKey
import java.security.Provider
import java.security.PublicKey
import java.security.spec.ECParameterSpec
import java.security.spec.ECPoint
import java.security.spec.KeySpec

internal actual object GenericJavaCryptography {
    actual val provider: Provider = BouncyCastleProvider()
    private val ecParameterSpec = ECNamedCurveTable.getParameterSpec(ECConfig.CURVE_NAME)
    actual val ecNamedCurveSpec: ECParameterSpec = ECNamedCurveSpec(
        ecParameterSpec.name,
        ecParameterSpec.curve,
        ecParameterSpec.g,
        ecParameterSpec.n
    )

    actual fun keySpec(d: BigInteger): KeySpec {
        val q = ecParameterSpec.g.multiply(d)
        return ECPublicKeySpec(q, ecParameterSpec)
    }
    actual fun privateKeyD(privateKey: PrivateKey): BigInteger =
        when (privateKey) {
            is org.spongycastle.jcajce.provider.asymmetric.ec.BCECPrivateKey -> privateKey.d
            else -> throw IllegalStateException("Unexpected private key implementation")
        }
    actual fun publicKeyPoint(publicKey: PublicKey): ECPoint =
        when (publicKey) {
            is org.spongycastle.jcajce.provider.asymmetric.ec.BCECPublicKey -> publicKey.w
            else -> throw IllegalStateException("Unexpected public key implementation")
        }
    actual fun decodePoint(compressed: ByteArray): ECPoint {
        val bouncyCastlePoint = ecParameterSpec.curve.decodePoint(compressed)
        return ECPoint(
            bouncyCastlePoint.xCoord.toBigInteger(),
            bouncyCastlePoint.yCoord.toBigInteger()
        )
    }
}
