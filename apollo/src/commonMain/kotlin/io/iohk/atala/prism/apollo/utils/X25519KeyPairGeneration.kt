package io.iohk.atala.prism.apollo.utils

import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

/**
 * Interface defining the functionality for generating X25519 key pairs.
 */
@ExperimentalJsExport
@JsExport
interface X25519KeyPairGeneration {
    fun generateKeyPair(): KMMX25519KeyPair
}
