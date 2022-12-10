package io.iohk.atala.prism.apollo.derivation

import kotlin.js.JsExport

/**
 * A model representing the Mnemonic Code used in the SDK
 * it must be 24 words
 * @exception MnemonicLengthException in case it is not 24 words
 */
@JsExport
public data class MnemonicCode @Throws(MnemonicLengthException::class) public constructor(val words: List<String>) {

    init {
        if (words.size != 24) {
            throw MnemonicLengthException("Can't create a DID from mnemonic that is not 24 words")
        }
    }
}
