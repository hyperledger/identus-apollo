package io.iohk.atala.prism.apollo

import io.iohk.atala.prism.apollo.util.BytesOps
import kotlin.test.Test
import kotlin.test.assertEquals

internal class HmacSha256Test {

    @Test
    fun compute() {
        val hmac = HmacSha256.compute("test".encodeToByteArray(), SymmetricKey("secret".encodeToByteArray()))
        assertEquals(32, hmac.size)
        assertEquals(
            "0329a06b62cd16b33eb6792be8c60b158d89a2ee3a876fce9a881ebb488c0914",
            BytesOps.bytesToHex(hmac)
        )
    }
}
