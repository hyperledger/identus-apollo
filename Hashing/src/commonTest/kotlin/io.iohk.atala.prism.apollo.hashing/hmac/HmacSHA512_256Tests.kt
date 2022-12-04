package io.iohk.atala.prism.apollo.hashing.hmac

import io.iohk.atala.prism.apollo.hashing.SHA512_256
import io.iohk.atala.prism.apollo.hashing.internal.JsIgnore
import io.iohk.atala.prism.apollo.hashing.internal.toHexString
import kotlin.test.Test
import kotlin.test.assertEquals

@JsIgnore
class HmacSHA512_256Tests : BaseHmacHashTests() {

    override fun hash(key: ByteArray, stringToHash: ByteArray, outputLength: Int?): String {
        val hash = SHA512_256().createHmac(key, outputLength)
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
            "9f9126c3d9c3c330d760425ca8a217e31feae31bfe70196ff81642b868402eab",
            hash("0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b", "Hi There")
        )
        assertEquals(
            "6df7b24630d5ccb2ee335407081a87188c221489768fa2020513b2d593359456",
            hash("4a656665", "what do ya want for nothing?")
        )
    }
}
