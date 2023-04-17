package io.iohk.atala.prism.apollo.aes

import io.iohk.atala.prism.apollo.utils.NativeTypeInterface

expect enum class KAESPadding : NativeTypeInterface<KAESPaddingNativeType> {
    NO_PADDING,
    PKCS5PADDING,
    PKCS7PADDING
}
