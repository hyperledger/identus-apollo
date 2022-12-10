package io.iohk.atala.prism.apollo

import com.soywiz.krypto.SecureRandom
import io.iohk.atala.prism.apollo.AesConfig.AUTH_TAG_SIZE
import javax.crypto.Cipher
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec

/**
 * AES256-GCM Java implementation.
 *
 * Can be used with:
 *   - own key with provided IV
 *   - own key with auto-generated IV
 */

public actual object Aes {

    /**
     * Encrypt data with a key. The IV is created randomly.
     */

    public actual fun encrypt(data: ByteArray, key: SymmetricKey): AesEncryptedData {
        val iv = SecureRandom.nextBytes(AesConfig.IV_SIZE)
        return encrypt(data, key, iv)
    }

    /**
     * Encrypt data with a key and IV.
     */
    public actual fun encrypt(data: ByteArray, key: SymmetricKey, iv: ByteArray): AesEncryptedData {
        val cipher = createCipher(Cipher.ENCRYPT_MODE, key, iv)

        return AesEncryptedData(
            cipher.doFinal(data),
            iv,
            salt = null
        )
    }

    /**
     * Decrypt data with key and IV.
     */
    public actual fun decrypt(
        encryptedData: AesEncryptedData,
        key: SymmetricKey
    ): ByteArray {
        val iv = encryptedData.iv
        val cipher = createCipher(Cipher.DECRYPT_MODE, key, iv)

        return cipher.doFinal(encryptedData.data)
    }

    /**
     * Cipher initialization.
     */
    private fun createCipher(mode: Int, key: SymmetricKey, iv: ByteArray): Cipher {
        val derivedKey = SecretKeySpec(key.raw, "AES")
        val parameters = GCMParameterSpec(AUTH_TAG_SIZE, iv)
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")

        cipher.init(mode, derivedKey, parameters)
        return cipher
    }
}
