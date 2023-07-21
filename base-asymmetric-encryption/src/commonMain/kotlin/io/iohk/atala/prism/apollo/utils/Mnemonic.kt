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

        fun createRandomMnemonics(): Array<String> {
            val entropyBytes = SecureRandom.generateSeed(SEED_ENTROPY_BITS_24_WORDS / 8)
            return MnemonicCode(MnemonicCodeEnglish.wordList.toTypedArray()).toMnemonic(entropyBytes)
        }

        fun createSeed(mnemonics: String, passphrase: String = "AtalaPrism"): ByteArray {
            return PBKDF2SHA512.derive(
                mnemonics,
                passphrase,
                PBKDF2C,
                PBKDF2DKLen
            )
        }

        fun createRandomSeed(passphrase: String = "AtalaPrism"): ByteArray {
            val mnemonics = this.createRandomMnemonics().joinToString(separator = " ")
            return this.createSeed(mnemonics, passphrase)
        }
    }
}
