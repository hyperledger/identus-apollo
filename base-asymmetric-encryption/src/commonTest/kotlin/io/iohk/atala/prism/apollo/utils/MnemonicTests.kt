package io.iohk.atala.prism.apollo.utils

import io.iohk.atala.prism.apollo.hashing.PBKDF2SHA512
import io.iohk.atala.prism.apollo.hashing.internal.toHexString
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class MnemonicTests {
    @Test
    fun testCreateRandomMnemonics() {
        val mnemonics = Mnemonic.createRandomMnemonics().joinToString(separator = " ")
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
        val mnemonics = "random seed mnemonic words"
        val seed = Mnemonic.createSeed(mnemonics)

        assertEquals(seed.size, 64)

        val privateKey = seed.slice(IntRange(0, 31))
        assertContains(privateKey.toByteArray().toHexString(), "feac83cecc84531575eb67250a03d8ac112d4d6678674968bf3f6576ad028ae3")
    }

    @Test
    fun testCreateSeedWithPW() {
        val mnemonics = "random seed mnemonic words"
        val password = "123456"
        val seed = Mnemonic.createSeed(mnemonics, password)

        assertEquals(seed.size, 64)

        val privateKey = seed.slice(IntRange(0, 31))
        assertContains(privateKey.toByteArray().toHexString(), "b3a8af66eca002e8b4ca868c5b55a8c865f15e0cfea483d6a164a6fbecf83625")
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
