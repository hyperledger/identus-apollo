package io.iohk.atala.prism.apollo.aes

import io.iohk.atala.prism.apollo.utils.NativeTypeInterface

@OptIn(ExperimentalJsExport::class)
@JsExport
actual enum class KAESPadding : NativeTypeInterface<KAESPaddingNativeType> {
    NO_PADDING,
    PKCS5PADDING,
    PKCS7PADDING;

    override fun nativeValue(): KAESPaddingNativeType {
        return when (this) {
            NO_PADDING -> ""
            PKCS5PADDING -> ""
            PKCS7PADDING -> ""
        }
    }
}
