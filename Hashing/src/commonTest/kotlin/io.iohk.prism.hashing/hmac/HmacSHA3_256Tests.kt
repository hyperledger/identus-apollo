package io.iohk.prism.hashing.hmac

import io.iohk.prism.hashing.SHA3_256
import io.iohk.prism.hashing.internal.toBinary
import io.iohk.prism.hashing.internal.toHexString
import kotlin.test.Test
import kotlin.test.assertEquals

class HmacSHA3_256Tests : BaseHmacHashTests() {

    override fun hash(key: ByteArray, stringToHash: ByteArray, outputLength: Int?): String {
        val hash = SHA3_256().createHmac(key, outputLength)
        return hash.digest(stringToHash).toHexString()
    }

    /**
     * From:
     * - https://fossies.org/linux/peazip/tv_hmac-sha3.txt
     * - https://github.com/bcgit/bc-java/blob/master/core/src/test/java/org/bouncycastle/crypto/test/SHA3HMacTest.java
     */

    @Test
    fun test_Strings() {
        assertEquals(
            "ba85192310dffa96e2a3a40e69774351140bb7185e1202cdcc917589f95e16bb",
            hash("0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b", "Hi There")
        )
        assertEquals(
            "c7d4072e788877ae3596bbb0da73b887c9171f93095b294ae857fbe2645e1ba5",
            hash("4a656665", "what do ya want for nothing?")
        )
        assertEquals(
            "ed73a374b96c005235f948032f09674a58c0ce555cfc1f223b02356560312c3b",
            hash("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", "Test Using Larger Than Block-Size Key - Hash Key First")
        )
        assertEquals(
            "65c5b06d4c3de32a7aef8763261e49adb6e2293ec8e7c61e8de61701fc63e123",
            hash("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", "This is a test using a larger than block-size key and a larger than block-size data. The key needs to be hashed before being used by the HMAC algorithm.")
        )
        assertEquals(
            "a6072f86de52b38bb349fe84cd6d97fb6a37c4c0f62aae93981193a7229d3467",
            hash("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", "Test Using Larger Than Block-Size Key - Hash Key First")
        )
        assertEquals(
            "e6a36d9b915f86a093cac7d110e9e04cf1d6100d30475509c2475f571b758b5a",
            hash("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", "This is a test using a larger than block-size key and a larger than block-size data. The key needs to be hashed before being used by the HMAC algorithm.")
        )
    }

    @Test
    fun test_Hexs() {
        assertEquals(
            "84ec79124a27107865cedd8bd82da9965e5ed8c37b0ac98005a7f39ed58a4207",
            hash("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", "dddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd".toBinary())
        )
        assertEquals(
            "57366a45e2305321a4bc5aa5fe2ef8a921f6af8273d7fe7be6cfedb3f0aea6d7",
            hash("0102030405060708090a0b0c0d0e0f10111213141516171819", "cdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcd".toBinary())
        )
        assertEquals(
            "4fe8e202c4f058e8dddc23d8c34e467343e23555e24fc2f025d598f558f67205",
            hash("000102030405060708090a0b0c0d0e0f101112131415161718191a1b1c1d1e1f", "53616d706c65206d65737361676520666f72206b65796c656e3c626c6f636b6c656e".toBinary())
        )
        assertEquals(
            "68b94e2e538a9be4103bebb5aa016d47961d4d1aa906061313b557f8af2c3faa",
            hash("000102030405060708090a0b0c0d0e0f101112131415161718191a1b1c1d1e1f202122232425262728292a2b2c2d2e2f303132333435363738393a3b3c3d3e3f404142434445464748494a4b4c4d4e4f505152535455565758595a5b5c5d5e5f606162636465666768696a6b6c6d6e6f707172737475767778797a7b7c7d7e7f8081828384858687", "53616d706c65206d65737361676520666f72206b65796c656e3d626c6f636b6c656e".toBinary())
        )
        assertEquals(
            "9bcf2c238e235c3ce88404e813bd2f3a97185ac6f238c63d6229a00b07974258",
            hash("000102030405060708090a0b0c0d0e0f101112131415161718191a1b1c1d1e1f202122232425262728292a2b2c2d2e2f303132333435363738393a3b3c3d3e3f404142434445464748494a4b4c4d4e4f505152535455565758595a5b5c5d5e5f606162636465666768696a6b6c6d6e6f707172737475767778797a7b7c7d7e7f808182838485868788898a8b8c8d8e8f909192939495969798999a9b9c9d9e9fa0a1a2a3a4a5a6a7", "53616d706c65206d65737361676520666f72206b65796c656e3e626c6f636b6c656e".toBinary())
        )
    }

    @Test
    fun test_Truncation() {
        assertEquals(
            "6e02c64537fb118057abb7fb66a23b3c",
            hash("0c0c0c0c0c0c0c0c0c0c0c0c0c0c0c0c0c0c0c0c", "Test With Truncation", 16)
        )
        assertEquals(
            "c8dc7148d8c1423aa549105dafdf9cad",
            hash("000102030405060708090a0b0c0d0e0f101112131415161718191a1b1c1d1e1f", "53616d706c65206d65737361676520666f72206b65796c656e3c626c6f636b6c656e2c2077697468207472756e636174656420746167".toBinary(), 16)
        )
    }
}
