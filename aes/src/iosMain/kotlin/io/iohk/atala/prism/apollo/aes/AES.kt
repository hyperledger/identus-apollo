package io.iohk.atala.prism.apollo.aes

import cocoapods.IOHKAES.AESOptionsNone
import cocoapods.IOHKAES.AESOptionsPkcs7Padding
import cocoapods.IOHKAES.IOHKAES
import io.iohk.atala.prism.apollo.utils.KMMSymmetricKey
import io.iohk.atala.prism.apollo.utils.toByteArray
import io.iohk.atala.prism.apollo.utils.toNSData
import kotlinx.cinterop.autoreleasepool
import platform.Foundation.NSData

actual typealias KAESAlgorithmNativeType = Long
actual typealias KAESBlockModeNativeType = Int
actual typealias KAESPaddingNativeType = Long

actual final class AES actual constructor(
    actual val algorithm: KAESAlgorithm,
    actual val blockMode: KAESBlockMode,
    actual val padding: KAESPadding,
    actual val key: KMMSymmetricKey,
    actual val iv: ByteArray?
) : AESEncryptor, AESDecryptor {
    override suspend fun encrypt(data: ByteArray): ByteArray {
        autoreleasepool {
            val encryptedData = IOHKAES.aesEncryptionWithAlgorithm(
                algorithm.nativeValue(),
                AESOptionsPkcs7Padding,
                blockMode.nativeValue().toUInt(),
                padding.nativeValue(),
                data.toNSData(),
                key.nativeValue,
                iv?.toNSData() ?: NSData()
            )?.toByteArray()

            return encryptedData ?: ByteArray(0)
        }
    }

    override suspend fun decrypt(data: ByteArray): ByteArray {
        autoreleasepool {
            val decryptedData = IOHKAES.aesDecryptionWithAlgorithm(
                algorithm.nativeValue(),
                AESOptionsNone,
                blockMode.nativeValue().toUInt(),
                padding.nativeValue(),
                data.toNSData(),
                key.nativeValue,
                iv?.toNSData() ?: NSData()
            )?.toByteArray()

            return decryptedData ?: ByteArray(0)
        }
    }

    actual companion object : AESKeyGeneration {
        override suspend fun createRandomAESKey(algorithm: KAESAlgorithm, blockMode: KAESBlockMode): KMMSymmetricKey {
            autoreleasepool {
                return KMMSymmetricKey(
                    IOHKAES.generateAESKeyWithAlgorithm(algorithm.nativeValue())
                )
            }
        }
    }
}
