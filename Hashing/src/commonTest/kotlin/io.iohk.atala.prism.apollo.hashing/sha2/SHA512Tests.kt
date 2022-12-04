package io.iohk.atala.prism.apollo.hashing.sha2

import io.iohk.atala.prism.apollo.hashing.BaseHashTests
import io.iohk.atala.prism.apollo.hashing.SHA512
import io.iohk.atala.prism.apollo.hashing.internal.toHexString
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals

class SHA512Tests : BaseHashTests() {
    override val valueForHash: List<String>
        get() = listOf(
            "cf83e1357eefb8bdf1542850d66d8007d620e4050b5715dc83f4a921d36ce9ce47d0d13c5d85f2b0ff8318d2877eec2f63b931bd47417a81a538327af927da3e",
            "ddaf35a193617abacc417349ae20413112e6fa4e89a97ea20a9eeee64b55d39a2192992a274fc1a836ba3c23a3feebbd454d4423643ce80e2a9ac94fa54ca49f",
            "204a8fc6dda82f0a0ced7beb8e08a41657c16ef468b228a8279be331a703c33596fd15c13b1b07f9aa1d3bea57789ca031ad85c7a71dd70354ec631238ca3445",
            "8e959b75dae313da8cf4f72814fc143f8f7779c6eb9f7fa17299aeadb6889018501d289e4900f7e4331b99dec4b5433ac7d329eeb6dd26545e96e55b874be909",
            "1f40fc92da241694750979ee6cf582f2d5d7d28e18335de05abc54d0560e0f5302860c652bf08d560252aa5e74210546f369fbbbce8c12cfc7957b2652fe9a75",
            "90d1bdb9a6cbf9cb0d4a7f185ee0870456f440b81f13f514f4561a08112763523033245875b68209bb1f5d5215bac81e0d69f77374cc44d1be30f58c8b615141"
        )

    override fun hash(stringToHash: String): String {
        val hash = SHA512()
        return hash.digest(stringToHash.encodeToByteArray()).toHexString()
    }

    @Test
    fun test_Strings() {
        for (i in stringsToHash.indices) {
            assertEquals(valueForHash[i], hash(stringsToHash[i]), "failed with hashing ${stringsToHash[i]}")
        }
        assertEquals("4dbff86cc2ca1bae1e16468a05cb9881c97f1753bce3619034898faa1aabe429955a1bf8ec483d7421fe3c1646613a59ed5441fb0f321389f77f48a879c7b1f1", hash("abcdefghijklmnopqrstuvwxyz"))
        assertEquals("1e07be23c26a86ea37ea810c8ec7809352515a970e9253c26f536cfc7a9996c45c8370583e0a78fa4a90041d71a4ceab7423f19c71b9d5a3e01249f0bebd5894", hash("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"))
        assertEquals("72ec1ef1124a45b047e8b7c75a932195135bb61de24ec0d1914042246e0aec3a2354e093d76f3048b456764346900cb130d2a4fd5dd16abb5e30bcb850dee843", hash("12345678901234567890123456789012345678901234567890123456789012345678901234567890"))
    }

    @Test
    fun test_MillionA() {
        assertEquals("e718483d0ce769644e2e42c7bc15b4638e1f98b13b2044285632a803afa973ebde0ff244877ea60a4cb0432ce577c31beb009c5c2c49aa2e4eadb217ad8cc09b", hash("a".repeat(1_000_000)), "failed with 1 million a")
    }

    /**
     * The following tests from:
     *  - https://csrc.nist.gov/CSRC/media/Projects/Cryptographic-Standards-and-Guidelines/documents/examples/SHA512.pdf
     *  - https://csrc.nist.gov/CSRC/media/Projects/Cryptographic-Standards-and-Guidelines/documents/examples/SHA2_Additional.pdf
     */
    @Test
    fun test_nist0Byte() {
        val hash = SHA512()
        assertEquals("cf83e1357eefb8bdf1542850d66d8007d620e4050b5715dc83f4a921d36ce9ce47d0d13c5d85f2b0ff8318d2877eec2f63b931bd47417a81a538327af927da3e", hash.digest(ByteArray(0)).toHexString())
    }

    @Test
    fun test_nist111x0() {
        val hash = SHA512()
        assertEquals("77ddd3a542e530fd047b8977c657ba6ce72f1492e360b2b2212cd264e75ec03882e4ff0525517ab4207d14c70c2259ba88d4d335ee0e7e20543d22102ab1788c", hash.digest(ByteArray(111) { 0 }).toHexString())
    }

    @Test
    fun test_nist112x0() {
        val hash = SHA512()
        assertEquals("2be2e788c8a8adeaa9c89a7f78904cacea6e39297d75e0573a73c756234534d6627ab4156b48a6657b29ab8beb73334040ad39ead81446bb09c70704ec707952", hash.digest(ByteArray(112) { 0 }).toHexString())
    }

    @Test
    fun test_nist113x0() {
        val hash = SHA512()
        assertEquals("0e67910bcf0f9ccde5464c63b9c850a12a759227d16b040d98986d54253f9f34322318e56b8feb86c5fb2270ed87f31252f7f68493ee759743909bd75e4bb544", hash.digest(ByteArray(113) { 0 }).toHexString())
    }

    @Test
    fun test_nist122x0() {
        val hash = SHA512()
        assertEquals("4f3f095d015be4a7a7cc0b8c04da4aa09e74351e3a97651f744c23716ebd9b3e822e5077a01baa5cc0ed45b9249e88ab343d4333539df21ed229da6f4a514e0f", hash.digest(ByteArray(122) { 0 }).toHexString())
    }

    @Test
    fun test_nist1000x00() {
        val hash = SHA512()
        assertEquals("ca3dff61bb23477aa6087b27508264a6f9126ee3a004f53cb8db942ed345f2f2d229b4b59c859220a1cf1913f34248e3803bab650e849a3d9a709edc09ae4a76", hash.digest(ByteArray(1000) { 0 }).toHexString())
    }

    @Test
    fun test_nist1000xA() {
        val hash = SHA512()
        assertEquals("329c52ac62d1fe731151f2b895a00475445ef74f50b979c6f7bb7cae349328c1d4cb4f7261a0ab43f936a24b000651d4a824fcdd577f211aef8f806b16afe8af", hash.digest(ByteArray(1000) { 'A'.code.toByte() }).toHexString())
    }

    @Test
    fun test_nist1005xU() {
        val hash = SHA512()
        assertEquals("59f5e54fe299c6a8764c6b199e44924a37f59e2b56c3ebad939b7289210dc8e4c21b9720165b0f4d4374c90f1bf4fb4a5ace17a1161798015052893a48c3d161", hash.digest(ByteArray(1005) { 'U'.code.toByte() }).toHexString())
    }

    @Test
    fun test_nist1million() {
        val hash = SHA512()
        assertEquals("ce044bc9fd43269d5bbc946cbebc3bb711341115cc4abdf2edbc3ff2c57ad4b15deb699bda257fea5aef9c6e55fcf4cf9dc25a8c3ce25f2efe90908379bff7ed", hash.digest(ByteArray(1000000) { 0 }).toHexString())
    }

    @Test
    @Ignore // Java Heap Size
    fun test_nist536870912xZ() {
        val hash = SHA512()
        assertEquals("da172279f3ebbda95f6b6e1e5f0ebec682c25d3d93561a1624c2fa9009d64c7e9923f3b46bcaf11d39a531f43297992ba4155c7e827bd0f1e194ae7ed6de4cac", hash.digest(ByteArray(0x20000000) { 'Z'.code.toByte() }).toHexString())
    }

    @Test
    @Ignore // Java Heap Size
    fun test_nist1090519040x00() {
        val hash = SHA512()
        assertEquals("14b1be901cb43549b4d831e61e5f9df1c791c85b50e85f9d6bc64135804ad43ce8402750edbe4e5c0fc170b99cf78b9f4ecb9c7e02a157911d1bd1832d76784f", hash.digest(ByteArray(0x41000000) { 0 }).toHexString())
    }

    @Test
    @Ignore // Java Heap Size
    fun test_nist1610612798xB() {
        val hash = SHA512()
        assertEquals("fd05e13eb771f05190bd97d62647157ea8f1f6949a52bb6daaedbad5f578ec59b1b8d6c4a7ecb2feca6892b4dc138771670a0f3bd577eea326aed40ab7dd58b1", hash.digest(ByteArray(0x6000003e) { 'B'.code.toByte() }).toHexString())
    }
}
