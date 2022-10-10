package io.iohk.prism.apollo.hashing.hmac

import io.iohk.prism.apollo.hashing.MD5
import io.iohk.prism.apollo.hashing.internal.JsIgnore
import io.iohk.prism.apollo.hashing.internal.toBinary
import io.iohk.prism.apollo.hashing.internal.toHexString
import kotlin.test.Test
import kotlin.test.assertEquals

@JsIgnore
class HmacMD5Tests : BaseHmacHashTests() {

    override fun hash(key: ByteArray, stringToHash: ByteArray, outputLength: Int?): String {
        val hash = MD5().createHmac(key, outputLength)
        return hash.digest(stringToHash).toHexString()
    }

    /**
     * From:
     * - RFC 2104
     * - https://datatracker.ietf.org/doc/html/rfc2202.html
     * - https://github.com/crypto-browserify/hash-test-vectors/blob/master/hmac.json
     * - https://github.com/xsc/pandect/blob/main/test/pandect/hmac_test.clj
     */

    @Test
    fun test_Strings() {
        assertEquals(
            "9294727a3638bb1c13f48ef8158bfc9d",
            hash("0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b", "Hi There")
        )
        assertEquals(
            "80070713463e7749b90c2dc24911e275",
            hash("6b6579", "The quick brown fox jumps over the lazy dog")
        )
        assertEquals(
            "e9139d1e6ee064ef8cf514fc7dc83e86",
            hash("", "More text test vectors to stuff up EBCDIC machines :-)")
        )
        assertEquals(
            "750c783e6ab0b503eaa86e310a5db738",
            hash("Jefe".encodeToByteArray(), "what do ya want for nothing?".encodeToByteArray())
        )
        assertEquals(
            "80070713463e7749b90c2dc24911e275",
            hash("6b6579", "The quick brown fox jumps over the lazy dog")
        )
    }

    @Test
    fun test_Hexs() {
        assertEquals(
            "56be34521d144c88dbb8c733f0e8b3f6",
            hash(
                "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
                "DDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD".toBinary()
            )
        )
        assertEquals(
            "f1bbf62a07a5ea3e72072d12e9e25014",
            hash(
                "4a656665",
                "7768617420646f2079612077616e74207768617420646f2079612077616e7420".toBinary()
            )
        )
        assertEquals(
            "2ab8b9a9f7d3894d15ad8383b97044b2",
            hash(
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
                "dddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd".toBinary()
            )
        )
        assertEquals(
            "697eaf0aca3a3aea3a75164746ffaa79",
            hash(
                "0102030405060708090a0b0c0d0e0f10111213141516171819",
                "cdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcd".toBinary()
            )
        )
    }

    @Test
    fun test_Truncation() {
        assertEquals(
            "56461ef2342edc00f9bab995",
            hash("0c0c0c0c0c0c0c0c0c0c0c0c0c0c0c0c", "Test With Truncation", 12)
        )
    }

    @Test
    fun test_LargerThanBlockSizeKey() {
        assertEquals(
            "6b1ab7fe4bd7bf8f0b62e6ce61b9d0cd",
            hash(
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
                "Test Using Larger Than Block-Size Key - Hash Key First"
            )
        )
    }

    @Test
    fun test_LargerThanBlockSizeKeyAndLargerThanOneBlockSizeData() {
        assertEquals(
            "6f630fad67cda0ee1fb1f562db3aa53e",
            hash(
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
                "Test Using Larger Than Block-Size Key and Larger Than One Block-Size Data"
            )
        )
    }

    @Test
    fun test_Seq() {
        val expectedOutput = listOf(
            "c91e40247251f39bdfe6a7b72a5857f9",
            "00ff2644d0e3699f677f58ecdf57082f",
            "1b6c2db6819a4f023ffe21b91e284e93",
            "04b0ed3e73fbb9a94444fdffaa530695",
            "1557a22261110dfb31ace25936bde45d",
            "54c5a67a9cb4544ca66bbda1a2b8479e",
            "f803d9e43c934545af078ffbb34bc30b",
            "32f56ea655df36d845e430d637c85d17",
            "14bd2095f4a478c10eebff379de76dd3",
            "aaf6867b3fa01dd26312b0dfd6371a2a",
            "0fa2a6fefebe7ce3c31a38400f8ab260",
            "54c37be13b7333287d0e74aa9d9227f6",
            "385d75a58b0c95e5cdc059db168bd1d2",
            "e73003103ed65c08e62d46ae1e1b771a",
            "278ed4a4ebea1ffa5eec874f198c0cc0",
            "f65ce9eea7fdb90b9cc603329d3fb9a9",
            "8640836944ee0009b2cc6fdc3f5c39e1",
            "7819a99f82babdf060aa51ae109629db",
            "ef26336668486c76921d1dab67ed5673",
            "13ed7bc140f1496e09ad29c644586957",
            "5fdd337ce9c4ac8d910833fcc2bd837e",
            "e9470246abf7cf4d37fd378738d8f763",
            "384a75c33effa12eb69187bb80df843b",
            "63866a5406b9ea0341032fcfd0244a4b",
            "8042f8572c8a9b88e135acb83ef1fd39",
            "bd1be6af2d022f966f612569e191f0e9",
            "9f70c839533ee4c7b3cf20c6fb65c94c",
            "800a5ce92ca4fee6f1d353f496113873",
            "c35e93e1e54c84c4389d2de71e1b9846",
            "a130ef5f91465f5a56999f450e63f4f9",
            "5f16564e05285a099f628245df9a3c2a",
            "a34f7e3df06dd84cc67e8a922240d60b",
            "945e50753b6e6c920183822d5f280f10",
            "2ddd269dbcdf5c21a1c3fd540ff4aba9",
            "212fe3e2cef7df74fc01cc2cc83119b8",
            "d98b2930011649f16c08bc8c0178d838",
            "e39e21026111c1efb0c491c0fdfa841d",
            "ae46de06c3b0d2cec35352c95a1003f0",
            "5550ee50bf88c9de5ada34567fe044c7",
            "6bc486627760373eacff508f7032bf31",
            "ae6e0b8dbcfdcca4b3449b57647d5ae5",
            "6be5a0f140dfc4b75439630e6f9a36ee",
            "e3e4e735bfe79397d4653a6243df1925",
            "68c1d9e8973a3f6b92b588469d68a2a5",
            "956132d512118d5f446c8cb912b924d9",
            "df5c2ad650b3ca7a89ebf92ee618c845",
            "14d375cf7e4294ed99135e4237414f01",
            "db966d40b447692e2d13cc0c09c1b495",
            "53dadcf1c6b99bd403052a1ce1ed0d14",
            "dec4a3c1db8f6aa4515c512c9299c4dc",
            "3b3a51dd83ab1dc56a7f0cbe1c71923f",
            "03c73353b3203ef9cdb95f9db8750af1",
            "ed9e15fd86d66da2d546d2bfc55041ad",
            "81b649338f9db1c6e592427d38221c7c",
            "92e170e13bf40ff65e3b4c665f222dd5",
            "00d5e23f5f829b21d454c4445851ab53",
            "39057029af0b3f4391a7bdc6ddce4d07",
            "2deacefa698f9ccad5198c4e17e69a93",
            "ad35fd52ea199e26948009df3546d3a2",
            "4c42cf2cfd4d8fd9a06e3f73d02fe818",
            "4d7c893e4313fff72103854463414277",
            "3f04e8b32ab56eaf216503e46bd7aebe",
            "f015ddc3eef41ecc93e944fa3577db52",
            "31f77a50a2ed96ed8e4a3ce04b9daa23",
            "fbf481373481756e0c88978f7e0809a2",
            "7d8d793b287c04e7d2896d76eaa5ca15",
            "dac74aebecc2385dd9d0c3147cca1f78",
            "f6dde50d37b460ff5e8b4c03a0854bd5",
            "5710d6a54a2124e06a6dadbe9bf76119",
            "19db5d13a53e57184759f33976537aa5",
            "848dd8d32130626fbd11b0133c2a29e3",
            "4f75be04bf2f6dd85d048db82f19c38c",
            "4ae9436540ed24bcb5ec62977ac90789",
            "859d1a9fc2b795ad60f24a37eb9ef890",
            "cd45865317fd17b652de9f9ebbba16b6",
            "52313319d395f453ba2c0a0159cf180b",
            "a7b190c0eecacca4dfc5b45dfb324718",
            "23e85cae85b50f45f7f48ee0f22fde85",
            "6a80dbff139a5345235ef76586cfcbc7",
            "850e638fce5a2f3b1d1fe9c28f05ef49",
            "797cdc3f7e271fc9a3d0566a905d1cfe",
            "030ce97a9a0b1d5403e253d883fcaf12",
            "648ffff44e416d9de606ba0ddb751194",
            "fe15098e0dac65fa8ee45cac67121cc9",
            "17c90ecd390a8b41046b4c7fa0354e4f",
            "7d149dff5f6379b7dbf5c401db6d2976",
            "8d055a4701dd51cb9d1af8e2ae59bd21",
            "f3481cb07b034eb4a023d00d4fda9a86",
            "feb22562ffaaa9cce5cdda34c29e55c3",
            "a620aa447216709d8ce5c5f23474ecf8",
            "f25fcbb2bf7440c5e3c5b53092b8c828",
            "dbbae1cf60bbca0b05edea0b362f0a33",
            "e18e85bcb4633a797faf7975cef44b84",
            "1be27eec72c2ede151978705c7c7ded2",
            "a15d36c5c5bed77699838832fc225dd8",
            "08f31e68bfbbb420742f80b20b69be8c",
            "5e9b4b5b3228f533ba8efc3c0b9aad3d",
            "1239ba6d941d1d8ad2ed561bf517d4b4",
            "5233f50218e0d097efcc68f1536f30ae",
            "340b47c78b003272eaa4b9d22c3b0542",
            "e7f11759fe8a897364c21767570885bb",
            "054bd6acbfd5421c0290b0839c0a0acc",
            "cc0748f7b2cc921cf5fa019f955066c9",
            "a4df167697949b1aedbba3226a334baa",
            "29893b9776ba5e750a9fcea37b0116ae",
            "2dc25c935f006f7965fab3256d77004d",
            "24089811fff2189fb9af38651f43977d",
            "0e048569d634bf652cd8ebf859c9b69a",
            "00386b569dab73844a708ba5b48bbaa8",
            "8033e1affbe1218f81c8331343fbe5b5",
            "9b82008a34f3847c1204aca89f3d57d1",
            "be1a529f88aa05a42afc40f663e97849",
            "5237637aa645e83b0e56a1361ab80643",
            "15bc4405e891adaf48fa56d4356705d5",
            "0820087438832b63aadc479cfc88bdbf",
            "b1e3ba7e96605d5ff614b1bec1f57ac1",
            "838a096d64e6c0ddb069dc89e4c3f839",
            "934bce159f3959a933c87ab497ca8d42",
            "ca501f1de619a570dc38fdcb8b3f7722",
            "033b27d5994a6f5d5f6800539b69e876",
            "b447fc68fef4e3cf9290b06eb6aecaa3",
            "dd3d3f72f0f1fbcd030d839dcfee457a",
            "ee73c4c996e0150d93b3144f20fb2c1b",
            "5af9679d2441542391c6a903fd8c1626",
            "2bd84b87230511dae7256b62a46aa45e",
            "eb159e5694c191f7708951ebc0aaf135",
            "60f02efe1dafaacf65f6664a2321b153",
            "14e5a0e90d4420e765c4324b68174f46",
            "09f1503bcd00e3a1b965b66b9609e998"
        )

        var key = ByteArray(16) {
            it.toByte()
        }

        expectedOutput.forEachIndexed { index, output ->
            assertEquals(output, hash(key, ByteArray(index) { it.toByte() }))
            key = output.toBinary()
        }
    }
}
