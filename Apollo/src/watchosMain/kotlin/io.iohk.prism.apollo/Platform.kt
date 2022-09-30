package io.iohk.prism.apollo

import platform.WatchKit.WKInterfaceDevice

actual object Platform {
    actual val OS: String
        get() {
            val wkInterfaceDevice = WKInterfaceDevice.currentDevice()
            return "${wkInterfaceDevice.systemName}-${wkInterfaceDevice.systemVersion}"
        }
}