package io.iohk.prism.apollo

import platform.UIKit.UIDevice

actual object Platform {
    actual val OS: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
}