package io.iohk.prism.hashing.hmac

import io.iohk.prism.hashing.SHA512_224
import io.iohk.prism.hashing.internal.toHexString
import kotlin.test.Test
import kotlin.test.assertEquals

class HmacSHA512_224Tests: BaseHmacHashTests() {

    override fun hash(key: ByteArray, stringToHash: ByteArray, outputLength: Int?): String {
        val hash = SHA512_224().createHmac(key, outputLength)
        return hash.digest(stringToHash).toHexString()
    }

    /**
     * From:
     * - https://github.com/bcgit/bc-java/blob/master/prov/src/test/java/org/bouncycastle/jce/provider/test/HMacTest.java
     * - https://github.com/peazip/PeaZip/blob/welcome/peazip-sources/t_hmac.pas
     */

    @Test
    fun test_Strings() {
        assertEquals(
            "b244ba01307c0e7a8ccaad13b1067a4cf6b961fe0c6a20bda3d92039",
            hash("0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b", "Hi There")
        )
        assertEquals(
            "4a530b31a79ebcce36916546317c45f247d83241dfb818fd37254bde",
            hash("4a656665", "what do ya want for nothing?")
        )
    }
}
