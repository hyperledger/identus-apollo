package io.iohk.atala.prism.apollo.aes

interface AESEncryptor {
    suspend fun encrypt(data: ByteArray): ByteArray
}
