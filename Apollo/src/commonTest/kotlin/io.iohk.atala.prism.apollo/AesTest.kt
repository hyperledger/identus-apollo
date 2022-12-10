package io.iohk.atala.prism.apollo

import io.iohk.atala.prism.apollo.util.BytesOps
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class AesTest {

    private val key = SymmetricKey(BytesOps.hexToBytes("c6f919d4634bbf8cf05b28e2a6b3bf98ad9f7693d9aca930702b150cf5c4850b"))
    private val iv = BytesOps.hexToBytes("1e935a245565063b38dc10db501f1d8a8c261a1e1a090440ba94a15ddbc44ac46a5e04cde078f3f695c72d2e63db9b6f6e61d732131069d81b49e7ba543af5ae")
    private val encryptedData = io.iohk.atala.prism.apollo.AesEncryptedData(
        data = BytesOps.hexToBytes("a31e4a778a18121c1b4ca40489ab08ad9e923102fef5"),
        iv = iv,
        salt = null
    )

    @Test
    @IgnoreJs
    fun testEncrypt() {
        assertEquals(encryptedData, Aes.encrypt("secret".encodeToByteArray(), key, iv))
    }

    @Test
    @IgnoreJs
    fun testDecrypt() {
        assertTrue("secret".encodeToByteArray().contentEquals(Aes.decrypt(encryptedData, key)))
    }
}
