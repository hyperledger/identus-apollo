package io.iohk.atala.prism.apollo.secp256k1

expect class Secp256k1Lib constructor() {
    fun createPublicKey(privateKey: ByteArray, compressed: Boolean): ByteArray

    fun derivePrivateKey(privateKeyBytes: ByteArray, derivedPrivateKeyBytes: ByteArray): ByteArray?

    fun sign(privateKey: ByteArray, data: ByteArray): ByteArray

    fun verify(publicKey: ByteArray, signature: ByteArray, data: ByteArray): Boolean

    fun uncompressPublicKey(compressed: ByteArray): ByteArray

    fun compressPublicKey(uncompressed: ByteArray): ByteArray
}
