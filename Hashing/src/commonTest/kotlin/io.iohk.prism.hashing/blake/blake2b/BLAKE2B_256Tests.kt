package io.iohk.prism.hashing.blake.blake2b

import io.iohk.prism.hashing.BLAKE2B_256
import io.iohk.prism.hashing.internal.toHexString
import kotlin.test.Test
import kotlin.test.assertEquals

class BLAKE2B_256Tests {

    private fun hash(stringToHash: String, output: String) {
        val hash = BLAKE2B_256()
        assertEquals(output, hash.digest(stringToHash.encodeToByteArray()).toHexString())
    }

    private fun hashKeyed(stringToHash: String, output: String) {
        val hash = BLAKE2B_256.Keyed("hello".encodeToByteArray())
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
            "2691c04886143bd44752a384fbc197d4236e2740716bf5be48c0ff0511d09209"
        )
        hash(
            "hello world",
            "256c83b297114d201b30179f3f0ef0cace9783622da5974326b436178aeef610"
        )
        hash(
            "verystrongandlongpassword",
            "0be8eefd20cb65c34363dcea323883953b8febbbd125ea38e18244c645cb1833"
        )
        hash(
            "The quick brown fox jumps over the lazy dog",
            "01718cec35cd3d796dd00020e0bfecb473ad23457d063b75eff29c0ffa2e58a9"
        )
        hash(
            "",
            "0e5751c026e543b2e8ab2eb06099daa1d1e5df47778f7787faab45cdf12fe3a8"
        )
        hash(
            "abc",
            "bddd813c634239723171ef3fee98579b94964e3bb1cb3e427262c8c068d52319"
        )
        hash(
            "UPPERCASE",
            "3d43b230c7b29c9c2fc1d0bf6a3dc79fd9c05ab5eeaa9c6cdb425be037a1baa5"
        )
        hash(
            "123456789",
            "16e0bf1f85594a11e75030981c0b670370b3ad83a43f49ae58a2fd6f6513cde9"
        )
    }

    @Test
    fun test_Keyed() {
        hashKeyed(
            "",
            "e2d195462b16afe436c946a6e93ead79a8bf1f875805ae0c57b9d4986def473b"
        )
        hashKeyed(
            "A",
            "972cd53c40222a761e7bb65c5f5c8e687f565346c23c2a0de543bc334914d8b8"
        )
        hashKeyed(
            "ABCDEFGHIJKLMNOPQRSTUVWXYZ",
            "4264ef6d7b0aeb7b3f4b0d070f063f13f6157ba36294797a280f0346a57180cb"
        )
    }
}