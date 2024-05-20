package org.hyperledger.identus.apollo

import kotlin.experimental.ExperimentalNativeApi
import kotlin.native.Platform

/**
 * Provides information about the platform on which the application is running.
 */
actual object Platform {
    /**
     * Represents the operating system on which the application is running.
     */
    @OptIn(ExperimentalNativeApi::class)
    actual val OS: String
        get() = "macOS-${Platform.osFamily.name}"
}
