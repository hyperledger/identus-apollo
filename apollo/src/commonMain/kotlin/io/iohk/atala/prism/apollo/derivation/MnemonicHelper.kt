package io.iohk.atala.prism.apollo.derivation

import io.iohk.atala.prism.apollo.hashing.PBKDF2SHA512
import io.iohk.atala.prism.apollo.securerandom.SecureRandom
import org.kotlincrypto.hash.sha2.SHA256

/**
 * A helper class for working with mnemonics.
 */
final class MnemonicHelper {
    companion object {
        private const val SEED_ENTROPY_BITS_24_WORDS = 256

        private const val PBKDF2C = 2048
        private const val PBKDF2_DK_LEN = 64

        /**
         * A custom exception class representing an invalid mnemonic code.
         *
         * @param code The mnemonic code that is invalid.
         * @constructor Creates an instance of InvalidMnemonicCode with the specified code.
         */
        class InvalidMnemonicCode(code: String) : RuntimeException(code)

        /**
         * It validates if a provided list of works is a valid mnemonic
         *
         * @return Boolean
         */
        fun isValidMnemonicCode(code: List<String>): Boolean {
            return code.all { it in MnemonicCodeEnglish.wordList }
        }

        /**
         * It creates a random list of works
         *
         * @return List<String> as mnemonics
         */
        fun createRandomMnemonics(): List<String> {
            val entropyBytes = SecureRandom.generateSeed(SEED_ENTROPY_BITS_24_WORDS / 8)
            return toMnemonicCode(MnemonicCodeEnglish.wordList, entropyBytes)
        }

        /**
         * Creates a seed from a mnemonics list and a passphrase.
         *
         * @param mnemonics The word list used for generating the mnemonic code.
         * @param passphrase Passphrase used to derive the mnemonic string
         * @throws InvalidMnemonicCode if the list of mnemonics is invalid
         * @return ByteArray representing the seed raw value
         */
        @Throws(InvalidMnemonicCode::class)
        fun createSeed(mnemonics: List<String>, passphrase: String = "AtalaPrism"): ByteArray {
            if (!isValidMnemonicCode(mnemonics)) {
                throw InvalidMnemonicCode(mnemonics.joinToString(separator = " "))
            }
            val mnemonicString = mnemonics.joinToString(separator = " ")
            return PBKDF2SHA512.derive(
                mnemonicString,
                passphrase,
                PBKDF2C,
                PBKDF2_DK_LEN
            )
        }

        /**
         * Create random seed with a passphrase
         *
         * @param String passphrase to include into the seed creation
         * @return ByteArray seed raw value
         */
        fun createRandomSeed(passphrase: String = "AtalaPrism"): ByteArray {
            val mnemonics = this.createRandomMnemonics()
            return this.createSeed(mnemonics, passphrase)
        }

        /**
         * Converts a byte array of entropy into a mnemonic code (word list) based on the specified word list.
         *
         * @param words The word list to be used for generating the mnemonic code. This list should conform to the BIP-39 standard.
         * @param entropy The entropy byte array, which is used to generate the mnemonic code. The length of this array must be a multiple of 4 bytes (32 bits).
         * @throws Exception if the entropy is empty or its length is not a multiple of 32 bits.
         * @return A list of strings representing the mnemonic code.
         */
        fun toMnemonicCode(words: List<String>, entropy: ByteArray): List<String> {
            if (entropy.size % 4 > 0) {
                throw Exception("Entropy length not multiple of 32 bits.")
            }
            if (entropy.isEmpty()) {
                throw Exception("Entropy is empty.")
            }

            val sha = SHA256()
            sha.update(entropy)
            val hash = sha.digest()
            val checksumLengthBits = entropy.size / 4
            val checksum = hash[0].toInt() ushr (8 - checksumLengthBits)

            val concatBits = BooleanArray(entropy.size * 8 + checksumLengthBits)
            for (i in entropy.indices) {
                for (j in 0 until 8) {
                    concatBits[i * 8 + j] = entropy[i].toInt() and (1 shl (7 - j)) != 0
                }
            }
            for (i in 0 until checksumLengthBits) {
                concatBits[entropy.size * 8 + i] = checksum and (1 shl (checksumLengthBits - i - 1)) != 0
            }

            val resultWords = ArrayList<String>()
            val nwords = concatBits.size / 11
            for (i in 0 until nwords) {
                var index = 0
                for (j in 0 until 11) {
                    index = index shl 1
                    if (concatBits[i * 11 + j]) {
                        index = index or 0x1
                    }
                }
                resultWords.add(words[index]) // Assuming wordList is a list of words according to BIP-39
            }

            return resultWords
        }
    }
}
