package io.iohk.atala.prism.apollo.util

import platform.Foundation.*

@Suppress("CAST_NEVER_SUCCEEDS")
internal fun String.toUtf8NsData(): NSData? {
    val nsString = this as NSString
    val decomposedNSString = nsString.decomposedStringWithCompatibilityMapping as NSString
    return decomposedNSString.dataUsingEncoding(NSUTF8StringEncoding)!!
}
