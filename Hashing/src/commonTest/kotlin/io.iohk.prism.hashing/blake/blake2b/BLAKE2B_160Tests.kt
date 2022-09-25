package io.iohk.prism.hashing.blake.blake2b

import io.iohk.prism.hashing.BLAKE2B_160
import io.iohk.prism.hashing.internal.toHexString
import kotlin.test.Test
import kotlin.test.assertEquals

class BLAKE2B_160Tests {

    private fun hash(stringToHash: String, output: String) {
        val hash = BLAKE2B_160()
        assertEquals(output, hash.digest(stringToHash.encodeToByteArray()).toHexString())
    }

    private fun hashKeyed(stringToHash: String, output: String) {
        val hash = BLAKE2B_160.Keyed("hello".encodeToByteArray())
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
            "ad55cb15ca0ac08f485292537aca1ecdf6bb2c3c"
        )
        hash(
            "hello world",
            "70e8ece5e293e1bda064deef6b080edde357010f"
        )
        hash(
            "verystrongandlongpassword",
            "36e6349976400e8fa8fc52e5fdfffef5dae40f47"
        )
        hash(
            "The quick brown fox jumps over the lazy dog",
            "3c523ed102ab45a37d54f5610d5a983162fde84f"
        )
        hash(
            "",
            "3345524abf6bbe1809449224b5972c41790b6cf2"
        )
        hash(
            "abc",
            "384264f676f39536840523f284921cdc68b6846b"
        )
        hash(
            "UPPERCASE",
            "c96d1be9b55143039a82b31b2bc504ec23b67b16"
        )
        hash(
            "123456789",
            "f34f0bb8223b921e1fffeecca699db4a66edf1a8"
        )
    }

    @Test
    fun test_Keyed() {
        hashKeyed(
            "",
            "1c86f0e50b66458902b1b583098f789e8414c888"
        )
        hashKeyed(
            "A",
            "612ce2bfc91df8db2c992e8dd80f58fe917294ba"
        )
        hashKeyed(
            "ABCDEFGHIJKLMNOPQRSTUVWXYZ",
            "b605f0aa5a514513bf4f549a5ac62c73d0bf349a"
        )
    }
}
