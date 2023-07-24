package io.iohk.atala.prism.apollo.aes

import io.iohk.atala.prism.apollo.utils.NativeTypeInterface

@OptIn(ExperimentalJsExport::class)
@JsExport
actual enum class KAESAlgorithm : NativeTypeInterface<KAESAlgorithmNativeType> {
    AES_128,
    AES_192,
    AES_256;

    override fun nativeValue(): KAESAlgorithmNativeType {
        return when (this) {
            AES_128 -> "AES"
            AES_192 -> "AES"
            AES_256 -> "AES"
        }
    }
}
