package io.iohk.atala.prism.apollo.aes

import io.iohk.atala.prism.apollo.utils.KMMSymmetricKey

expect class KAESAlgorithmNativeType
expect class KAESBlockModeNativeType
expect class KAESPaddingNativeType

expect final class AES(
    algorithm: KAESAlgorithm,
    blockMode: KAESBlockMode,
    padding: KAESPadding,
    key: KMMSymmetricKey,
    iv: ByteArray?
) : AESEncryptor, AESDecryptor {
    val algorithm: KAESAlgorithm
    val blockMode: KAESBlockMode
    val padding: KAESPadding
    val key: KMMSymmetricKey
    val iv: ByteArray?

    companion object : AESKeyGeneration
}
