package io.iohk.atala.prism.apollo.utils

import kotlinx.coroutines.MainScope
import kotlinx.coroutines.await
import kotlinx.coroutines.promise
import web.crypto.KeyUsage
import web.crypto.RsaHashedKeyGenParams
import web.crypto.crypto

actual final class KMMRSAKeyPair actual constructor(
    val privateKey: KMMRSAPrivateKey,
    val publicKey: KMMRSAPublicKey
) {
    actual companion object : RSAKeyPairGeneration {
        private fun getRsaHashedKeyGenParams(algorithm: RSAAsymmetricAlgorithm, hashType: JsHashType, keySize: Int): RsaHashedKeyGenParams {
            val algorithmNativeValue = algorithm.nativeValue()
            val keySizeNativeValue = keySize
            val hashTypeNativeValue = hashType.nativeValue()
            return js(
                "{name: algorithmNativeValue, modulusLength: keySizeNativeValue, publicExponent: new Uint8Array([1, 0, 1]), hash: hashTypeNativeValue}"
            ).unsafeCast<RsaHashedKeyGenParams>()
        }

        override suspend fun generateRSAKeyPair(algorithm: RSAAsymmetricAlgorithm, hash: JsHashType, keySize: Int): KMMRSAKeyPair {
            return MainScope().promise {
                val keyPair =
                    crypto.subtle.generateKey(
                        getRsaHashedKeyGenParams(algorithm, hash, keySize),
                        true,
                        arrayOf(KeyUsage.sign, KeyUsage.verify)
                    ).await()
                KMMRSAKeyPair(KMMRSAPrivateKey(keyPair.privateKey), KMMRSAPublicKey(keyPair.publicKey))
            }.await()
        }

        override suspend fun generateRSAKeyPairFrom(seed: ByteArray, algorithm: RSAAsymmetricAlgorithm, hash: JsHashType, keySize: Int): KMMRSAKeyPair {
            return MainScope().promise {
                val keyPair =
                    crypto.subtle.generateKey(
                        getRsaHashedKeyGenParams(algorithm, hash, keySize),
                        true,
                        arrayOf(KeyUsage.sign, KeyUsage.verify)
                    ).await()
                KMMRSAKeyPair(KMMRSAPrivateKey(keyPair.privateKey), KMMRSAPublicKey(keyPair.publicKey))
            }.await()
        }
    }
}
