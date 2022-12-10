package io.iohk.atala.prism.apollo.derivation

import java.io.ByteArrayInputStream

internal object JvmMnemonic {
    private val wordsText = MnemonicCodeEnglish.wordList.joinToString(separator = "\n", postfix = "\n")
    private val mnemonicCodeEnglishInputStream = ByteArrayInputStream(wordsText.toByteArray())
    val bitcoinjMnemonic = org.bitcoinj.crypto.MnemonicCode(mnemonicCodeEnglishInputStream, null)
}
