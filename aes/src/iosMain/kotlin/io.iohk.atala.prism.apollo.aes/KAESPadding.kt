package io.iohk.atala.prism.apollo.aes

import cocoapods.IOHKAES.PaddingNoPadding
import cocoapods.IOHKAES.PaddingPkcs7Padding
import io.iohk.atala.prism.apollo.utils.NativeTypeInterface

actual enum class KAESPadding : NativeTypeInterface<KAESPaddingNativeType> {
    No_Padding,
    PKCS5PADDING,
    PKCS7PADDING;

    override fun nativeValue(): KAESPaddingNativeType {
        return when (this) {
            No_Padding -> PaddingNoPadding
            // PKCS#5 padding and PKCS#7 padding are practically the same => https://crypto.stackexchange.com/questions/9043/what-is-the-difference-between-pkcs5-padding-and-pkcs7-padding/9044#9044
            PKCS5PADDING -> PaddingPkcs7Padding
            PKCS7PADDING -> PaddingPkcs7Padding
        }
    }
}
