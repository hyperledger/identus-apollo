package io.iohk.atala.prism.apollo

import platform.UIKit.UIDevice

public actual object Platform {
    public actual val OS: String = "${UIDevice.currentDevice.systemName()}-${UIDevice.currentDevice.systemVersion}"
}
