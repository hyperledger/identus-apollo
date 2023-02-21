package io.iohk.atala.prism.apollo.utils

import com.ionspin.kotlin.bignum.integer.BigInteger
import com.ionspin.kotlin.bignum.integer.Sign
import kotlin.experimental.and

interface KMMECSecp256k1PublicKeyCommon {

    fun getEncodedCompressed(): ByteArray {
        val size = ECConfig.PRIVATE_KEY_BYTE_SIZE
        val curvePoint = getCurvePoint()
        val yArr = curvePoint.y.bytes()
        val xArr = curvePoint.x.bytes()
        val prefix = 2 + (yArr[yArr.size - 1] and 1)
        val arr = ByteArray(1 + size)
        arr[0] = prefix.toByte()
        xArr.copyInto(arr, 1)
        return arr
    }

    /**
     * @return a point from the Secp256k1 elliptic curve representing this public key
     */
    fun getCurvePoint(): KMMECPoint
}

interface KMMECSecp256k1PublicKeyCommonStaticInterface {
    fun isPointOnSecp256k1Curve(point: KMMECPoint): Boolean {
        val x = point.x.coordinate
        val y = point.y.coordinate

        // Elliptic curve equation for Secp256k1
        return ((y * y - x * x * x - ECConfig.b) mod ECConfig.p) == BigInteger.ZERO
    }

    fun secp256k1FromBytes(encoded: ByteArray): KMMECSecp256k1PublicKey {
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

    fun secp256k1FromByteCoordinates(x: ByteArray, y: ByteArray): KMMECSecp256k1PublicKey {
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

    fun secp256k1FromBigIntegerCoordinates(x: BigInteger, y: BigInteger): KMMECSecp256k1PublicKey
    fun secp256k1FromCompressed(compressed: ByteArray): KMMECSecp256k1PublicKey
}

expect class KMMECSecp256k1PublicKey : KMMECPublicKey, KMMECSecp256k1PublicKeyCommon {
    val ecPoint: KMMECPoint

    companion object : KMMECSecp256k1PublicKeyCommonStaticInterface
}
