package io.iohk.atala.prism.apollo.derivation

import fr.acinq.bitcoin.DeterministicWallet
import io.iohk.atala.prism.apollo.util.toUByteArray
import kotlinx.cinterop.*
import platform.posix.*

public actual object KeyDerivation {
    private const val SEED_ENTROPY_BITS_24_WORDS = 256

    private fun generateBytes(memScope: MemScope, numBytes: Int): CArrayPointer<UByteVar> {
        val result = memScope.allocArray<UByteVar>(numBytes)
        val resultPtr = result.getPointer(memScope)
        val urandom = fopen("/dev/urandom", "rb") ?: error("No /dev/urandom on this device")
        try {
            fread(resultPtr, 1.convert(), numBytes.convert(), urandom)
            for (n in 0 until numBytes) result[n] = resultPtr[n]
        } finally {
            fclose(urandom)
        }
        return result
    }

    public actual fun randomMnemonicCode(): MnemonicCode {
        val mnemonicWords = memScoped {
            val entropyPtr = generateBytes(this, SEED_ENTROPY_BITS_24_WORDS / 8)
            val entropyBytes = entropyPtr.toUByteArray(SEED_ENTROPY_BITS_24_WORDS / 8).toByteArray()
            fr.acinq.bitcoin.MnemonicCode.toMnemonics(entropyBytes)
        }

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
     *  @exception RuntimeException
     */
    @Throws(RuntimeException::class)
    public actual fun binarySeed(seed: MnemonicCode, passphrase: String): ByteArray {
        try {
            fr.acinq.bitcoin.MnemonicCode.validate(seed.words, MnemonicCodeEnglish.wordList)
        } catch (e: RuntimeException) {
            when {
                e.message == "invalid checksum" -> {
                    throw MnemonicChecksumException(e.message, e)
                }
                e.message == "mnemonic code cannot be empty" ||
                    e.message?.contains("invalid mnemonic word count") == true -> {
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

    public actual fun derivationRoot(seed: ByteArray): ExtendedKey =
        ExtendedKey(DeterministicWallet.generate(seed))

    public actual fun deriveKey(seed: ByteArray, path: DerivationPath): ExtendedKey =
        path.axes.fold(derivationRoot(seed)) { key, axis -> key.derive(axis) }
}
