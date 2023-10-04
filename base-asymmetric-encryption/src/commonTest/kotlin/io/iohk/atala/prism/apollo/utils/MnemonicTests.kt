package io.iohk.atala.prism.apollo.utils

import io.iohk.atala.prism.apollo.hashing.PBKDF2SHA512
import io.iohk.atala.prism.apollo.hashing.internal.toHexString
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse

class MnemonicTests {

    @Test
    fun testValidateMnemonics() {
        val invalidMnemonics = arrayOf("abc", "ddd", "inv")
        assertFalse(Mnemonic.isValidMnemonicCode(invalidMnemonics))
    }

    @Test
    fun testCreateRandomMnemonics() {
        val mnemonics = Mnemonic.createRandomMnemonics()
        val seed = Mnemonic.createSeed(mnemonics)
        assertEquals(seed.size, 64)
    }

    @Test
    fun testCreateRandomSeedWithPW() {
        val seed = Mnemonic.createRandomSeed("Demo passphrase")
        assertEquals(seed.size, 64)
    }

    @Test
    fun testCreateSeed() {
        val mnemonics = arrayOf("adjust", "animal", "anger", "around")
        val seed = Mnemonic.createSeed(mnemonics)

        assertEquals(seed.size, 64)

        val privateKey = seed.slice(IntRange(0, 31))
        assertContains(privateKey.toByteArray().toHexString(), "a078d8a0f3beca52ef17a1d0279eb6e9c410cd3837d3db38d31e43df6da95ac6")
    }

    @Test
    fun testCreateSeedInvalidMnemonics() {
        val mnemonics = arrayOf("abc", "ddd", "adsada", "testing")

        assertFailsWith<Mnemonic.Companion.InvalidMnemonicCode> {
            Mnemonic.createSeed(mnemonics)
        }
    }

    @Test
    fun testCreateSeedWithPW() {
        val mnemonics = arrayOf("adjust", "animal", "anger", "around")
        val password = "123456"
        val seed = Mnemonic.createSeed(mnemonics, password)

        assertEquals(seed.size, 64)

        val privateKey = seed.slice(IntRange(0, 31))

        assertContains(privateKey.toByteArray().toHexString(), "815b70655ca4c9675f5fc15fe8f82315f07521d034eec45bf4d5912bd3a61218")
    }

    @Test
    fun testCreateSeedWithPW2() {
        val mnemonics = "tool knock nerve skate detail early limit energy foam garage resource boring traffic violin cave place accuse can bring bring cargo clip stick dog"
        val c = 2048
        val dklen = 64
        val passphrase = "mnemonic"

        val derived = PBKDF2SHA512.derive(mnemonics, passphrase, c, dklen)

        assertEquals(derived.size, 64)
    }

    @Test
    fun testCreateSeedWithPW2() {
        val mnemonics = "tool knock nerve skate detail early limit energy foam garage resource boring traffic violin cave place accuse can bring bring cargo clip stick dog"
        val c = 2048
        val dklen = 64
        val passphrase = "mnemonic"

        val derived = PBKDF2SHA512.derive(mnemonics, passphrase, c, dklen)

        assertEquals(derived.size, 64)
    }
}
