package io.iohk.atala.prism.apollo

import com.ionspin.kotlin.bignum.integer.BigInteger
import com.ionspin.kotlin.bignum.integer.Sign
import io.iohk.atala.prism.apollo.keys.ECKeyPair
import io.iohk.atala.prism.apollo.keys.ECPoint
import io.iohk.atala.prism.apollo.keys.ECPrivateKey
import io.iohk.atala.prism.apollo.keys.ECPrivateKeyDecodingException
import io.iohk.atala.prism.apollo.keys.ECPublicKey
import io.iohk.atala.prism.apollo.signature.ECSignature
import kotlin.js.JsExport

@JsExport
public abstract class ECAbstract {
    /**
     * @return a random secp256k1 key pair
     */
    public abstract fun generateKeyPair(): ECKeyPair

    /**
     * @param point an elliptic cryptography point to test
     * @return true, if the given point belongs to the secp256k1 curve
     *         false, otherwise
     */
    public fun isSecp256k1(point: ECPoint): Boolean {
        val x = point.x.coordinate
        val y = point.y.coordinate

        // Elliptic curve equation for Secp256k1
        return ((y * y - x * x * x - ECConfig.b) mod ECConfig.p) == BigInteger.ZERO
    }

    /**
     * @param encoded a 32-length byte array
     * @return the private key represented by the given byte array
     */
    @Throws(ECPrivateKeyDecodingException::class)
    public fun toPrivateKeyFromBytes(encoded: ByteArray): ECPrivateKey {
        if (encoded.size != ECConfig.PRIVATE_KEY_BYTE_SIZE)
            throw ECPrivateKeyDecodingException("Expected encoded byte length to be ${ECConfig.PRIVATE_KEY_BYTE_SIZE}, but got ${encoded.size}")

        return toPrivateKeyFromBigInteger(BigInteger.fromByteArray(encoded, Sign.POSITIVE))
    }

    /**
     * @param d a bigint number that is less than 2^256
     * @return the private key represented by the given number
     */
    public abstract fun toPrivateKeyFromBigInteger(d: BigInteger): ECPrivateKey

    /**
     * @param encoded a 65-length byte array starting with 0x04
     * @return the public key represented by the given byte array
     */
    public fun toPublicKeyFromBytes(encoded: ByteArray): ECPublicKey {
        val expectedLength = 1 + 2 * ECConfig.PRIVATE_KEY_BYTE_SIZE
        require(encoded.size == expectedLength) {
            "Encoded byte array's expected length is $expectedLength, but got ${encoded.size} bytes"
        }
        require(encoded[0].toInt() == 0x04) {
            "First byte was expected to be 0x04, but got ${encoded[0]}"
        }

        val xBytes = encoded.copyOfRange(1, 1 + ECConfig.PRIVATE_KEY_BYTE_SIZE)
        val yBytes = encoded.copyOfRange(1 + ECConfig.PRIVATE_KEY_BYTE_SIZE, encoded.size)
        return toPublicKeyFromByteCoordinates(xBytes, yBytes)
    }

    /**
     * @param x a 32-length byte array
     * @param y a 32-length byte array
     * @return the public key represented by the given coordinates
     */
    public fun toPublicKeyFromByteCoordinates(x: ByteArray, y: ByteArray): ECPublicKey {
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
        return toPublicKeyFromBigIntegerCoordinates(xInteger, yInteger)
    }

    /**
     * @param x a bigint number that is less than 2^256
     * @param y a bigint number that is less than 2^256
     * @return the public key represented by the given coordinates
     */
    public abstract fun toPublicKeyFromBigIntegerCoordinates(x: BigInteger, y: BigInteger): ECPublicKey

    /**
     * @param privateKey secp256k1 private key
     * @return the public key associated with the given private key
     */
    public abstract fun toPublicKeyFromPrivateKey(privateKey: ECPrivateKey): ECPublicKey

    /**
     * @param compressed a 33-length byte array starting with either 0x02 or 0x03
     * @return the public key represented by the given compressed encoding
     */
    public abstract fun toPublicKeyFromCompressed(compressed: ByteArray): ECPublicKey

    /**
     * @param encoded a byte array containing a ASN.1/DER signature, must be shorter than 72 bytes
     * @return the signature represented by the given encoded byte array
     */
    public fun toSignatureFromBytes(encoded: ByteArray): ECSignature {
        require(encoded.size <= ECConfig.SIGNATURE_MAX_BYTE_SIZE) {
            "Expected signature byte length to be less than ${ECConfig.SIGNATURE_MAX_BYTE_SIZE}, but got ${encoded.size} bytes"
        }
        return ECSignature(encoded)
    }

    /**
     * @param text string to be signed
     * @param privateKey key to be used for signing
     * @return a valid ECDSA signature for the given text using the given private key
     */
    public fun signText(text: String, privateKey: ECPrivateKey): ECSignature {
        return signBytes(text.encodeToByteArray(), privateKey)
    }

    /**
     * @param data byte array to be signed
     * @param privateKey private key to be used for signing
     * @return a valid ECDSA signature for the given byte array using the given private key
     */
    public abstract fun signBytes(data: ByteArray, privateKey: ECPrivateKey): ECSignature

    /**
     * @param text string which content will be used for verification
     * @param publicKey public key to be used for verification
     * @param signature main subject of verification
     * @return true, if the given signature matches the given text with the given public key
     *         false, otherwise
     */
    public fun verifyText(text: String, publicKey: ECPublicKey, signature: ECSignature): Boolean {
        return verifyBytes(text.encodeToByteArray(), publicKey, signature)
    }

    /**
     * @param data byte array which content will be used for verification
     * @param publicKey public key to be used for verification
     * @param signature main subject of verification
     * @return true, if the given signature matches the given byte array with the given public key
     *         false, otherwise
     */
    public abstract fun verifyBytes(data: ByteArray, publicKey: ECPublicKey, signature: ECSignature): Boolean
}

public expect object EC : ECAbstract
