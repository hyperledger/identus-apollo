package io.iohk.atala.prism.apollo.keys

import io.iohk.atala.prism.apollo.EC
import io.iohk.atala.prism.apollo.ECConfig
import kotlin.experimental.and

public abstract class ECPublicKeyCommon(internal val ecPoint: ECPoint) : ECKey() {
    init {
        /* This check is a preventive step,
           the underlying platform specific libraries seem to throw their own errors in this case.
         */
        if (!EC.isSecp256k1(ecPoint)) {
            throw ECPublicKeyInitializationException("ECPoint corresponding to a public key doesn't belong to Secp256k1 curve")
        }
    }

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
    override fun getEncoded(): ByteArray {
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

    public fun getEncodedCompressed(): ByteArray {
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
    public fun getCurvePoint(): ECPoint = ecPoint
}

public expect class ECPublicKey : ECPublicKeyCommon
