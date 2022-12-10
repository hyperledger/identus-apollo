package io.iohk.atala.prism.apollo

import kotlin.js.JsExport

/**
 * Encrypted AES256-GCM data.
 *
 * @param data Encrypted data
 * @param iv Initialization vector
 * @param salt Salt used to derive key from password string
 */
@JsExport
public data class AesEncryptedData(val data: ByteArray, val iv: ByteArray, val salt: ByteArray?) {
    public fun combined(): ByteArray = (data + iv)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as AesEncryptedData

        if (!data.contentEquals(other.data)) return false
        if (!iv.contentEquals(other.iv)) return false
        if (salt != null) {
            if (other.salt == null) return false
            if (!salt.contentEquals(other.salt)) return false
        } else if (other.salt != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = data.contentHashCode()
        result = 31 * result + iv.contentHashCode()
        result = 31 * result + (salt?.contentHashCode() ?: 0)
        return result
    }

    public companion object {
        public fun fromCombined(bytes: ByteArray): AesEncryptedData {
            return AesEncryptedData(
                data = bytes.takeLast(bytes.size - AesConfig.IV_SIZE)
                    .toByteArray(),
                iv = bytes.take(AesConfig.IV_SIZE).toByteArray(),
                salt = null
            )
        }
    }
}

/**
 * AES default parameters.
 */

internal object AesConfig {
    const val IV_SIZE = 64 // size in bits
    const val KEY_SIZE = 256 // size in bits
    const val AUTH_TAG_SIZE = 128 // size in bits
}

/**
 * AES256-GCM facade.
 *
 * Can be used with:
 *   - own key with provided IV
 *   - own key with auto-generated IV
 */

public expect object Aes {

    /**
     * Encrypt data with a key. The IV is created randomly.
     */
    public fun encrypt(data: ByteArray, key: SymmetricKey): AesEncryptedData

    /**
     * Encrypt data with a key and IV.
     */
    public fun encrypt(data: ByteArray, key: SymmetricKey, iv: ByteArray): AesEncryptedData

    /**
     * Decrypt data with a key and IV.
     */
    public fun decrypt(encryptedData: AesEncryptedData, key: SymmetricKey): ByteArray
}
