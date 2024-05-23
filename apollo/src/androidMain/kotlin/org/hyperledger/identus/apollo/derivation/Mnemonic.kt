package io.iohk.atala.prism.apollo.derivation

/**
 * The Mnemonic class provides utility methods for working with mnemonic codes.
 */
final class Mnemonic {
    companion object {
        class InvalidMnemonicCode(code: String) : RuntimeException(code)

        fun isValidMnemonicCode(code: List<String>): Boolean {
            return MnemonicHelper.isValidMnemonicCode(code)
        }

        fun createRandomMnemonics(): List<String> {
            return MnemonicHelper.createRandomMnemonics()
        }

        fun createSeed(mnemonics: List<String>, passphrase: String = "AtalaPrism"): ByteArray {
            return MnemonicHelper.createSeed(mnemonics.toList(), passphrase)
        }

        fun createRandomSeed(passphrase: String = "AtalaPrism"): ByteArray {
            return MnemonicHelper.createRandomSeed(passphrase)
        }
    }
}
