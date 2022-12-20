package io.iohk.atala.prism.apollo.aes

import io.iohk.atala.prism.apollo.utils.NativeTypeInterface

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
            ECB -> "ECB"
            CBC -> "CBC"
            CFB -> "CFB"
            CFB8 -> "CFB8"
            CTR -> "CTR"
            GCM -> "GCM"
            OFB -> "OFB"
            RC4 -> throw Exception("Not implemented in JVM")
        }
    }
}
