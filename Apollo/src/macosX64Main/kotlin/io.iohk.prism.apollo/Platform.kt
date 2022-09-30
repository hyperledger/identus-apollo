package io.iohk.prism.apollo

import platform.Foundation.NSProcessInfo

actual object Platform {
    actual val OS: String
        get() {
            val processInfo = NSProcessInfo.processInfo()
            return "${processInfo.operatingSystemName()}-${processInfo.operatingSystemVersionString()}"
        }
}