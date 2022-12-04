package io.iohk.atala.prism.apollo

actual object Platform {
    actual val OS: String = "${UIDevice.currentDevice.systemName()}-${UIDevice.currentDevice.systemVersion}"
}
