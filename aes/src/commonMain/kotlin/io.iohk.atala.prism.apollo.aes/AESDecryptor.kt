package io.iohk.atala.prism.apollo.aes

interface AESDecryptor {
    suspend fun decrypt(data: ByteArray): ByteArray
}
