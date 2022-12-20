package io.iohk.atala.prism.apollo.aes

import io.iohk.atala.prism.apollo.utils.KMMSymmetricKey
import io.iohk.atala.prism.apollo.utils.SymmetricKeyType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class AESTests {
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testAESGCM() = runTest {
        val text = "Welcome to IOG!"
        val algo = KAESAlgorithm.AES_256
        val key = AES.createRandomAESKey(algo)
        val aes = AES(
            algo,
            KAESBlockMode.GCM,
            KAESPadding.NO_PADDING,
            key,
            KMMSymmetricKey.createRandomIV(16)
        )
        val encryptedBytes = aes.encrypt(text.encodeToByteArray())
        val decryptedBytes = aes.decrypt(encryptedBytes)
        assertEquals(text, decryptedBytes.decodeToString())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testAESCBC() = runTest {
        val text = "Welcome to IOG!"
        val algo = KAESAlgorithm.AES_256
        val key = AES.createRandomAESKey(algo)
        val aes = AES(
            algo,
            KAESBlockMode.CBC,
            KAESPadding.PKCS5PADDING,
            key,
            KMMSymmetricKey.createRandomIV(16)
        )
        val encryptedBytes = aes.encrypt(text.encodeToByteArray())
        val decryptedBytes = aes.decrypt(encryptedBytes)
        assertEquals(text, decryptedBytes.decodeToString())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testAESKeyExportImport() = runTest {
        val algo = KAESAlgorithm.AES_256
        val key = AES.createRandomAESKey(algo)

        val base64Key = key.exportToBase64()

        val importedKey = KMMSymmetricKey.createKeyFromBase64(base64Key, SymmetricKeyType.AES)

        assertEquals(base64Key, importedKey.exportToBase64())
    }
}
