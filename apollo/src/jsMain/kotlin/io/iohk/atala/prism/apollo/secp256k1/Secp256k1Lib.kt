package io.iohk.atala.prism.apollo.secp256k1

import com.ionspin.kotlin.bignum.integer.BigInteger
import io.iohk.atala.prism.apollo.utils.ECConfig
import io.iohk.atala.prism.apollo.utils.asByteArray
import io.iohk.atala.prism.apollo.utils.asUint8Array
import io.iohk.atala.prism.apollo.utils.decodeHex
import io.iohk.atala.prism.apollo.utils.external.BN
import io.iohk.atala.prism.apollo.utils.external.ec
import io.iohk.atala.prism.apollo.utils.external.secp256k1.secp256k1
import io.iohk.atala.prism.apollo.utils.toHexString
import org.kotlincrypto.hash.sha2.SHA256

/**
 * This class provides utility methods for working with a specific implementation of the secp256k1 elliptic curve cryptography.
 * It supports generating public keys, deriving private keys, signing data, verifying signatures, and compressing/uncompressing public keys.
 */
actual class Secp256k1Lib actual constructor() {
    /**
     * Creates a public key from a given private key.
     *
     * @param privateKey The private key in byte array format.
     * @param compressed A boolean indicating whether the public key should be compressed.
     * @return A byte array representing the public key.
     */
    actual fun createPublicKey(privateKey: ByteArray, compressed: Boolean): ByteArray {
        return secp256k1.getPublicKey(privateKey.asUint8Array(), compressed).asByteArray()
    }

    /**
     * Derives a new private key from an existing private key and derived bytes.
     *
     * @param privateKeyBytes The original private key in byte array format.
     * @param derivedPrivateKeyBytes The byte array used for deriving the new private key.
     * @return A byte array representing the derived private key, or null if derivation fails.
     */
    actual fun derivePrivateKey(privateKeyBytes: ByteArray, derivedPrivateKeyBytes: ByteArray): ByteArray? {
        val privKeyString = privateKeyBytes.toHexString()
        val derivedPrivKeyString = derivedPrivateKeyBytes.toHexString()
        val privKey = BigInteger.parseString(privKeyString, 16)
        val derivedPrivKey = BigInteger.parseString(derivedPrivKeyString, 16)

        val added = (privKey + derivedPrivKey) % ECConfig.n

        return added.toByteArray()
    }

    /**
     * Signs the given data using the provided private key.
     *
     * @param privateKey The private key used for signing the data.
     * @param data The data to be signed.
     * @return The signature of the data in DER format.
     */
    actual fun sign(privateKey: ByteArray, data: ByteArray): ByteArray {
        // TODO: "Using noble/secp256k1 would be problematic since it requires a configuration so it can use Sync methods to sign, also it doesnt have DER signatures")
        val sha = SHA256().digest(data)
        val ecInstance = ec("secp256k1")
        val key = ecInstance.keyFromPrivate(privateKey.asUint8Array())
        val signature = key.sign(sha.asUint8Array())
        val derSignature = signature.toDER(enc = "hex").unsafeCast<String>()
        return derSignature.decodeHex()
    }

    /**
     * Verifies the signature of the given data using the provided public key.
     *
     * @param publicKey The public key used for verification.
     * @param signature The signature to be verified.
     * @param data The data to be verified.
     * @return true if the signature is valid, false otherwise.
     */
    actual fun verify(
        publicKey: ByteArray,
        signature: ByteArray,
        data: ByteArray
    ): Boolean {
        val ecjs = ec("secp256k1")
        val sha = SHA256().digest(data)
        return ecjs.verify(sha.toHexString(), signature.toHexString(), publicKey.toHexString(), enc = "hex")
    }

    /**
     * Converts a compressed public key to an uncompressed public key.
     *
     * @param compressed The compressed public key as a byte array.
     * @return The uncompressed public key as a byte array.
     */
    @OptIn(ExperimentalUnsignedTypes::class)
    actual fun uncompressPublicKey(compressed: ByteArray): ByteArray {
        val ecjs = ec("secp256k1")

        val decoded = ecjs.curve.decodePoint(compressed.asUint8Array())

        val x = ByteArray(decoded.getX().toArray().size) { index -> decoded.getX().toArray()[index].asDynamic() as Byte }
        val y = ByteArray(decoded.getX().toArray().size) { index -> decoded.getY().toArray()[index].asDynamic() as Byte }

        val header: Byte = 0x04
        return byteArrayOf(header) + x + y
    }

    /**
     * Compresses an uncompressed public key.
     *
     * @param uncompressed The uncompressed public key to compress.
     * @return The compressed public key.
     */
    actual fun compressPublicKey(uncompressed: ByteArray): ByteArray {
        val ecjs = ec("secp256k1")
        val pubKeyBN = BN(ecjs.keyFromPublic(uncompressed.asUint8Array()).getPublic().encodeCompressed())
        return ByteArray(pubKeyBN.toArray().size) { index -> pubKeyBN.toArray()[index].asDynamic() as Byte }
    }
}
