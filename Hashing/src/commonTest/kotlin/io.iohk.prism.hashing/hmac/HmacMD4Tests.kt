package io.iohk.prism.hashing.hmac

import io.iohk.prism.hashing.MD4
import io.iohk.prism.hashing.internal.JsIgnore
import io.iohk.prism.hashing.internal.toBinary
import io.iohk.prism.hashing.internal.toHexString
import kotlin.test.Test
import kotlin.test.assertEquals

@JsIgnore
class HmacMD4Tests : BaseHmacHashTests() {

    override fun hash(key: ByteArray, stringToHash: ByteArray, outputLength: Int?): String {
        val hash = MD4().createHmac(key, outputLength)
        return hash.digest(stringToHash).toHexString()
    }

    /**
     * From:
     * - https://github.com/crypto-browserify/hash-test-vectors/blob/master/hmac.json
     * - https://github.com/xsc/pandect/blob/main/test/pandect/hmac_test.clj
     */

    @Test
    fun test_Strings() {
        assertEquals("5570ce964ba8c11756cdc3970278ff5a", hash("0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b", "Hi There"))
        assertEquals("8d3366c440a9c65124ab0b5f4ca27338", hash("6b6579", "The quick brown fox jumps over the lazy dog"))
    }

    @Test
    fun test_Hexs() {
        assertEquals("c8451e320690b9b5dbd859f2eb63230b", hash("4a656665", "7768617420646f2079612077616e74207768617420646f2079612077616e7420".toBinary()))
        assertEquals("bc9d1ec8a7d0ee67a2955fac8cc78dde", hash("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", "dddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd".toBinary()))
        assertEquals("fb14cddf9efe11ad24033fc70f37bb9e", hash("0102030405060708090a0b0c0d0e0f10111213141516171819", "cdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcd".toBinary()))
    }

    @Test
    fun test_LargerThanBlockSizeKeyAndLargerThanBlockSizeData() {
        assertEquals("7d3124db88aaddd70a5d1dcd1a1a9113", hash("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", "This is a test using a larger than block-size key and a larger than block-size data. The key needs to be hashed before being used by the HMAC algorithm."))
    }
}
