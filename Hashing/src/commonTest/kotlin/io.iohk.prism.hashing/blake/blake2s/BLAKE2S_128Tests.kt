package io.iohk.prism.hashing.blake.blake2s

import io.iohk.prism.hashing.BLAKE2S_128
import io.iohk.prism.hashing.internal.toHexString
import kotlin.test.Test
import kotlin.test.assertEquals

class BLAKE2S_128Tests {

    private fun hash(stringToHash: String, output: String) {
        val hash = BLAKE2S_128()
        assertEquals(output, hash.digest(stringToHash.encodeToByteArray()).toHexString())
    }

    private fun hashKeyed(stringToHash: String, output: String) {
        val hash = BLAKE2S_128.Keyed("hello".encodeToByteArray())
        assertEquals(output, hash.digest(stringToHash.encodeToByteArray()).toHexString())
    }

    @Test
    fun test_Strings() {
        hash(
            "blake2",
            "13212c0218c995a400ec9da5ee76ab0a"
        )
        hash(
            "hello world",
            "37deae0226c30da2ab424a7b8ee14e83"
        )
        hash(
            "verystrongandlongpassword",
            "f1a8e54c1008db40683e5afd8dad6535"
        )
        hash(
            "The quick brown fox jumps over the lazy dog",
            "96fd07258925748a0d2fb1c8a1167a73"
        )
        hash(
            "",
            "64550d6ffe2c0a01a14aba1eade0200c"
        )
        hash(
            "abc",
            "aa4938119b1dc7b87cbad0ffd200d0ae"
        )
        hash(
            "UPPERCASE",
            "c509c829bc8319d5ea8e5ebf7aa743ca"
        )
        hash(
            "123456789",
            "dce1c41568c6aa166e2f8eafce34e617"
        )
    }

    @Test
    fun test_Keyed() {
        hashKeyed(
            "",
            "db9067ccc6f4249e6543ee804e199671"
        )
        hashKeyed(
            "A",
            "991e2d9986b2b5e86ca1ca46129fc062"
        )
        hashKeyed(
            "ABCDEFGHIJKLMNOPQRSTUVWXYZ",
            "947032cabd450e085d4b66c5ebf4a23c"
        )
    }
}