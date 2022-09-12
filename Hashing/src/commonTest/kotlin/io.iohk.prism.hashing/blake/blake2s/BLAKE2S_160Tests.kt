package io.iohk.prism.hashing.blake.blake2s

import io.iohk.prism.hashing.BLAKE2S_160
import io.iohk.prism.hashing.internal.toHexString
import kotlin.test.Test
import kotlin.test.assertEquals

class BLAKE2S_160Tests {

    private fun hash(stringToHash: String, output: String) {
        val hash = BLAKE2S_160()
        assertEquals(output, hash.digest(stringToHash.encodeToByteArray()).toHexString())
    }

    private fun hashKeyed(stringToHash: String, output: String) {
        val hash = BLAKE2S_160.Keyed("hello".encodeToByteArray())
        assertEquals(output, hash.digest(stringToHash.encodeToByteArray()).toHexString())
    }

    @Test
    fun test_Strings() {
        hash(
            "blake2",
            "1e8182172353320bf8fbb2bede8806247dbe9215"
        )
        hash(
            "hello world",
            "5b61362bd56823fd6ed1d3bea2f3ff0d2a0214d7"
        )
        hash(
            "verystrongandlongpassword",
            "e0e2f0c3c17d5af9b8e9e0a8f3554c446025e87d"
        )
        hash(
            "The quick brown fox jumps over the lazy dog",
            "5a604fec9713c369e84b0ed68daed7d7504ef240"
        )
        hash(
            "",
            "354c9c33f735962418bdacb9479873429c34916f"
        )
        hash(
            "abc",
            "5ae3b99be29b01834c3b508521ede60438f8de17"
        )
        hash(
            "UPPERCASE",
            "3824db0550d59d304a0fb1a3b89bf3fa83662e9f"
        )
        hash(
            "123456789",
            "57c99b8345acf5a7b22f15db7742a36d4cb8313b"
        )
    }

    @Test
    fun test_Keyed() {
        hashKeyed(
            "",
            "790427e5843a8c57960c0d5b50e17a61d2b4185b"
        )
        hashKeyed(
            "A",
            "0eaf2bfa3b1564fea21ce5106072b7bdf8f179d0"
        )
        hashKeyed(
            "ABCDEFGHIJKLMNOPQRSTUVWXYZ",
            "a636d562437a961ddb7e3528ea87f3c5d7c3c37a"
        )
    }
}