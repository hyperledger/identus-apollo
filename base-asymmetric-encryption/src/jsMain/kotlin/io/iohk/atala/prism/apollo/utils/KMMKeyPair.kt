package io.iohk.atala.prism.apollo.utils

import kotlinx.coroutines.MainScope
import kotlinx.coroutines.await
import kotlinx.coroutines.promise
import web.crypto.KeyUsage
import web.crypto.RsaHashedKeyGenParams
import web.crypto.crypto

actual final class KMMKeyPair actual constructor(
    val privateKey: KMMPrivateKey,
    val publicKey: KMMPublicKey
) {
    actual companion object : RSAKeyPairGeneration {

        private suspend fun getRsaHashedKeyGenParams(algorithm: RSAAsymmetricAlgorithm, hashType: JsHashType, keySize: Int): RsaHashedKeyGenParams {
            val algorithmNativeValue = algorithm.nativeValue()
            val keySizeNativeValue = keySize
            val hashTypeNativeValue = hashType.nativeValue()
            return js("{name: algorithmNativeValue, modulusLength: keySizeNativeValue, publicExponent: new Uint8Array([1, 0, 1]), hash: hashTypeNativeValue}") as RsaHashedKeyGenParams
        }

        override suspend fun generateRSAKeyPair(algorithm: RSAAsymmetricAlgorithm, hash: JsHashType, keySize: Int): KMMKeyPair {
            return MainScope().promise {
                val keyPair = crypto.subtle.generateKey(
                    getRsaHashedKeyGenParams(algorithm, hash, keySize),
                    true,
                    arrayOf(KeyUsage.sign, KeyUsage.verify)
                ).await()
                KMMKeyPair(KMMPrivateKey(keyPair.privateKey), KMMPublicKey(keyPair.publicKey))
            }.await()
        }

        override suspend fun generateRSAKeyPairFrom(
            seed: ByteArray,
            algorithm: RSAAsymmetricAlgorithm,
            hash: JsHashType,
            keySize: Int
        ): KMMKeyPair {
            return MainScope().promise {
                val keyPair = crypto.subtle.generateKey(
                    getRsaHashedKeyGenParams(algorithm, hash, keySize),
                    true,
                    arrayOf(KeyUsage.sign, KeyUsage.verify)
                ).await()
                KMMKeyPair(KMMPrivateKey(keyPair.privateKey), KMMPublicKey(keyPair.publicKey))
            }.await()
        }
    }
}
