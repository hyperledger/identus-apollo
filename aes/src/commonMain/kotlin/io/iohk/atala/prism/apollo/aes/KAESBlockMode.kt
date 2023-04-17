package io.iohk.atala.prism.apollo.aes

import io.iohk.atala.prism.apollo.utils.NativeTypeInterface

expect enum class KAESBlockMode : NativeTypeInterface<KAESBlockModeNativeType> {
    ECB,
    CBC,
    CFB,
    CFB8,
    CTR,
    GCM,
    OFB,
    RC4
}

fun KAESBlockMode.needIV(): Boolean {
    return when (this) {
        KAESBlockMode.ECB -> false
        KAESBlockMode.CBC, KAESBlockMode.CFB, KAESBlockMode.CFB8, KAESBlockMode.CTR, KAESBlockMode.GCM,
        KAESBlockMode.OFB, KAESBlockMode.RC4 -> true
        else -> throw IllegalStateException("Should never reach this line")
    }
}
