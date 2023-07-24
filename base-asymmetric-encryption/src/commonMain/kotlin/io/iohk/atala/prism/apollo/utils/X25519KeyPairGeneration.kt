package io.iohk.atala.prism.apollo.utils

import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

@ExperimentalJsExport
@JsExport
interface X25519KeyPairGeneration {
    fun generateKeyPair(): KMMX25519KeyPair
}
