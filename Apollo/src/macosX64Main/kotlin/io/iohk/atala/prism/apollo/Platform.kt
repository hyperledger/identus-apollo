package io.iohk.atala.prism.apollo

actual object Platform {
    actual val OS: String
        get() {
            val processInfo = NSProcessInfo.processInfo()
            return "${processInfo.operatingSystemName()}-${processInfo.operatingSystemVersionString()}"
        }
}