package io.iohk.atala.prism.apollo.derivation

import io.iohk.atala.prism.apollo.utils.external.fromSeed
import io.iohk.atala.prism.apollo.utils.external.generateMnemonic
import io.iohk.atala.prism.apollo.utils.external.mnemonicToSeedSync
import io.iohk.atala.prism.apollo.utils.external.validateMnemonic
import io.iohk.atala.prism.apollo.utils.toByteArray
import node.buffer.Buffer

actual object KeyDerivation {
    private const val SEED_ENTROPY_BITS_24_WORDS = 256
    private val wordArray = MnemonicCodeEnglish.wordList.toTypedArray()

    /**
     * Generates a random mnemonic code, usually used when a new wallet is being created.
     */
    actual fun randomMnemonicCode(): MnemonicCode {
        val words = generateMnemonic(strength = SEED_ENTROPY_BITS_24_WORDS, wordlist = wordArray)
        return MnemonicCode(words.split(' '))
    }

    /**
     * Checks if the word is one of words used in mnemonics
     */
    actual fun isValidMnemonicWord(word: String): Boolean {
        return MnemonicCodeEnglish.wordList.contains(word)
    }

    /**
     * Returns list of valid mnemonic words
     */
    actual fun getValidMnemonicWords(): List<String> {
        return MnemonicCodeEnglish.wordList
    }

    /**
     * From the BIP39 spec (https://github.com/bitcoin/bips/blob/master/bip-0039.mediawiki#from-mnemonic-to-seed):
     * - To create a binary seed from the mnemonic, we use the PBKDF2 function with a mnemonic
     *   sentence (in UTF-8 NFKD) used as the password and the string "mnemonic" + passphrase (again in UTF-8 NFKD)
     *   used as the salt. The iteration count is set to 2048 and HMAC-SHA512 is used as the pseudo-random
     *   function. The length of the derived key is 512 bits (= 64 bytes).
     *
     *  Generate the binary seed given a mnemonic and a password
     *
     *  @param seed list of 24 mnemonic words
     *  @param passphrase password
     *  @return binary seed
     */
    actual fun binarySeed(
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

    /**
     * Computes master key from seed bytes, according to BIP 32 protocol
     */
    actual fun derivationRoot(seed: ByteArray): ExtendedKey {
        val bip32 = fromSeed(Buffer.from(seed.toTypedArray()))
        return ExtendedKey(bip32, DerivationPath.empty())
    }

    /**
     * Computes key in derivation tree from seed bytes, according to BIP 32 protocol
     */
    actual fun deriveKey(
        seed: ByteArray,
        path: DerivationPath
    ): ExtendedKey {
        return path.axes.fold(derivationRoot(seed)) { key, axis -> key.derive(axis) }
    }
}
