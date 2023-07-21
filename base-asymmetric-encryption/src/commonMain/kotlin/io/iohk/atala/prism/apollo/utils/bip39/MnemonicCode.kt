package io.iohk.atala.prism.apollo.utils.bip39

import io.iohk.atala.prism.apollo.hashing.SHA256
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

/**
 * A model representing the Mnemonic Code
 *
 * @exception MnemonicLengthException in case it is not 24 words
 */
@OptIn(ExperimentalJsExport::class)
@JsExport
final class MnemonicCode(val words: Array<String>) {
    fun toMnemonic(entropy: ByteArray): Array<String> {
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

        val words = ArrayList<String>()
        val nwords = concatBits.size / 11
        for (i in 0 until nwords) {
            var index = 0
            for (j in 0 until 11) {
                index = index shl 1
                if (concatBits[i * 11 + j]) {
                    index = index or 0x1
                }
            }
            words.add(this.words[index]) // Assuming wordList is a list of words according to BIP-39
        }

        return words.toTypedArray()
    }
}
