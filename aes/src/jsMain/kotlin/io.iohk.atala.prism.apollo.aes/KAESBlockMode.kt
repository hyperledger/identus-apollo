package io.iohk.atala.prism.apollo.aes

import io.iohk.atala.prism.apollo.utils.NativeTypeInterface

@OptIn(ExperimentalJsExport::class)
@JsExport
actual enum class KAESBlockMode : NativeTypeInterface<KAESBlockModeNativeType> {
    ECB,
    CBC,
    CFB,
    CFB8,
    CTR,
    GCM,
    OFB,
    RC4;

    override fun nativeValue(): KAESBlockModeNativeType {
        return when (this) {
            ECB -> throw NotImplementedError()
            CBC -> "CBC"
            CFB -> throw NotImplementedError()
            CFB8 -> throw NotImplementedError()
            CTR -> "CTR"
            GCM -> "GCM"
            OFB -> throw NotImplementedError()
            RC4 -> throw NotImplementedError()
        }
    }
}
