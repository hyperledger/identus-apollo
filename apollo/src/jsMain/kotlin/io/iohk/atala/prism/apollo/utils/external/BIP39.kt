// Automatically generated by dukat and then slightly adjusted manually to make it compile
@file:Suppress("ktlint", "internal:ktlint-suppression")
@file:JsModule("bip39")

package io.iohk.atala.prism.apollo.utils.external

import node.buffer.Buffer
import kotlin.js.*

internal external fun mnemonicToSeedSync(mnemonic: String, password: String = definedExternally): Buffer

internal external fun mnemonicToSeed(mnemonic: String, password: String = definedExternally): Promise<Buffer>

internal external fun mnemonicToEntropy(mnemonic: String, wordlist: Array<String> = definedExternally): String

internal external fun entropyToMnemonic(entropy: Buffer, wordlist: Array<String> = definedExternally): String

internal external fun entropyToMnemonic(entropy: Buffer): String

internal external fun entropyToMnemonic(entropy: String, wordlist: Array<String> = definedExternally): String

internal external fun entropyToMnemonic(entropy: String): String

internal external fun generateMnemonic(strength: Number = definedExternally, rng: (size: Number) -> Buffer = definedExternally, wordlist: Array<String> = definedExternally): String

internal external fun validateMnemonic(mnemonic: String, wordlist: Array<String> = definedExternally): Boolean

internal external fun setDefaultWordlist(language: String)

internal external fun getDefaultWordlist(): String