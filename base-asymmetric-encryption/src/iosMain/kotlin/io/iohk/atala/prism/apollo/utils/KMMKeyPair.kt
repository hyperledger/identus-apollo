package io.iohk.atala.prism.apollo.utils

import cocoapods.IOHKRSA.IOHKRSA
import platform.Security.SecKeyRef

actual final class KMMKeyPair actual constructor(val privateKey: KMMPrivateKey, val publicKey: KMMPublicKey) {

    private constructor(nativePrivateKey: SecKeyRef, nativePublicKey: SecKeyRef) : this(KMMPrivateKey(nativePrivateKey), KMMPublicKey(nativePublicKey))

    actual companion object : RSAKeyPairGeneration {
        override suspend fun generateRSAKeyPair(algorithm: RSAAsymmetricAlgorithm, keySize: Int): KMMKeyPair {
            val pair = IOHKRSA.generateKeyPairWithKeySize(keySize.toLong())!!
            return KMMKeyPair(pair.privateKey()!!, pair.publicKey()!!)
        }

        override suspend fun generateRSAKeyPairFrom(
            seed: ByteArray,
            algorithm: RSAAsymmetricAlgorithm,
            keySize: Int
        ): KMMKeyPair {
            val pair = IOHKRSA.generateKeyPairWithKeySize(keySize.toLong())!!
            return KMMKeyPair(pair.privateKey()!!, pair.publicKey()!!)
        }
    }
}
