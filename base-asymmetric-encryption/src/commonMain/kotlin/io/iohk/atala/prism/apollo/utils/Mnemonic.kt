package io.iohk.atala.prism.apollo.utils

import io.iohk.atala.prism.apollo.derivation.MnemonicCodeEnglish
import io.iohk.atala.prism.apollo.hashing.PBKDF2SHA512
import io.iohk.atala.prism.apollo.securerandom.SecureRandom
import io.iohk.atala.prism.apollo.utils.bip39.MnemonicCode
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

@OptIn(ExperimentalJsExport::class)
@JsExport
final class Mnemonic {

    companion object {
        private const val SEED_ENTROPY_BITS_24_WORDS = 256

        private const val PBKDF2C = 2048
        private const val PBKDF2DKLen = 64
        class InvalidMnemonicCode(code: String) : RuntimeException(code)

        fun isValidMnemonicCode(code: Array<String>): Boolean {
            return code.all { it in MnemonicCodeEnglish.wordList }
        }

        fun createRandomMnemonics(): Array<String> {
            val entropyBytes = SecureRandom.generateSeed(SEED_ENTROPY_BITS_24_WORDS / 8)
            return MnemonicCode(MnemonicCodeEnglish.wordList.toTypedArray()).toMnemonic(entropyBytes)
        }

        @Throws(InvalidMnemonicCode::class)
        fun createSeed(mnemonics: Array<String>, passphrase: String = "AtalaPrism"): ByteArray {
            if (!isValidMnemonicCode(mnemonics)) {
                throw InvalidMnemonicCode(mnemonics.joinToString(separator = " "))
            }
            val mnemonicString = mnemonics.joinToString(separator = " ")
            return PBKDF2SHA512.derive(
                mnemonicString,
                passphrase,
                PBKDF2C,
                PBKDF2DKLen
            )
        }

        fun createRandomSeed(passphrase: String = "AtalaPrism"): ByteArray {
            val mnemonics = this.createRandomMnemonics()
            return this.createSeed(mnemonics, passphrase)
        }
    }
}
