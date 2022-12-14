package io.iohk.atala.prism.apollo.aes

import io.iohk.atala.prism.apollo.utils.KMMSymmetricKey
import kotlinx.browser.window
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.await
import kotlinx.coroutines.withContext
import kotlin.js.Promise

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

    private fun browserEncrypt(data: ByteArray): Promise<ByteArray> {
        val algorithmString = "${algorithm.nativeValue()}-${blockMode.nativeValue()}"
        val nativeKey = key.nativeValue
        val iv = this.iv

        return js(
            """
                window.crypto.subtle.encrypt(
                    {
                        name: algorithmString,
                        iv: iv
                    },
                    nativeKey,
                    data
                );
            """
        ) as Promise<ByteArray>
    }

    private fun nodeEncrypt(data: ByteArray): Promise<ByteArray> {
        val algorithmString = "${algorithm.nativeValue()}-${blockMode.nativeValue()}"
        val nativeKey = key.nativeValue
        val iv = this.iv

        return js(
            """
                var crypto = require('crypto');
                crypto.subtle.encrypt(
                    {
                        name: algorithmString,
                        iv: iv
                    },
                    nativeKey,
                    data
                );
            """
        ) as Promise<ByteArray>
    }

    override suspend fun encrypt(data: ByteArray): ByteArray {
        return withContext(MainScope().coroutineContext) {
            if (jsTypeOf(window) != "undefined") {
                browserEncrypt(data).await()
            } else {
                nodeEncrypt(data).await()
            }
        }
    }

    private suspend fun browserDecrypt(data: ByteArray): Promise<ByteArray> {
        val algorithmString = "${algorithm.nativeValue()}-${blockMode.nativeValue()}"
        val nativeKey = key.nativeValue
        val iv = this.iv

        return js(
            """
                window.crypto.subtle.decrypt(
                    {
                        name: algorithmString,
                        iv: iv
                    },
                    nativeKey,
                    data
                );
            """
        ) as Promise<ByteArray>
    }

    private suspend fun nodeDecrypt(data: ByteArray): Promise<ByteArray> {
        val algorithmString = "${algorithm.nativeValue()}-${blockMode.nativeValue()}"
        val nativeKey = key.nativeValue
        val iv = this.iv

        return js(
            """
                var crypto = require('crypto');
                crypto.subtle.decrypt(
                    {
                        name: algorithmString,
                        iv: iv
                    },
                    nativeKey,
                    data
                );
            """
        ) as Promise<ByteArray>
    }

    override suspend fun decrypt(data: ByteArray): ByteArray {
        return withContext(MainScope().coroutineContext) {
            if (jsTypeOf(window) != "undefined") {
                browserDecrypt(data).await()
            } else {
                nodeDecrypt(data).await()
            }
        }
    }

    actual companion object : AESKeyGeneration {
        // Because NITS recommends it to always be 128 or bigger https://csrc.nist.gov/publications/detail/sp/800-38d/final
        private const val AUTH_TAG_SIZE = 128

        private suspend fun browserCreateRandomAESKey(algorithm: KAESAlgorithm): Promise<ByteArray> {
            return js(
                """
                    window.crypto.subtle.generateKey(
                        {
                            name: "AES-GCM",
                            length: length
                        },
                        true,
                        ["encrypt", "decrypt"]
                    );
                """
            ) as Promise<ByteArray>
        }

        private suspend fun nodeCreateRandomAESKey(algorithm: KAESAlgorithm): Promise<ByteArray> {
            return js(
                """
                    var crypto = require('crypto');
                    crypto.subtle.generateKey(
                        {
                            name: "AES-GCM",
                            length: length
                        },
                        true,
                        ["encrypt", "decrypt"]
                    );
                """
            ) as Promise<ByteArray>
        }

        override suspend fun createRandomAESKey(algorithm: KAESAlgorithm): KMMSymmetricKey {
            return withContext(MainScope().coroutineContext) {
                val key = if (jsTypeOf(window) != "undefined") {
                    browserCreateRandomAESKey(algorithm).await()
                } else {
                    nodeCreateRandomAESKey(algorithm).await()
                }
                KMMSymmetricKey(key)
            }
        }
    }
}
