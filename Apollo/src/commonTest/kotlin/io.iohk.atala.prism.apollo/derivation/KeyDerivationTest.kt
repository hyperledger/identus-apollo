package io.iohk.atala.prism.apollo.derivation

import io.iohk.atala.prism.apollo.util.BytesOps
import kotlinx.serialization.*
import kotlinx.serialization.json.*
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlin.test.*

@ExperimentalSerializationApi
class KeyDerivationTest {

    @Test
    fun testRandomMnemonicCode() {
        for (i in 1..10) {
            assertEquals(24, KeyDerivation.randomMnemonicCode().words.size)
        }
    }

    @Test
    fun testGenerateRandomMnemonics() {
        val seenWords = mutableSetOf<String>()
        for (i in 1..300) {
            seenWords.addAll(KeyDerivation.randomMnemonicCode().words)
        }

        // with great probability we'll see at least 75% of words after 3600 draws from 2048 possible
        assertTrue(2048 - seenWords.size < 512)
    }

    @Test
    fun testValidMnemonicCode() {
        for (word in KeyDerivation.getValidMnemonicWords()) {
            assertTrue(KeyDerivation.isValidMnemonicWord(word))
        }
    }

    @Test
    fun testInvalidMnemonicCode() {
        assertFalse(KeyDerivation.isValidMnemonicWord("hocus"))
    }

    @Test
    fun testComputeRightBinarySeed() {
        val password = "TREZOR"
        val vectors = Json.decodeFromString<List<List<String>>>(bip39Vectors)
        for (v in vectors) {
            val (_, mnemonicPhrase, binarySeedHex, _) = v
            val mnemonicCode = MnemonicCode(mnemonicPhrase.split(" "))
            val binarySeed = KeyDerivation.binarySeed(mnemonicCode, password)

            assertEquals(binarySeedHex, BytesOps.bytesToHex(binarySeed))
        }
    }

    @Test
    fun testFailWhenChecksumIsIncorrect() {
        val mnemonicCode = MnemonicCode(List(24) { "abandon" })
        assertFailsWith<MnemonicChecksumException> {
            KeyDerivation.binarySeed(mnemonicCode, "")
        }
    }

    @Test
    fun testFailWhenInvalidWordIsUsed() {
        val mnemonicCode = MnemonicCode(listOf("hocus", "pocus", "mnemo", "codus") + List(20) { "abandon" })
        assertFailsWith<MnemonicWordException> {
            KeyDerivation.binarySeed(mnemonicCode, "")
        }
    }

    @Test
    fun testFailWhenWrongLength() {
        assertFailsWith<MnemonicLengthException> {
            val mnemonicCode = MnemonicCode(listOf("abandon"))
            KeyDerivation.binarySeed(mnemonicCode, "")
        }
    }

    @Test
    fun testDeriveKey() {
        class Derivation(val path: String, val pubKeyHex: String, val privKeyHex: String)
        class TestVector(val seedHex: String, val derivations: List<Derivation>)

        // Kotlin/Native does not have reflection and hence needs an explicit
        // serializer provided
        val module = SerializersModule {
            polymorphic(RawTestVector::class, RawTestVector.serializer())
        }

        val vectors = Json {
            serializersModule = module
        }.decodeFromString<List<RawTestVector>>(bip32Vectors).map { vector ->
            val derivations = vector.derivations.map { derivation ->
                val (path, pubKeyHex, privKeyHex) = derivation
                Derivation(path, pubKeyHex, privKeyHex)
            }
            TestVector(vector.seed, derivations)
        }

        for (v in vectors) {
            val seed = BytesOps.hexToBytes(v.seedHex)
            for (d in v.derivations) {
                val path = DerivationPath.fromPath(d.path)
                val key = KeyDerivation.deriveKey(seed, path)

                assertEquals(d.privKeyHex, key.privateKey().getHexEncoded())
                assertEquals(d.pubKeyHex, key.publicKey().getHexEncoded())
            }
        }
    }

    @Test
    fun testExtendedKeyPathsMatch() {
        val derivationPath = "m/0'/0'/1'"
        val mnemonicCode = KeyDerivation.randomMnemonicCode()
        val seed = KeyDerivation.binarySeed(mnemonicCode, "my_secret_password")
        val path = DerivationPath.fromPath(derivationPath)
        val extendedKey = KeyDerivation.deriveKey(seed, path)
        assertEquals(DerivationPath.fromPath(derivationPath), extendedKey.path())
    }
}
