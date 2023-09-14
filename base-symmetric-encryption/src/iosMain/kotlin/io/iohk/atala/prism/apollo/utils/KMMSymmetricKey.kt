package io.iohk.atala.prism.apollo.utils

import io.iohk.atala.prism.apollo.base64.base64PadDecodedBytes
import io.iohk.atala.prism.apollo.base64.base64PadEncoded
import kotlinx.cinterop.UnsafeNumber
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.allocArrayOf
import kotlinx.cinterop.convert
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.usePinned
import platform.Foundation.NSData
import platform.Foundation.create
import platform.darwin.NSUInteger
import platform.posix.memcpy

actual final class KMMSymmetricKey(val nativeValue: NSData) : SymmetricKeyBase64Export {
    override fun exportToBase64(): String {
        return nativeValue.toByteArray().base64PadEncoded
    }

    actual companion object : SymmetricKeyBase64Import, IVBase64Import, IVBase64Export, IVGeneration {
        override fun createKeyFromBase64(base64Encoded: String, algorithm: SymmetricKeyType): KMMSymmetricKey {
            return KMMSymmetricKey(base64Encoded.base64PadDecodedBytes.toNSData())
        }
    }
}

@OptIn(UnsafeNumber::class)
private fun ByteArray.toNSData(): NSData = memScoped {
    NSData.create(
        bytes = allocArrayOf(this@toNSData),
        length = this@toNSData.size.convert<NSUInteger>()
    )
}

@OptIn(UnsafeNumber::class)
private fun NSData.toByteArray(): ByteArray = ByteArray(this@toByteArray.length.toInt()).apply {
    if (this@toByteArray.length > 0U) {
        usePinned {
            memcpy(it.addressOf(0), this@toByteArray.bytes, this@toByteArray.length)
        }
    }
}
