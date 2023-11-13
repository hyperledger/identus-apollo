package io.iohk.atala.prism.apollo.derivation

@OptIn(ExperimentalJsExport::class)
@JsExport
final class Mnemonic {
    companion object {
        class InvalidMnemonicCode(code: String) : RuntimeException(code)

        fun isValidMnemonicCode(code: Array<String>): Boolean {
            return MnemonicHelper.isValidMnemonicCode(code.toList())
        }

        fun createRandomMnemonics(): Array<String> {
            return MnemonicHelper.createRandomMnemonics().toTypedArray()
        }

        fun createSeed(mnemonics: Array<String>, passphrase: String = "AtalaPrism"): ByteArray {
            return MnemonicHelper.createSeed(mnemonics.toList(), passphrase)
        }

        fun createRandomSeed(passphrase: String = "AtalaPrism"): ByteArray {
            return MnemonicHelper.createRandomSeed(passphrase)
        }
    }
}
