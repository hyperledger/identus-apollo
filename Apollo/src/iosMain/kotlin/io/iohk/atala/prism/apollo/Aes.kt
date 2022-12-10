package io.iohk.atala.prism.apollo

import com.soywiz.krypto.SecureRandom
import io.iohk.atala.prism.swift.cryptoKit.*
import kotlinx.cinterop.*
import platform.Foundation.*
import platform.posix.memcpy

/**
 * AES256-GCM iOS implementation.
 *
 * Can be used with:
 *   - own key with provided IV
 *   - own key with auto-generated IV
 */
public actual object Aes {

    public actual fun encrypt(data: ByteArray, key: SymmetricKey): AesEncryptedData {
        val iv = SecureRandom.nextBytes(AesConfig.IV_SIZE)
        return encrypt(data, key, iv)
    }

    public actual fun encrypt(
        data: ByteArray,
        key: SymmetricKey,
        iv: ByteArray
    ): AesEncryptedData {
        autoreleasepool {
            return AesEncryptedData(
                data = SwiftCryptoKit.aes256GcmEncryptWithData(
                    data.toData(),
                    key.raw.toData(),
                    iv.toData()
                ).getOrThrow().toByteArray(),
                iv = iv,
                salt = null
            )
        }
    }

    public actual fun decrypt(
        encryptedData: AesEncryptedData,
        key: SymmetricKey
    ): ByteArray {
        autoreleasepool {
            return SwiftCryptoKit.aes256GcmDecryptWithData(
                data = encryptedData.data.toData(),
                key = key.raw.toData(),
                iv = encryptedData.iv.toData()
            ).getOrThrow().toByteArray()
        }
    }

    private inline fun ByteArray.toData(): NSData {
        val pinned = pin()
        return NSData.create(pinned.addressOf(0), size.toULong()) { _, _ -> pinned.unpin() }
    }

    private fun NSData.toByteArray(): ByteArray {
        return ByteArray(length.toInt()).apply {
            usePinned { memcpy(it.addressOf(0), bytes, length) }
        }
    }

    private fun DataWithError.getOrThrow(): NSData {
        this.failure()?.let { throw RuntimeException(it.description ?: "Unknown CryptoKit error.") }
        return success() ?: error("Invalid CryptoKit result.")
    }
}
