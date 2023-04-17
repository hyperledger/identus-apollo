package io.iohk.atala.prism.apollo.aes

import cocoapods.IOHKAES.BlockModeCbc
import cocoapods.IOHKAES.BlockModeCfb
import cocoapods.IOHKAES.BlockModeCfb8
import cocoapods.IOHKAES.BlockModeCtr
import cocoapods.IOHKAES.BlockModeEcb
import cocoapods.IOHKAES.BlockModeGcm
import cocoapods.IOHKAES.BlockModeOfb
import cocoapods.IOHKAES.BlockModeRc4
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
            ECB -> BlockModeEcb.toInt()
            CBC -> BlockModeCbc.toInt()
            CFB -> BlockModeCfb.toInt()
            CFB8 -> BlockModeCfb8.toInt()
            CTR -> BlockModeCtr.toInt()
            GCM -> BlockModeGcm.toInt()
            OFB -> BlockModeOfb.toInt()
            RC4 -> BlockModeRc4.toInt()
        }
    }
}
