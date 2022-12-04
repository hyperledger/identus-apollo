package io.iohk.atala.prism.apollo

actual object Platform {
    actual val OS: String
        get() {
            val wkInterfaceDevice = WKInterfaceDevice.currentDevice()
            return "${wkInterfaceDevice.systemName}-${wkInterfaceDevice.systemVersion}"
        }
}