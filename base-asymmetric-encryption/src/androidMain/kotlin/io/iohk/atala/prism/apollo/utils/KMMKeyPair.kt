package io.iohk.atala.prism.apollo.utils

import java.security.KeyPairGenerator
import java.security.SecureRandom

actual final class KMMKeyPair actual constructor(val privateKey: KMMPrivateKey, val publicKey: KMMPublicKey) {
    actual companion object : RSAKeyPairGeneration {

        override suspend fun generateRSAKeyPair(algorithm: RSAAsymmetricAlgorithm, keySize: Int): KMMKeyPair {
            val keyPairGen = KeyPairGenerator.getInstance(algorithm.nativeValue())
            keyPairGen.initialize(keySize, SecureRandom())
            val keyPair = keyPairGen.generateKeyPair()

            return KMMKeyPair(KMMPrivateKey(keyPair.private), KMMPublicKey(keyPair.public))
        }

        override suspend fun generateRSAKeyPairFrom(seed: ByteArray, algorithm: RSAAsymmetricAlgorithm, keySize: Int): KMMKeyPair {
            val keyPairGen = KeyPairGenerator.getInstance(algorithm.nativeValue())
            keyPairGen.initialize(keySize, SecureRandom(seed))
            val keyPair = keyPairGen.generateKeyPair()

            return KMMKeyPair(KMMPrivateKey(keyPair.private), KMMPublicKey(keyPair.public))
        }
    }
}
