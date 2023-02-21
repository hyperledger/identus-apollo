package io.iohk.atala.prism.apollo.utils

/* ktlint-disable */
import com.ionspin.kotlin.bignum.integer.BigInteger
import com.ionspin.kotlin.bignum.integer.Sign
import kotlinx.cinterop.alloc
import kotlinx.cinterop.convert
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
// import io.iohk.atala.prism.apollo.kmmsecp256k1.*
import secp256k1.*
/* ktlint-disable */

@OptIn(ExperimentalUnsignedTypes::class)
actual class KMMECSecp256k1PublicKey(nativeValue: UByteArray) : KMMECPublicKey(nativeValue), KMMECSecp256k1PublicKeyCommon {

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
            return secp256k1FromCompressed(byteArrayOf(0x04) + KMMECCoordinate(x).bytes() + KMMECCoordinate(y).bytes())
        }

        override fun secp256k1FromCompressed(compressed: ByteArray): KMMECSecp256k1PublicKey {
            return memScoped {
                val context = secp256k1_context_create((SECP256K1_CONTEXT_SIGN or SECP256K1_CONTEXT_VERIFY).convert())
                defer {
                    secp256k1_context_destroy(context)
                }
                val pubkey = alloc<secp256k1_pubkey>()
                val input = compressed.toUByteArray().toCArrayPointer(this)
                val result = secp256k1_ec_pubkey_parse(context, pubkey.ptr, input, compressed.size.convert())
                if (result != 1) {
                    error("Could not parse public key")
                }

                val publicKeyBytes = pubkey.data.toUByteArray(ECConfig.PUBLIC_KEY_BYTE_SIZE)
                KMMECSecp256k1PublicKey(publicKeyBytes)
            }
        }

        override fun secp256k1FromBytes(encoded: ByteArray): KMMECSecp256k1PublicKey {
            val expectedLength = 1 + 2 * ECConfig.PRIVATE_KEY_BYTE_SIZE
            require(encoded.size == expectedLength) {
                "Encoded byte array's expected length is $expectedLength, but got ${encoded.size} bytes"
            }
            require(encoded[0].toInt() == 0x04) {
                "First byte was expected to be 0x04, but got ${encoded[0]}"
            }

            val x = encoded.copyOfRange(1, 1 + ECConfig.PRIVATE_KEY_BYTE_SIZE)
            val y = encoded.copyOfRange(1 + ECConfig.PRIVATE_KEY_BYTE_SIZE, encoded.size)

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
    }
}
