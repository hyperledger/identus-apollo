package io.iohk.atala.prism.apollo.aes

import cocoapods.IOHKAES.AESAlgorithmAes128
import cocoapods.IOHKAES.AESAlgorithmAes192
import cocoapods.IOHKAES.AESAlgorithmAes256
import io.iohk.atala.prism.apollo.utils.NativeTypeInterface

actual enum class KAESAlgorithm : NativeTypeInterface<KAESAlgorithmNativeType> {
    AES_128,
    AES_192,
    AES_256;

    override fun nativeValue(): KAESAlgorithmNativeType {
        return when (this) {
            AES_128 -> AESAlgorithmAes128
            AES_192 -> AESAlgorithmAes192
            AES_256 -> AESAlgorithmAes256
        }
    }
}
