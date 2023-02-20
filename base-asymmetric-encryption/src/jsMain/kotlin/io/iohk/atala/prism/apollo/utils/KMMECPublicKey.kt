package io.iohk.atala.prism.apollo.utils

import com.ionspin.kotlin.bignum.integer.BigInteger
import io.iohk.atala.prism.apollo.utils.external.Coordinates
import io.iohk.atala.prism.apollo.utils.external.base.BasePoint
import io.iohk.atala.prism.apollo.utils.external.ec

actual class KMMECPublicKey(val nativeValue: BasePoint) : KMMECPublicKeyCommon(computeCurvePoint(nativeValue)) {

    actual companion object : KMMECPublicKeyCommonStaticInterface {

        override fun secp256k1FromBigIntegerCoordinates(x: BigInteger, y: BigInteger): KMMECPublicKey {
            val xCoord = x.toByteArray()
            val yCoord = y.toByteArray()
            val ecjs = ec("secp256k1")
            val keyPair = ecjs.keyFromPublic(
                object : Coordinates {
                    override var x = xCoord.toHex()
                    override var y = yCoord.toHex()
                }
            )
            return KMMECPublicKey(keyPair.getPublic())
        }

        override fun secp256k1FromCompressed(compressed: ByteArray): KMMECPublicKey {
            require(compressed.size == ECConfig.PUBLIC_KEY_COMPRESSED_BYTE_SIZE) {
                "Compressed byte array's expected length is ${ECConfig.PUBLIC_KEY_COMPRESSED_BYTE_SIZE}, but got ${compressed.size}"
            }
            val ecjs = ec("secp256k1")
            val point = ecjs.curve.decodePoint(compressed.asUint8Array())
            val uncompressedEncoding = point.encode("hex").decodeHex()
            return secp256k1FromBytes(uncompressedEncoding)
        }

        private fun computeCurvePoint(basePoint: BasePoint): KMMECPoint {
            val x = BigInteger.parseString(basePoint.getX().toString())
            val y = BigInteger.parseString(basePoint.getY().toString())
            return KMMECPoint(x, y)
        }
    }
}
