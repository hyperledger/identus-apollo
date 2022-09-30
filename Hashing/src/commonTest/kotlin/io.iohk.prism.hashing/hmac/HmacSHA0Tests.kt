package io.iohk.prism.hashing.hmac

import io.iohk.prism.hashing.SHA0
import io.iohk.prism.hashing.internal.JsIgnore
import io.iohk.prism.hashing.internal.toBinary
import io.iohk.prism.hashing.internal.toHexString
import kotlin.test.Test
import kotlin.test.assertEquals

@JsIgnore
class HmacSHA0Tests : BaseHmacHashTests() {

    override fun hash(key: ByteArray, stringToHash: ByteArray, outputLength: Int?): String {
        val hash = SHA0().createHmac(key, outputLength)
        return hash.digest(stringToHash).toHexString()
    }

    /**
     * From:
     * - https://github.com/crypto-browserify/hash-test-vectors/blob/master/hmac.json
     */

    @Test
    fun test_Strings() {
        assertEquals(
            "c2cbaa7817447fb494ca153a88f2f013f934ff58",
            hash("0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b", "Hi There")
        )
    }

    @Test
    fun test_Hexs() {
        assertEquals(
            "b058879503487b824bfb6bdd59d10e910f55a428",
            hash("4a656665", "7768617420646f2079612077616e74207768617420646f2079612077616e7420".toBinary())
        )
        assertEquals(
            "20b8027a3e4b3a7485d16d3297ea05389d64b4bf",
            hash("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", "dddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd".toBinary())
        )
        assertEquals(
            "8e47262e2e939da3cd487ddffe3f6bbb9f2809e7",
            hash("0102030405060708090a0b0c0d0e0f10111213141516171819", "cdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcd".toBinary())
        )
    }

    @Test
    fun test_Truncation() {
        assertEquals(
            "3a29508f315d0548c140e8a8c0b4cd58",
            hash("0c0c0c0c0c0c0c0c0c0c0c0c0c0c0c0c0c0c0c0c", "Test With Truncation", 16)
        )
    }

    @Test
    fun test_LargerThanBlockSizeKeyAndLargerThanOneBlockSizeData() {
        assertEquals(
            "8b0a2731db7a6c716644354dbebdf8f4b0eb4e1f",
            hash("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", "This is a test using a larger than block-size key and a larger than block-size data. The key needs to be hashed before being used by the HMAC algorithm.")
        )
    }
}
