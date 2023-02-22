package io.iohk.atala.prism.apollo.derivation

import org.bitcoinj.crypto.HDKeyDerivation
import java.io.ByteArrayInputStream
import java.security.SecureRandom

actual object KeyDerivation {
    private const val SEED_ENTROPY_BITS_24_WORDS = 256

    /**
     * Generates a random mnemonic code, usually used when a new wallet is being created.
     */
    actual fun randomMnemonicCode(): MnemonicCode {
        val entropyBytes = SecureRandom.getSeed(SEED_ENTROPY_BITS_24_WORDS / 8)
        val wordsText = MnemonicCodeEnglish.wordList.joinToString(separator = "\n", postfix = "\n")
        val mnemonicCodeEnglishInputStream = ByteArrayInputStream(wordsText.toByteArray())
        val bitcoinjMnemonic = org.bitcoinj.crypto.MnemonicCode(mnemonicCodeEnglishInputStream, null)
        val mnemonicWords = bitcoinjMnemonic.toMnemonic(entropyBytes)
        return MnemonicCode(mnemonicWords)
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
        val javaWords = seed.words
        val wordsText = MnemonicCodeEnglish.wordList.joinToString("\n", "", "\n")
        val bitcoinjMnemonic =
            org.bitcoinj.crypto.MnemonicCode(wordsText.byteInputStream(), null)

        try {
            bitcoinjMnemonic.check(javaWords)
        } catch (e: org.bitcoinj.crypto.MnemonicException.MnemonicChecksumException) {
            throw MnemonicChecksumException(e.message, e)
        } catch (e: org.bitcoinj.crypto.MnemonicException.MnemonicWordException) {
            throw MnemonicWordException(e.message, e)
        } catch (e: org.bitcoinj.crypto.MnemonicException.MnemonicLengthException) {
            throw MnemonicLengthException(e.message, e)
        } catch (e: Throwable) {
            throw RuntimeException("Unexpected exception returned by MnemonicCode.check", e)
        }

        return org.bitcoinj.crypto.MnemonicCode.toSeed(javaWords, passphrase)
    }

    /**
     * Computes master key from seed bytes, according to BIP 32 protocol
     */
    actual fun derivationRoot(seed: ByteArray): ExtendedKey {
        return ExtendedKey(HDKeyDerivation.createMasterPrivateKey(seed))
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
