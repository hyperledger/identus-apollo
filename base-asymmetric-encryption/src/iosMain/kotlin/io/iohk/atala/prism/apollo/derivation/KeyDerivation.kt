package io.iohk.atala.prism.apollo.derivation

import fr.acinq.bitcoin.DeterministicWallet
import kotlin.random.Random

actual object KeyDerivation {
    private const val SEED_ENTROPY_BITS_24_WORDS = 256

    /**
     * Generates a random mnemonic code, usually used when a new wallet is being created.
     */
    actual fun randomMnemonicCode(): MnemonicCode {
        return MnemonicCode(fr.acinq.bitcoin.MnemonicCode.toMnemonics(Random.Default.nextBytes(SEED_ENTROPY_BITS_24_WORDS / 8)))
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
        try {
            fr.acinq.bitcoin.MnemonicCode.validate(seed.words, MnemonicCodeEnglish.wordList)
        } catch (e: RuntimeException) {
            when {
                e.message == "invalid checksum" -> {
                    throw MnemonicChecksumException(e.message, e)
                }
                e.message == "mnemonic code cannot be empty" || e.message?.contains("invalid mnemonic word count") == true -> {
                    throw MnemonicLengthException(e.message, e)
                }
                e.message?.contains("invalid mnemonic word") == true -> {
                    throw MnemonicWordException(e.message, e)
                }
                else -> {
                    throw e
                }
            }
        }

        return fr.acinq.bitcoin.MnemonicCode.toSeed(seed.words, passphrase)
    }

    /**
     * Computes master key from seed bytes, according to BIP 32 protocol
     */
    actual fun derivationRoot(seed: ByteArray): ExtendedKey {
        return ExtendedKey(DeterministicWallet.generate(seed))
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
