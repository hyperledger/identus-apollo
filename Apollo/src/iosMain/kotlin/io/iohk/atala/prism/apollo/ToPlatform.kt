package io.iohk.atala.prism.apollo

import kotlinx.cinterop.UnsafeNumber
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.allocArrayOf
import kotlinx.cinterop.convert
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.usePinned
import platform.Foundation.NSData
import platform.Foundation.NSNumber
import platform.Foundation.NSString
import platform.Foundation.create
import platform.Foundation.numberWithInt
import platform.darwin.NSUInteger
import platform.posix.memcpy

@OptIn(UnsafeNumber::class)
public fun ByteArray.toNSData(): NSData = memScoped {
    NSData.create(
        bytes = allocArrayOf(this@toNSData),
        length = this@toNSData.size.convert<NSUInteger>()
    )
}

@OptIn(UnsafeNumber::class)
public fun NSData.toByteArray(): ByteArray = ByteArray(this@toByteArray.length.toInt()).apply {
    if (this@toByteArray.length > 0U) {
        usePinned {
            memcpy(it.addressOf(0), this@toByteArray.bytes, this@toByteArray.length)
        }
    }
}

public fun Int.toNSNumber(): NSNumber = memScoped {
    return NSNumber.numberWithInt(this@toNSNumber)
}

public fun NSNumber.toKotlinInt(): Int {
    return this.intValue
}

public fun String.toNSString(): NSString = memScoped {
    return NSString.create(string = this@toNSString)
}

public fun NSString.toKotlinString(): String {
    return this.toString()
}
