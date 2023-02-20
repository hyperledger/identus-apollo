package io.iohk.atala.prism.apollo.utils

import com.ionspin.kotlin.bignum.integer.BigInteger
import com.ionspin.kotlin.bignum.integer.Sign
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

        fun secp256k1FromBytes(encoded: ByteArray): KMMECPublicKey {
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

        fun secp256k1FromByteCoordinates(x: ByteArray, y: ByteArray): KMMECPublicKey {
            val xTrimmed = x.dropWhile { it == 0.toByte() }.toByteArray()
            require(xTrimmed.size <= ECConfig.PUBLIC_KEY_COORDINATE_BYTE_SIZE) {
                "Expected x coordinate byte length to be less than or equal ${ECConfig.PUBLIC_KEY_COORDINATE_BYTE_SIZE}, but got ${x.size} bytes"
            }

            val yTrimmed = y.dropWhile { it == 0.toByte() }.toByteArray()
            require(yTrimmed.size <= ECConfig.PUBLIC_KEY_COORDINATE_BYTE_SIZE) {
                "Expected y coordinate byte length to be less than or equal ${ECConfig.PUBLIC_KEY_COORDINATE_BYTE_SIZE}, but got ${y.size} bytes"
            }

            val xInteger = BigInteger.fromByteArray(xTrimmed, Sign.POSITIVE)
            val yInteger = BigInteger.fromByteArray(yTrimmed, Sign.POSITIVE)
            return secp256k1FromBigIntegerCoordinates(xInteger, yInteger)
        }

        private fun computeCurvePoint(basePoint: BasePoint): KMMECPoint {
            val x = BigInteger.parseString(basePoint.getX().toString())
            val y = BigInteger.parseString(basePoint.getY().toString())
            return KMMECPoint(x, y)
        }
    }
}
