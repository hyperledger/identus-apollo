package io.iohk.atala.prism.apollo.utils

import io.iohk.atala.prism.apollo.base64.base64PadDecodedBytes
import io.iohk.atala.prism.apollo.base64.base64PadEncoded
import platform.Foundation.NSData

actual open class KMMSymmetricKey(val nativeValue: NSData) : SymmetricKeyBase64Export {
    override fun exportToBase64(): String {
        return nativeValue.toByteArray().base64PadEncoded
    }

    actual companion object : SymmetricKeyBase64Import, IVBase64Import, IVBase64Export, IVGeneration {
        override fun createKeyFromBase64(base64Encoded: String, algorithm: SymmetricKeyType): KMMSymmetricKey {
            return KMMSymmetricKey(base64Encoded.base64PadDecodedBytes.toNSData())
        }
    }
}