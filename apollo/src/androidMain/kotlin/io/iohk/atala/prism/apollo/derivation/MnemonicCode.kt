package io.iohk.atala.prism.apollo.derivation

/**
 * A class representing a Mnemonic Code.
 *
 * @param words The list of words representing the mnemonic code.
 * @throws MnemonicLengthException if the size of the words list is not divisible by 3.
 */
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
