package io.iohk.prism.hashing.blake.blake2b

import io.iohk.prism.hashing.BLAKE2B_384
import io.iohk.prism.hashing.internal.toHexString
import kotlin.test.Test
import kotlin.test.assertEquals

class BLAKE2B_384Tests {

    private fun hash(stringToHash: String, output: String) {
        val hash = BLAKE2B_384()
        assertEquals(output, hash.digest(stringToHash.encodeToByteArray()).toHexString())
    }

    private fun hashKeyed(stringToHash: String, output: String) {
        val hash = BLAKE2B_384.Keyed("hello".encodeToByteArray())
        assertEquals(output, hash.digest(stringToHash.encodeToByteArray()).toHexString())
    }

    /**
     * From:
     * - https://www.blake2.net/blake2b-test.txt
     */

    @Test
    fun test_Strings() {
        hash(
            "blake2",
            "a15b4fd669cf966479c74f7ac4046b0a9a1171ce0ef623ac2131523321a451d647a81feb7317683d4b65c2329db45979"
        )
        hash(
            "hello world",
            "8c653f8c9c9aa2177fb6f8cf5bb914828faa032d7b486c8150663d3f6524b086784f8e62693171ac51fc80b7d2cbb12b"
        )
        hash(
            "verystrongandlongpassword",
            "d9d3724cab698d25331a79d599880559277f475946c9445888ec99e79e78dcbf45cfa5c39ac3f34380a141bcbba7a96a"
        )
        hash(
            "The quick brown fox jumps over the lazy dog",
            "b7c81b228b6bd912930e8f0b5387989691c1cee1e65aade4da3b86a3c9f678fc8018f6ed9e2906720c8d2a3aeda9c03d"
        )
        hash(
            "",
            "b32811423377f52d7862286ee1a72ee540524380fda1724a6f25d7978c6fd3244a6caf0498812673c5e05ef583825100"
        )
        hash(
            "abc",
            "6f56a82c8e7ef526dfe182eb5212f7db9df1317e57815dbda46083fc30f54ee6c66ba83be64b302d7cba6ce15bb556f4"
        )
        hash(
            "UPPERCASE",
            "6fc332404d2888cffc2c8d1d7302acd0ffc133d84cf1d4bdd000edc14fe73e5a54366a705a66549a54207a50a997e793"
        )
        hash(
            "123456789",
            "80f35fcfa2f3eba9cac3287c2d95d02b5f179a65dfc60c9f48275a459919d2b52bdb5877dcd7e21e9ff95a551b87fc36"
        )
    }

    @Test
    fun keyed() {
        hashKeyed(
            "",
            "dc12e6cfaf5a8d59cdf98ad68192f854880598f2639f5b6c745c1b61a3afffc6c1d79326c1326b5c8945d40cf203625e"
        )
        hashKeyed(
            "A",
            "3bb34b8d43f0d98c910fb04247f25d574052dd5b5f8fa2e2e3dbdf0f4850d812a803a827d1662067c9bce039eed016a4"
        )
        hashKeyed(
            "ABCDEFGHIJKLMNOPQRSTUVWXYZ",
            "cc5a2dbe7e2d24d297d15dadb972c86fe4e748b770ce402e6162f9acaaffc9606536dae99a55e415c847ada2e3e1e7ac"
        )
    }
}
