package io.iohk.atala.prism.apollo.aes

import io.iohk.atala.prism.apollo.utils.KMMSymmetricKey
import java.security.spec.AlgorithmParameterSpec
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.IvParameterSpec

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

    private val cipher: Cipher = Cipher.getInstance("${algorithm.nativeValue()}/${blockMode.nativeValue()}/${padding.nativeValue()}")
    private val ivSpec: AlgorithmParameterSpec? = if (blockMode.needIV()) {
        when (blockMode) {
            KAESBlockMode.ECB -> IvParameterSpec(iv)
            KAESBlockMode.CBC -> IvParameterSpec(iv)
            KAESBlockMode.CFB -> IvParameterSpec(iv)
            KAESBlockMode.CFB8 -> IvParameterSpec(iv)
            KAESBlockMode.CTR -> IvParameterSpec(iv)
            KAESBlockMode.GCM -> GCMParameterSpec(AUTH_TAG_SIZE, iv)
            KAESBlockMode.OFB -> IvParameterSpec(iv)
            KAESBlockMode.RC4 -> IvParameterSpec(iv)
        }
    } else {
        null
    }

    override suspend fun encrypt(data: ByteArray): ByteArray {
        if (ivSpec == null) {
            cipher.init(Cipher.ENCRYPT_MODE, key.nativeType)
        } else {
            cipher.init(Cipher.ENCRYPT_MODE, key.nativeType, ivSpec)
        }
        return cipher.doFinal(data)
    }

    override suspend fun decrypt(data: ByteArray): ByteArray {
        if (ivSpec == null) {
            cipher.init(Cipher.DECRYPT_MODE, key.nativeType)
        } else {
            cipher.init(Cipher.DECRYPT_MODE, key.nativeType, ivSpec)
        }
        return cipher.doFinal(data)
    }

    actual companion object : AESKeyGeneration {
        private const val AUTH_TAG_SIZE = 128

        override suspend fun createRandomAESKey(algorithm: KAESAlgorithm, blockMode: KAESBlockMode): KMMSymmetricKey {
            val keygen = KeyGenerator.getInstance("AES")
            keygen.init(algorithm.keySize())
            return KMMSymmetricKey(keygen.generateKey())
        }
    }
}
