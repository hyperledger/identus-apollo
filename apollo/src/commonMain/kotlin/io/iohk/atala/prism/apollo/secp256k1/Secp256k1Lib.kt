package io.iohk.atala.prism.apollo.secp256k1

/**
 * This class provides various Secp256k1 cryptographic functionalities such as creating public keys, signing data,
 * verifying signatures, and compressing or decompressing public keys.
 */
expect class Secp256k1Lib constructor() {
    /**
     * Creates a public key from a given private key.
     *
     * @param privateKey The private key in byte array format.
     * @param compressed A boolean indicating whether the public key should be compressed.
     * @return A byte array representing the public key.
     */
    fun createPublicKey(privateKey: ByteArray, compressed: Boolean): ByteArray

    /**
     * Derives a new private key from an existing private key and derived bytes.
     *
     * @param privateKeyBytes The original private key in byte array format.
     * @param derivedPrivateKeyBytes The byte array used for deriving the new private key.
     * @return A byte array representing the derived private key, or null if derivation fails.
     */
    fun derivePrivateKey(privateKeyBytes: ByteArray, derivedPrivateKeyBytes: ByteArray): ByteArray?

    /**
     * Signs data using a given private key.
     *
     * @param privateKey The private key used for signing, in byte array format.
     * @param data The data to be signed, in byte array format.
     * @return A byte array representing the signature.
     */
    fun sign(privateKey: ByteArray, data: ByteArray): ByteArray

    /**
     * Verifies a signature against a public key and data.
     *
     * @param publicKey The public key in byte array format.
     * @param signature The signature to be verified, in byte array format.
     * @param data The data against which the signature will be verified, in byte array format.
     * @return A boolean indicating whether the signature is valid.
     */
    fun verify(publicKey: ByteArray, signature: ByteArray, data: ByteArray): Boolean

    /**
     * Decompresses a compressed public key.
     *
     * @param compressed The compressed public key in byte array format.
     * @return A byte array representing the uncompressed public key.
     */
    fun uncompressPublicKey(compressed: ByteArray): ByteArray

    /**
     * Compresses an uncompressed public key.
     *
     * @param uncompressed The uncompressed public key in byte array format.
     * @return A byte array representing the compressed public key.
     */
    fun compressPublicKey(uncompressed: ByteArray): ByteArray
}
