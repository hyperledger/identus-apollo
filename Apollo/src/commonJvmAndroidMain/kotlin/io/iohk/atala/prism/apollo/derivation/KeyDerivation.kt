package io.iohk.atala.prism.apollo.derivation

import org.bitcoinj.crypto.HDKeyDerivation
import java.io.ByteArrayInputStream
import java.security.SecureRandom
import kotlin.jvm.Throws

public actual object KeyDerivation {
    private const val SEED_ENTROPY_BITS_24_WORDS = 256

    private fun mnemonicCodeEnglishInputStream(): ByteArrayInputStream {
        val wordsText = MnemonicCodeEnglish.wordList.joinToString("\n", "", "\n")
        return wordsText.byteInputStream()
    }

    private val bitcoinjMnemonic =
        org.bitcoinj.crypto.MnemonicCode(mnemonicCodeEnglishInputStream(), null)

    public actual fun randomMnemonicCode(): MnemonicCode {
        val entropyBytes = SecureRandom.getSeed(SEED_ENTROPY_BITS_24_WORDS / 8)
        val mnemonicWords = JvmMnemonic.bitcoinjMnemonic.toMnemonic(entropyBytes)

        return MnemonicCode(mnemonicWords)
    }

    public actual fun isValidMnemonicWord(word: String): Boolean =
        MnemonicCodeEnglish.wordList.contains(word)

    public actual fun getValidMnemonicWords(): List<String> =
        MnemonicCodeEnglish.wordList

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
     *  @exception [io.iohk.atala.prism.apollo.derivation.MnemonicLengthException]
     *  @exception [io.iohk.atala.prism.apollo.derivation.MnemonicChecksumException]
     *  @exception [io.iohk.atala.prism.apollo.derivation.MnemonicWordException]
     *  @exception RuntimeException("Unexpected exception returned by MnemonicCode.check")
     */
    @Throws(MnemonicLengthException::class, MnemonicChecksumException::class, MnemonicWordException::class, RuntimeException::class)
    public actual fun binarySeed(seed: MnemonicCode, passphrase: String): ByteArray {
        val javaWords = seed.words

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

    public actual fun derivationRoot(seed: ByteArray): ExtendedKey =
        ExtendedKey(HDKeyDerivation.createMasterPrivateKey(seed))

    public actual fun deriveKey(seed: ByteArray, path: DerivationPath): ExtendedKey =
        path.axes.fold(derivationRoot(seed)) { key, axis -> key.derive(axis) }
}
