package io.iohk.atala.prism.apollo.secp256k1

import com.ionspin.kotlin.bignum.integer.BigInteger
import fr.acinq.secp256k1.Secp256k1
import io.iohk.atala.prism.apollo.hashing.SHA256
import io.iohk.atala.prism.apollo.utils.ECConfig
import io.iohk.atala.prism.apollo.utils.KMMECPoint
import io.iohk.atala.prism.apollo.utils.KMMECSecp256k1PublicKey
import io.iohk.atala.prism.apollo.utils.KMMEllipticCurve
import io.iohk.atala.prism.apollo.utils.toKotlinBigInteger
import org.bouncycastle.jce.ECNamedCurveTable
import java.security.spec.ECPoint

actual class Secp256k1Lib {
    actual fun createPublicKey(privateKey: ByteArray, compressed: Boolean): ByteArray {
        val pubKey = Secp256k1.pubkeyCreate(privateKey)
        if (compressed) {
            return Secp256k1.pubKeyCompress(pubKey)
        }
        return pubKey
    }

    actual fun derivePrivateKey(
        privateKeyBytes: ByteArray,
        derivedPrivateKeyBytes: ByteArray
    ): ByteArray? {
        return Secp256k1.privKeyTweakAdd(privateKeyBytes, derivedPrivateKeyBytes)
    }

    actual fun sign(privateKey: ByteArray, data: ByteArray): ByteArray {
        val sha = SHA256().digest(data)
        val compactSignature = Secp256k1.sign(sha, privateKey)
        return Secp256k1.compact2der(compactSignature)
    }

    actual fun verify(
        publicKey: ByteArray,
        signature: ByteArray,
        data: ByteArray
    ): Boolean {
        val sha = SHA256().digest(data)
        return Secp256k1.verify(signature, sha, publicKey)
    }

    actual fun decodePoint(compressed: ByteArray): KMMECPoint {
        require(compressed.size == ECConfig.PUBLIC_KEY_COMPRESSED_BYTE_SIZE) {
            "Compressed byte array's expected length is ${ECConfig.PUBLIC_KEY_COMPRESSED_BYTE_SIZE}, but got ${compressed.size}"
        }
        val ecParameterSpec = ECNamedCurveTable.getParameterSpec(KMMEllipticCurve.SECP256k1.value)
        val bouncyCastlePoint = ecParameterSpec.curve.decodePoint(compressed)
        val point = ECPoint(
            bouncyCastlePoint.xCoord.toBigInteger(),
            bouncyCastlePoint.yCoord.toBigInteger()
        )
        return KMMECPoint(point.affineX.toByteArray(), point.affineY.toByteArray())
    }
}
