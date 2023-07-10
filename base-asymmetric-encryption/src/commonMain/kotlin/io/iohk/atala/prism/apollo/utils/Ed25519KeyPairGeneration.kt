package io.iohk.atala.prism.apollo.utils

import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

@ExperimentalJsExport
@JsExport
interface Ed25519KeyPairGeneration {
    fun generateKeyPair(): KMMEdKeyPair
}
