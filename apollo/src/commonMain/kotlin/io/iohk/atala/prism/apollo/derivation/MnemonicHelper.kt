package io.iohk.atala.prism.apollo.derivation

import io.iohk.atala.prism.apollo.hashing.PBKDF2SHA512
import io.iohk.atala.prism.apollo.securerandom.SecureRandom
import org.kotlincrypto.hash.sha2.SHA256

final class MnemonicHelper {
    companion object {
        private const val SEED_ENTROPY_BITS_24_WORDS = 256

        private const val PBKDF2C = 2048
        private const val PBKDF2_DK_LEN = 64

        class InvalidMnemonicCode(code: String) : RuntimeException(code)

        fun isValidMnemonicCode(code: List<String>): Boolean {
            return code.all { it in MnemonicCodeEnglish.wordList }
        }

        fun createRandomMnemonics(): List<String> {
            val entropyBytes = SecureRandom.generateSeed(SEED_ENTROPY_BITS_24_WORDS / 8)
            return toMnemonicCode(MnemonicCodeEnglish.wordList, entropyBytes)
        }

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

        fun createRandomSeed(passphrase: String = "AtalaPrism"): ByteArray {
            val mnemonics = this.createRandomMnemonics()
            return this.createSeed(mnemonics, passphrase)
        }

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
