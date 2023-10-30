package io.iohk.atala.prism.apollo.utils

actual interface RSAKeyPairGeneration {
    suspend fun generateRSAKeyPair(algorithm: RSAAsymmetricAlgorithm, hash: JsHashType, keySize: Int): KMMRSAKeyPair
    suspend fun generateRSAKeyPairFrom(seed: ByteArray, algorithm: RSAAsymmetricAlgorithm, hash: JsHashType, keySize: Int): KMMRSAKeyPair
}
