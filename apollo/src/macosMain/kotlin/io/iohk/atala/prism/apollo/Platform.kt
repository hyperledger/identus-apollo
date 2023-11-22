package io.iohk.atala.prism.apollo

import kotlin.experimental.ExperimentalNativeApi
import kotlin.native.Platform

@OptIn(ExperimentalNativeApi::class)
actual object Platform {
    actual val OS: String
        get() = "macOS-${Platform.osFamily.name}"
}
