package io.iohk.atala.prism.apollo.aes

import io.iohk.atala.prism.apollo.utils.NativeTypeInterface

actual enum class KAESPadding : NativeTypeInterface<KAESPaddingNativeType> {
    No_Padding,
    PKCS5PADDING,
    PKCS7PADDING;

    override fun nativeValue(): KAESPaddingNativeType {
        return when (this) {
            No_Padding -> "NoPadding"
            PKCS5PADDING -> "PKCS5Padding"
            PKCS7PADDING -> "PKCS5Padding"
        }
    }
}
