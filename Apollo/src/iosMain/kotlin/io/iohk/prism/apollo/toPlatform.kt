package io.iohk.prism.apollo

import kotlinx.cinterop.*
import platform.Foundation.*
import platform.posix.memcpy

@OptIn(UnsafeNumber::class)
fun ByteArray.toNSData(): NSData = memScoped {
    NSData.create(
        bytes = allocArrayOf(this@toNSData),
        length = this@toNSData.size.toULong()
    )
}

@OptIn(UnsafeNumber::class)
fun NSData.toByteArray(): ByteArray = ByteArray(this@toByteArray.length.toInt()).apply {
    if (this@toByteArray.length > 0U) {
        usePinned {
            memcpy(it.addressOf(0), this@toByteArray.bytes, this@toByteArray.length)
        }
    }
}

fun Int.toNSNumber(): NSNumber = memScoped {
    return NSNumber.numberWithInt(this@toNSNumber)
}

fun NSNumber.toKotlinInt(): Int {
    return this.intValue
}

fun String.toNSString(): NSString = memScoped {
    return NSString.create(string = this@toNSString)
}

fun NSString.toKotlinString(): String {
    return this.toString()
}
