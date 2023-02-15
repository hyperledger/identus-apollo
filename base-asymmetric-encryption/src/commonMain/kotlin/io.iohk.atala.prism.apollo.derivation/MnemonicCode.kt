package io.iohk.atala.prism.apollo.derivation

import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

/**
 * A model representing the Mnemonic Code
 *
 * @exception MnemonicLengthException in case it is not 24 words
 */
@OptIn(ExperimentalJsExport::class)
@JsExport
data class MnemonicCode @Throws(MnemonicLengthException::class)constructor(val words: List<String>) {
    init {
        if (words.size % 3 != 0) {
            throw MnemonicLengthException("Can't create a DID from mnemonic that is not dividable by 3")
        }
    }
}
