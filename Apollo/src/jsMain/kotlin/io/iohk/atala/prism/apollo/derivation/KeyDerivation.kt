package io.iohk.atala.prism.apollo.derivation

import io.iohk.atala.prism.apollo.externals.fromSeed
import io.iohk.atala.prism.apollo.externals.generateMnemonic
import io.iohk.atala.prism.apollo.externals.mnemonicToSeedSync
import io.iohk.atala.prism.apollo.externals.validateMnemonic
import io.iohk.atala.prism.apollo.util.toByteArray

@JsExport
public actual object KeyDerivation {
    private const val SEED_ENTROPY_BITS_24_WORDS = 256
    private val wordArray = MnemonicCodeEnglish.wordList.toTypedArray()

    public actual fun randomMnemonicCode(): MnemonicCode {
        val words = generateMnemonic(strength = SEED_ENTROPY_BITS_24_WORDS, wordlist = wordArray)
        return MnemonicCode(words.split(' '))
    }

    public actual fun isValidMnemonicWord(word: String): Boolean =
        MnemonicCodeEnglish.wordList.contains(word)

    public actual fun getValidMnemonicWords(): List<String> =
        MnemonicCodeEnglish.wordList

    public actual fun binarySeed(
        seed: MnemonicCode,
        passphrase: String
    ): ByteArray {
        val mnemonic = seed.words.joinToString(" ")

        if (seed.words.size % 3 != 0) {
            throw MnemonicLengthException("Word list size must be multiple of three words")
        } else if (seed.words.isEmpty()) {
            throw MnemonicLengthException("Word list is empty")
        }
        for (word in seed.words) {
            if (!isValidMnemonicWord(word)) {
                throw MnemonicWordException("Invalid mnemonic word: $word")
            }
        }

        if (!validateMnemonic(mnemonic, wordArray)) {
            throw MnemonicChecksumException("Invalid mnemonic checksum")
        }

        return mnemonicToSeedSync(mnemonic, passphrase).toByteArray()
    }

    public actual fun derivationRoot(seed: ByteArray): ExtendedKey {
        val bip32 = fromSeed(Buffer.from(seed.toTypedArray()))
        return ExtendedKey(bip32, DerivationPath.empty())
    }

    public actual fun deriveKey(
        seed: ByteArray,
        path: DerivationPath
    ): ExtendedKey =
        path.axes.fold(derivationRoot(seed)) { key, axis -> key.derive(axis) }
}
