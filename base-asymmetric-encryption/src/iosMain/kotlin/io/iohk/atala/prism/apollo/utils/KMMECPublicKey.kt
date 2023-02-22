package io.iohk.atala.prism.apollo.utils

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
import secp256k1.SECP256K1_CONTEXT_SIGN
import secp256k1.SECP256K1_CONTEXT_VERIFY
import secp256k1.SECP256K1_EC_UNCOMPRESSED
import secp256k1.secp256k1_context_create
import secp256k1.secp256k1_context_destroy
import secp256k1.secp256k1_ec_pubkey_serialize
import secp256k1.secp256k1_pubkey

@OptIn(ExperimentalUnsignedTypes::class)
actual open class KMMECPublicKey(val nativeValue: UByteArray) : Encodable {
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
        val basePoint = computeCurvePoint(nativeValue)
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

    companion object {
        @OptIn(ExperimentalUnsignedTypes::class)
        fun computeCurvePoint(key: UByteArray): KMMECPoint {
            val encoded = convertRepresentation(key)
            val xBytes = encoded.slice(1..32)
            val x = BigInteger.fromByteArray(xBytes.toByteArray(), Sign.POSITIVE)
            val yBytes = encoded.slice(33..64)
            val y = BigInteger.fromByteArray(yBytes.toByteArray(), Sign.POSITIVE)

            return KMMECPoint(x, y)
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

        private fun toSecpPubkey(memScope: MemScope, key: UByteArray): secp256k1_pubkey {
            val pubkey = memScope.alloc<secp256k1_pubkey>()
            for (i in 0 until ECConfig.PUBLIC_KEY_BYTE_SIZE) {
                pubkey.data[i] = key[i]
            }

            return pubkey
        }
    }
}
