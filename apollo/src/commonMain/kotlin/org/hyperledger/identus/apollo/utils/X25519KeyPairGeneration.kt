package org.hyperledger.identus.apollo.utils

import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

/**
 * Interface defining the functionality for generating X25519 key pairs.
 */
@ExperimentalJsExport
@JsExport
interface X25519KeyPairGeneration {
    /**
     * Generates a new X25519 key pair.
     *
     * @return the generated key pair as a [KMMX25519KeyPair] object.
     */
    fun generateKeyPair(): KMMX25519KeyPair
}
