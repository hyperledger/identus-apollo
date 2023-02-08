package io.iohk.atala.prism.apollo.aes

import io.iohk.atala.prism.apollo.utils.NativeTypeInterface

actual enum class KAESAlgorithm : NativeTypeInterface<KAESAlgorithmNativeType> {
    AES_128,
    AES_192,
    AES_256;

    override fun nativeValue() = "AES"
}
