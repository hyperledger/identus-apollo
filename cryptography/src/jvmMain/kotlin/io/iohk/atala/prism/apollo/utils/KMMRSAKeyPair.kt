package io.iohk.atala.prism.apollo.utils

import java.security.KeyPairGenerator
import java.security.SecureRandom

actual final class KMMRSAKeyPair actual constructor(val privateKey: KMMRSAPrivateKey, val publicKey: KMMRSAPublicKey) {
    actual companion object : RSAKeyPairGeneration {
        override suspend fun generateRSAKeyPair(algorithm: RSAAsymmetricAlgorithm, keySize: Int): KMMRSAKeyPair {
            val keyPairGen = KeyPairGenerator.getInstance(algorithm.nativeValue())
            keyPairGen.initialize(keySize, SecureRandom())
            val keyPair = keyPairGen.generateKeyPair()

            return KMMRSAKeyPair(KMMRSAPrivateKey(keyPair.private), KMMRSAPublicKey(keyPair.public))
        }

        override suspend fun generateRSAKeyPairFrom(seed: ByteArray, algorithm: RSAAsymmetricAlgorithm, keySize: Int): KMMRSAKeyPair {
            val keyPairGen = KeyPairGenerator.getInstance(algorithm.nativeValue())
            keyPairGen.initialize(keySize, SecureRandom(seed))
            val keyPair = keyPairGen.generateKeyPair()

            return KMMRSAKeyPair(KMMRSAPrivateKey(keyPair.private), KMMRSAPublicKey(keyPair.public))
        }
    }
}
