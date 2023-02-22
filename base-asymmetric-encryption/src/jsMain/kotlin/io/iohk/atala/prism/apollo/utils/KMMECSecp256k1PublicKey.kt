package io.iohk.atala.prism.apollo.utils

import com.ionspin.kotlin.bignum.integer.BigInteger
import io.iohk.atala.prism.apollo.utils.external.Coordinates
import io.iohk.atala.prism.apollo.utils.external.base.BasePoint
import io.iohk.atala.prism.apollo.utils.external.ec

actual class KMMECSecp256k1PublicKey(nativeValue: BasePoint) : KMMECPublicKey(nativeValue), KMMECSecp256k1PublicKeyCommon {

    actual val ecPoint: KMMECPoint
        get() = computeCurvePoint(nativeValue)

    init {
        // This check is a preventive step, the underlying platform specific libraries seem to throw their own errors in this case.
        if (!isPointOnSecp256k1Curve(ecPoint)) {
            throw ECPublicKeyInitializationException("ECPoint corresponding to a public key doesn't belong to Secp256k1 curve")
        }
    }

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
            val xCoord = x.toByteArray()
            val yCoord = y.toByteArray()
            val ecjs = ec("secp256k1")
            val keyPair = ecjs.keyFromPublic(
                object : Coordinates {
                    override var x = xCoord.toHex()
                    override var y = yCoord.toHex()
                }
            )
            return KMMECSecp256k1PublicKey(keyPair.getPublic())
        }

        override fun secp256k1FromCompressed(compressed: ByteArray): KMMECSecp256k1PublicKey {
            require(compressed.size == ECConfig.PUBLIC_KEY_COMPRESSED_BYTE_SIZE) {
                "Compressed byte array's expected length is ${ECConfig.PUBLIC_KEY_COMPRESSED_BYTE_SIZE}, but got ${compressed.size}"
            }
            val ecjs = ec("secp256k1")
            val point = ecjs.curve.decodePoint(compressed.asUint8Array())
            val uncompressedEncoding = point.encode("hex").decodeHex()
            return secp256k1FromBytes(uncompressedEncoding)
        }
    }
}
