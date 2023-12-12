package io.iohk.atala.prism.apollo

import platform.UIKit.UIDevice

/**
 * Provides information about the platform on which the application is running.
 */
public actual object Platform {
    public actual val OS: String = "${UIDevice.currentDevice.systemName()}-${UIDevice.currentDevice.systemVersion}"
}
