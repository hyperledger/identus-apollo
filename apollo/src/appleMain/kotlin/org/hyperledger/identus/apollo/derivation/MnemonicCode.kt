package org.hyperledger.identus.apollo.derivation

final class MnemonicCode constructor(val words: List<String>) {
    init {
        if (words.size % 3 != 0) {
            throw MnemonicLengthException("Can't create a DID from mnemonic that is not dividable by 3")
        }
    }

    fun toMnemonic(entropy: ByteArray): List<String> {
        return MnemonicHelper.toMnemonicCode(words, entropy)
    }
}
