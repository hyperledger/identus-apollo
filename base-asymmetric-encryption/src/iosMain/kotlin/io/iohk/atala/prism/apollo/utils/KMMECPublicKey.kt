package io.iohk.atala.prism.apollo.utils

/* ktlint-disable */
import com.ionspin.kotlin.bignum.integer.BigInteger
import com.ionspin.kotlin.bignum.integer.Sign
import kotlinx.cinterop.MemScope
import kotlinx.cinterop.UByteVar
import kotlinx.cinterop.alloc
import kotlinx.cinterop.allocArray
import kotlinx.cinterop.convert
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.set
import kotlinx.cinterop.value
import platform.posix.size_tVar
// import io.iohk.atala.prism.apollo.kmmsecp256k1.*
import secp256k1.*
/* ktlint-disable */

@OptIn(ExperimentalUnsignedTypes::class)
actual class KMMECPublicKey(val nativeValue: UByteArray) : KMMECPublicKeyCommon(computeCurvePoint(nativeValue)) {
    actual companion object : KMMECPublicKeyCommonStatic {

        fun secp256k1FromBytes(encoded: ByteArray): KMMECPublicKey {
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

        fun secp256k1FromBigIntegerCoordinates(x: BigInteger, y: BigInteger): KMMECPublicKey {
            return parsePublicKey(byteArrayOf(0x04) + KMMECCoordinate(x).bytes() + KMMECCoordinate(y).bytes())
        }

        private fun parsePublicKey(encoded: ByteArray): KMMECPublicKey {
            return memScoped {
                val context = secp256k1_context_create((SECP256K1_CONTEXT_SIGN or SECP256K1_CONTEXT_VERIFY).convert())
                defer {
                    secp256k1_context_destroy(context)
                }
                val pubkey = alloc<secp256k1_pubkey>()
                val input = encoded.toUByteArray().toCArrayPointer(this)
                val result = secp256k1_ec_pubkey_parse(context, pubkey.ptr, input, encoded.size.convert())
                if (result != 1) {
                    error("Could not parse public key")
                }

                val publicKeyBytes = pubkey.data.toUByteArray(ECConfig.PUBLIC_KEY_BYTE_SIZE)
                KMMECPublicKey(publicKeyBytes)
            }
        }

        private fun toSecpPubkey(memScope: MemScope, key: UByteArray): secp256k1_pubkey {
            val pubkey = memScope.alloc<secp256k1_pubkey>()
            for (i in 0 until ECConfig.PUBLIC_KEY_BYTE_SIZE) {
                pubkey.data[i] = key[i]
            }

            return pubkey
        }

        private fun convertRepresentation(key: UByteArray): ByteArray {
            return memScoped {
                val context = secp256k1_context_create((SECP256K1_CONTEXT_SIGN or SECP256K1_CONTEXT_VERIFY).convert())
                defer {
                    secp256k1_context_destroy(context)
                }
                val pubkey = toSecpPubkey(this, key)
                val output = memScope.allocArray<UByteVar>(ECConfig.PUBLIC_KEY_BYTE_SIZE)
                val outputLen = alloc<size_tVar>()
                outputLen.value = ECConfig.PUBLIC_KEY_BYTE_SIZE.convert()
                val result =
                    secp256k1_ec_pubkey_serialize(context, output, outputLen.ptr, pubkey.ptr, SECP256K1_EC_UNCOMPRESSED)
                if (result != 1) {
                    error("Could not serialize public key")
                }
                output.toUByteArray(outputLen.value.convert()).toByteArray()
            }
        }
        @OptIn(ExperimentalUnsignedTypes::class)
        private fun computeCurvePoint(key: UByteArray): KMMECPoint {
            val encoded = convertRepresentation(key)
            val xBytes = encoded.slice(1..32)
            val x = BigInteger.fromByteArray(xBytes.toByteArray(), Sign.POSITIVE)
            val yBytes = encoded.slice(33..64)
            val y = BigInteger.fromByteArray(yBytes.toByteArray(), Sign.POSITIVE)

            return KMMECPoint(x, y)
        }
    }
}
