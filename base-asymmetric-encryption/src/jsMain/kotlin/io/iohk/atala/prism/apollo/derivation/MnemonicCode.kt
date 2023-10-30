package io.iohk.atala.prism.apollo.derivation

@OptIn(ExperimentalJsExport::class)
@JsExport
final class MnemonicCode constructor(val words: Array<String>) {
    init {
        if (words.size % 3 != 0) {
            throw MnemonicLengthException("Can't create a DID from mnemonic that is not dividable by 3")
        }
    }

    fun toMnemonic(entropy: ByteArray): Array<String> {
        return MnemonicHelper.toMnemonicCode(words.toList(), entropy).toTypedArray()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class.js != other::class.js) return false

        other as MnemonicCode

        if (!words.contentEquals(other.words)) return false

        return true
    }

    override fun hashCode(): Int {
        return words.contentHashCode()
    }
}
