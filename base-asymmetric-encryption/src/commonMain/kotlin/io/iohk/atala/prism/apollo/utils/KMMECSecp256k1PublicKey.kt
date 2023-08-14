package io.iohk.atala.prism.apollo.utils

import com.ionspin.kotlin.bignum.integer.BigInteger
import com.ionspin.kotlin.bignum.integer.Sign
import io.iohk.atala.prism.apollo.secp256k1.Secp256k1Lib
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport
import kotlin.js.JsName

@OptIn(ExperimentalJsExport::class)
@JsExport
interface KMMECSecp256k1PublicKeyCommonStaticInterface {
    /**
     * Check if key point is on the Secp256k1 curve or not
     * @param point public key point
     * @return true if point on curve, false if not.
     * @exception ClassCastException This method fails in JS. To be further investigated.
     */
    fun isPointOnSecp256k1Curve(point: KMMECPoint): Boolean {
        val x = BigInteger.fromByteArray(point.x, Sign.POSITIVE)
        val y = BigInteger.fromByteArray(point.y, Sign.POSITIVE)

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

        val header: Byte = 0x04
        return KMMECSecp256k1PublicKey(byteArrayOf(header) + x + y)
    }
}

@OptIn(ExperimentalJsExport::class)
@JsExport
class KMMECSecp256k1PublicKey {
    val raw: ByteArray

    @JsName("fromByteArray")
    constructor(raw: ByteArray) {
        this.raw = raw
    }

    fun getCurvePoint(): KMMECPoint {
        if (raw.size != 65) {
            throw IllegalArgumentException("Public key should be 65 bytes long")
        }
        if (raw[0] != 4.toByte()) {
            throw IllegalArgumentException("Public key should start with 0x04")
        }
        val x = raw.sliceArray(1..32)
        val y = raw.sliceArray(33..64)

        return KMMECPoint(x, y)
    }

    /**
     * Verify provided signature
     * @param signature that we need to verify
     * @param data that was used in signature
     * @return true when valid, false when invalid
     */
    fun verify(signature: ByteArray, data: ByteArray): Boolean {
        val secp256k1Lib = Secp256k1Lib()
        return secp256k1Lib.verify(raw, signature, data)
    }

    companion object : KMMECSecp256k1PublicKeyCommonStaticInterface
}
