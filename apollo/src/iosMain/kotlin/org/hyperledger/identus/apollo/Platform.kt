package org.hyperledger.identus.apollo

import platform.UIKit.UIDevice

/**
 * Provides information about the platform on which the application is running.
 */
public actual object Platform {
    /**
     * Represents the operating system of the current device.
     *
     * This property is platform-specific and its value depends on the device's operating system.
     * For iOS devices, the value is a concatenation of the system name and version provided by the `UIDevice` class.
     *
     * Example usage:
     * ```kotlin
     * val platformName = OS
     * println("Current OS: $platformName")
     * ```
     *
     * @see UIDevice
     */
    public actual val OS: String = "${UIDevice.currentDevice.systemName()}-${UIDevice.currentDevice.systemVersion}"
}