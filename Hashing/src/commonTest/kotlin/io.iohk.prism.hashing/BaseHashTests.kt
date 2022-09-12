package io.iohk.prism.hashing

import kotlin.test.assertEquals

abstract class BaseHashTests {
    /**
     * From https://www.di-mgt.com.au/sha_testvectors.html
     */
    protected val stringsToHash = listOf(
        "", // Input message: the empty string "", the bit string of length 0
        "abc", // Input message: "abc", the bit string (0x)616263 of length 24 bits.
        "abcdbcdecdefdefgefghfghighijhijkijkljklmklmnlmnomnopnopq", // Input message: "abcdbcdecdefdefgefghfghighijhijkijkljklmklmnlmnomnopnopq" (length 448 bits)
        "abcdefghbcdefghicdefghijdefghijkefghijklfghijklmghijklmnhijklmnoijklmnopjklmnopqklmnopqrlmnopqrsmnopqrstnopqrstu", // Input message: "abcdefghbcdefghicdefghijdefghijkefghijklfghijklmghijklmnhijklmnoijklmnopjklmnopqklmnopqrlmnopqrsmnopqrstnopqrstu" (length 896 bits)
        "a", // Input message: one million (1,000,000) repetitions of the character "a" (0x61)
        "abcdefghbcdefghicdefghijdefghijkefghijklfghijklmghijklmnhijklmno"// Input message: the extremely-long message "abcdefghbcdefghicdefghijdefghijkefghijklfghijklmghijklmnhijklmno" repeated 16,777,216 times
    )
    abstract val valueForHash: List<String>

    init {
        assertEquals(stringsToHash.size, valueForHash.size, "Must be same size")
    }

    abstract fun hash(stringToHash: String): String
}
