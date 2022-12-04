package io.iohk.atala.prism.apollo.hashing.blake.blake2s

import io.iohk.atala.prism.apollo.hashing.BLAKE2S_224
import io.iohk.atala.prism.apollo.hashing.internal.toHexString
import kotlin.test.Test
import kotlin.test.assertEquals

class BLAKE2S_224Tests {

    private fun hash(stringToHash: String, output: String) {
        val hash = BLAKE2S_224()
        assertEquals(output, hash.digest(stringToHash.encodeToByteArray()).toHexString())
    }

    private fun hashKeyed(stringToHash: String, output: String) {
        val hash = BLAKE2S_224.Keyed("hello".encodeToByteArray())
        assertEquals(output, hash.digest(stringToHash.encodeToByteArray()).toHexString())
    }

    @Test
    fun test_Strings() {
        hash(
            "blake2",
            "02bfe3a0a82b8f48ad491c88c6a8123468de3318c7d994d65ebd13aa"
        )
        hash(
            "hello world",
            "00d9f56ea4202532f8fd42b12943e6ee8ea6fbef70052a6563d041a1"
        )
        hash(
            "verystrongandlongpassword",
            "06119805e7bb87e654fc32ebef290e7659cf48501beee80655e9db6a"
        )
        hash(
            "The quick brown fox jumps over the lazy dog",
            "e4e5cb6c7cae41982b397bf7b7d2d9d1949823ae78435326e8db4912"
        )
        hash(
            "",
            "1fa1291e65248b37b3433475b2a0dd63d54a11ecc4e3e034e7bc1ef4"
        )
        hash(
            "abc",
            "0b033fc226df7abde29f67a05d3dc62cf271ef3dfea4d387407fbd55"
        )
        hash(
            "UPPERCASE",
            "c68f8c11e13f8598ac3d91a8e05f7a8167980ac41440e09be7fe0b4c"
        )
        hash(
            "123456789",
            "8b5b64be12d131e47ea3e1d7e2de47efb806461f6023c281f9e23cad"
        )
    }

    @Test
    fun test_Keyed() {
        hashKeyed(
            "",
            "dd5e0b97ab648294348d123aa109c97801f5a59dd85791e4edee187f"
        )
        hashKeyed(
            "A",
            "8407210b98c098d10e1d163439732389c82e8a4f79265716dea50893"
        )
        hashKeyed(
            "ABCDEFGHIJKLMNOPQRSTUVWXYZ",
            "cea702be7f0309009cd5c54d91df92ac0fb26c3fbb10dac669d03d30"
        )
    }
}
