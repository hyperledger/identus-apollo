package io.iohk.atala.prism.apollo

import kotlin.experimental.ExperimentalNativeApi
import kotlin.native.Platform

/**
 * Provides information about the platform on which the application is running.
 */
actual object Platform {
    @OptIn(ExperimentalNativeApi::class)
    actual val OS: String
        get() = "macOS-${Platform.osFamily.name}"
}
