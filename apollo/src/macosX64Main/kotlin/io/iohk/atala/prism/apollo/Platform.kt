package io.iohk.atala.prism.apollo

import kotlin.native.Platform

actual object Platform {
    actual val OS: String
        get() = "macOS-${Platform.osFamily.name}"
}
