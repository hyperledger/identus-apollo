package io.iohk.atala.prism.apollo.utils

import com.ionspin.kotlin.bignum.integer.BigInteger
import kotlin.experimental.and

// TODO(Create KMMSecp256k1PublicKey to contains all below implementation to better separate responsibilities)
abstract class KMMECPublicKeyCommon(internal val KMMECPoint: KMMECPoint) {
    /**
     * Guarantees to return a list of 65 bytes in the following form:
     *
     * 0x04 ++ xBytes ++ yBytes
     *
     * Where `xBytes` and `yBytes` represent a 32-byte coordinates of a point
     * on the secp256k1 elliptic curve, which follow the formula below:
     *
     * y^2 == x^3 + 7
     *
     * @return a list of 65 bytes that represent uncompressed public key
     */
    fun getEncoded(): ByteArray {
        val size = ECConfig.PRIVATE_KEY_BYTE_SIZE
        val basePoint = getCurvePoint()
        val xArr = basePoint.x.bytes()
        val yArr = basePoint.y.bytes()
        if (xArr.size == size && yArr.size == size) {
            val arr = ByteArray(1 + 2 * size) { 0 }
            arr[0] = 4 // Uncompressed point indicator for encoding
            xArr.copyInto(arr, size - xArr.size + 1)
            yArr.copyInto(arr, arr.size - yArr.size)
            return arr
        } else {
            throw IllegalStateException("Point coordinates do not match field size")
        }
    }

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
    fun getCurvePoint(): KMMECPoint = KMMECPoint
}

interface KMMECPublicKeyCommonStatic {
    fun isPointOnSecp256k1Curve(point: KMMECPoint): Boolean {
        val x = point.x.coordinate
        val y = point.y.coordinate

        // Elliptic curve equation for Secp256k1
        return ((y * y - x * x * x - ECConfig.b) mod ECConfig.p) == BigInteger.ZERO
    }
}

expect class KMMECPublicKey : KMMECPublicKeyCommon {
    companion object : KMMECPublicKeyCommonStatic
}
