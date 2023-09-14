package io.iohk.atala.prism.apollo.securerandom

import kotlinx.cinterop.UnsafeNumber
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.usePinned
import platform.Foundation.NSData
import platform.Foundation.NSNumber
import platform.Foundation.numberWithInt
import platform.posix.memcpy
import swift.secureRandomGeneration.IOHKSecureRandomGeneration

actual class SecureRandom actual constructor(
    actual val seed: ByteArray
) : SecureRandomInterface {

    @OptIn(UnsafeNumber::class)
    override fun nextBytes(size: Int): ByteArray {
        return IOHKSecureRandomGeneration.randomDataWithLength(size.toNSNumber().integerValue).toByteArray()
    }

    actual companion object : SecureRandomStaticInterface {
        @OptIn(UnsafeNumber::class)
        override fun generateSeed(numBytes: Int): ByteArray {
            return IOHKSecureRandomGeneration.randomDataWithLength(numBytes.toNSNumber().integerValue).toByteArray()
        }
    }
}

@OptIn(UnsafeNumber::class)
private fun NSData.toByteArray(): ByteArray = ByteArray(this@toByteArray.length.toInt()).apply {
    if (this@toByteArray.length > 0U) {
        usePinned {
            memcpy(it.addressOf(0), this@toByteArray.bytes, this@toByteArray.length)
        }
    }
}

private fun Int.toNSNumber(): NSNumber = memScoped {
    return NSNumber.numberWithInt(this@toNSNumber)
}
