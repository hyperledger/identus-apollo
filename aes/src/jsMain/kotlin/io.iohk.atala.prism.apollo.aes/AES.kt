package io.iohk.atala.prism.apollo.aes

import io.iohk.atala.prism.apollo.utils.KMMSymmetricKey
import io.iohk.atala.prism.apollo.utils.toArrayBuffer
import io.iohk.atala.prism.apollo.utils.toByteArray
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.await
import kotlinx.coroutines.promise
import web.crypto.AesCbcParams
import web.crypto.AesGcmParams
import web.crypto.AesKeyAlgorithm
import web.crypto.AesKeyGenParams
import web.crypto.Algorithm
import web.crypto.KeyFormat
import web.crypto.KeyUsage
import web.crypto.crypto

actual typealias KAESAlgorithmNativeType = String
actual typealias KAESBlockModeNativeType = String
actual typealias KAESPaddingNativeType = String

actual final class AES actual constructor(
    actual val algorithm: KAESAlgorithm,
    actual val blockMode: KAESBlockMode,
    actual val padding: KAESPadding,
    actual val key: KMMSymmetricKey,
    actual val iv: ByteArray?
) : AESEncryptor, AESDecryptor {

    private fun getAesKeyAlgorithm(): AesKeyAlgorithm {
        val algorithmString = "${algorithm.nativeValue()}-${blockMode.nativeValue()}"
        val length = this.algorithm.keySize()
        return js("{name: algorithmString, length: length}").unsafeCast<AesKeyAlgorithm>()
    }

    private fun getAesParams(): Algorithm {
        val algorithmString = "${algorithm.nativeValue()}-${blockMode.nativeValue()}"
        return if (blockMode.needIV()) {
            val jsIV = this.iv!!.toArrayBuffer()
            when (blockMode) {
                KAESBlockMode.GCM -> {
                    val tagSize = GCM_AUTH_TAG_SIZE
                    js("{name: algorithmString, iv: jsIV, tagLength: tagSize}").unsafeCast<AesGcmParams>()
                }
                KAESBlockMode.CBC -> {
                    js("{name: algorithmString, iv: jsIV}").unsafeCast<AesCbcParams>()
                }
                else -> {
                    throw NotImplementedError("Yet to be implemented")
                }
            }
        } else {
            js("{name: algorithmString}").unsafeCast<Algorithm>()
        }
    }

    override suspend fun encrypt(data: ByteArray): ByteArray {
        return MainScope().promise {
            val nativeKey = key.nativeValue

            val cryptoKey = crypto.subtle.importKey(
                KeyFormat.raw,
                nativeKey.toArrayBuffer(),
                getAesKeyAlgorithm(),
                true,
                arrayOf(KeyUsage.encrypt, KeyUsage.decrypt)
            ).await()

            crypto.subtle.encrypt(
                getAesParams(),
                cryptoKey,
                data.toArrayBuffer()
            ).await().toByteArray()
        }.await()
    }

    override suspend fun decrypt(data: ByteArray): ByteArray {
        return MainScope().promise {
            val nativeKey = key.nativeValue

            val cryptoKey = crypto.subtle.importKey(
                KeyFormat.raw,
                nativeKey.toArrayBuffer(),
                getAesKeyAlgorithm(),
                true,
                arrayOf(KeyUsage.encrypt, KeyUsage.decrypt)
            ).await()

            crypto.subtle.decrypt(
                getAesParams(),
                cryptoKey,
                data.toArrayBuffer()
            ).await().toByteArray()
        }.await()
    }

    actual companion object : AESKeyGeneration {
        // Because NITS recommends it to always be 128 or bigger https://csrc.nist.gov/publications/detail/sp/800-38d/final
        private const val GCM_AUTH_TAG_SIZE = 128

        private fun getAesKeyGenParams(algorithm: KAESAlgorithm, blockMode: KAESBlockMode): AesKeyGenParams {
            val algorithmString = "${algorithm.nativeValue()}-${blockMode.nativeValue()}"
            val keyLength = algorithm.keySize()
            return js("{name: algorithmString, length: keyLength}").unsafeCast<AesKeyGenParams>()
        }

        override suspend fun createRandomAESKey(algorithm: KAESAlgorithm, blockMode: KAESBlockMode): KMMSymmetricKey {
            return MainScope().promise {
                val cryptoKey = crypto.subtle.generateKey(
                    getAesKeyGenParams(algorithm, blockMode),
                    true,
                    arrayOf(KeyUsage.encrypt, KeyUsage.decrypt)
                ).await()
                val key = crypto.subtle.exportKey(KeyFormat.raw, cryptoKey).await().toByteArray()
                KMMSymmetricKey(key)
            }.await()
        }
    }
}
