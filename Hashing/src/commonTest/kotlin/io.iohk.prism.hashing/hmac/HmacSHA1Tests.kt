package io.iohk.prism.hashing.hmac

import io.iohk.prism.hashing.SHA1
import io.iohk.prism.hashing.internal.toBinary
import io.iohk.prism.hashing.internal.toHexString
import kotlin.test.Test
import kotlin.test.assertEquals

class HmacSHA1Tests : BaseHmacHashTests() {

    override fun hash(key: ByteArray, stringToHash: ByteArray, outputLength: Int?): String {
        val hash = SHA1().createHmac(key, outputLength)
        return hash.digest(stringToHash).toHexString()
    }

    /**
     * From:
     * - NIST test data
     * - FIPS 198a
     * - OpenSSL
     * - https://datatracker.ietf.org/doc/html/rfc2202.html
     * - https://github.com/crypto-browserify/hash-test-vectors/blob/master/hmac.json
     * - https://github.com/xsc/pandect/blob/main/test/pandect/hmac_test.clj
     */

    @Test
    fun test_Strings() {
        assertEquals(
            "b617318655057264e28bc0b6fb378c8ef146be00",
            hash("0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b", "Hi There")
        )
        assertEquals(
            "5fd596ee78d5553c8ff4e72d266dfd192366da29",
            hash("000102030405060708090A0B0C0D0E0F101112131415161718191A1B1C1D1E1F202122232425262728292A2B2C2D2E2F303132333435363738393A3B3C3D3E3F", "Sample message for keylen=blocklen")
        )
        assertEquals(
            "4c99ff0cb1b31bd33f8431dbaf4d17fcd356a807",
            hash("000102030405060708090A0B0C0D0E0F10111213", "Sample message for keylen<blocklen")
        )
        assertEquals(
            "2d51b2f7750e410584662e38f133435f4c4fd42a",
            hash("000102030405060708090A0B0C0D0E0F101112131415161718191A1B1C1D1E1F202122232425262728292A2B2C2D2E2F303132333435363738393A3B3C3D3E3F404142434445464748494A4B4C4D4E4F505152535455565758595A5B5C5D5E5F60616263", "Sample message for keylen=blocklen")
        )
        assertEquals(
            "effcdf6ae5eb2fa2d27416d5f184df9c259a7c79",
            hash("4a656665", "what do ya want for nothing?")
        )
        assertEquals(
            "4c99ff0cb1b31bd33f8431dbaf4d17fcd356a807",
            hash("000102030405060708090A0B0C0D0E0F10111213", "Sample message for keylen<blocklen")
        )
        assertEquals(
            "61afdecb95429ef494d61fdee15990cabf0826fc",
            hash("", "My test data")
        )
        assertEquals(
            "7dbe8c764c068e3bcd6e6b0fbcd5e6fc197b15bb",
            hash("3132333435", "My test data")
        )
        assertEquals(
            "4f4ca3d5d68ba7cc0a1208c9c61e9c5da0403c0a",
            hash("000102030405060708090A0B0C0D0E0F101112131415161718191A1B1C1D1E1F202122232425262728292A2B2C2D2E2F303132333435363738393A3B3C3D3E3F", "Sample #1")
        )
        assertEquals(
            "0922d3405faa3d194f82a45830737d5cc6c75d24",
            hash("303132333435363738393A3B3C3D3E3F40414243", "Sample #2")
        )
        assertEquals(
            "bcf41eab8bb2d802f3d05caf7cb092ecf8d1a3aa",
            hash("505152535455565758595A5B5C5D5E5F606162636465666768696A6B6C6D6E6F707172737475767778797A7B7C7D7E7F808182838485868788898A8B8C8D8E8F909192939495969798999A9B9C9D9E9FA0A1A2A3A4A5A6A7A8A9AAABACADAEAFB0B1B2B3", "Sample #3")
        )
        assertEquals(
            "de7c9b85b8b78aa6bc8a7a36f70a90701c9db4d9",
            hash("6b6579", "The quick brown fox jumps over the lazy dog")
        )
    }

    @Test
    fun test_Hexs() {
        assertEquals(
            "2fdb9bc89cf09e0d3a0bc1f1b89ba8359db9d93f",
            hash("4a656665", "7768617420646f2079612077616e74207768617420646f2079612077616e7420".toBinary())
        )
        assertEquals(
            "125d7342b9ac11cd91a39af48aa17b4f63f175d3",
            hash("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", "dddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd".toBinary())
        )
        assertEquals(
            "4c9007f4026250c6bc8414f9bf50c86c2d7235da",
            hash("0102030405060708090a0b0c0d0e0f10111213141516171819", "cdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcd".toBinary())
        )
    }

    @Test
    fun test_Truncation() {
        assertEquals(
            "4c1a03424b55e07fe7f27be1d58bb932",
            hash("0c0c0c0c0c0c0c0c0c0c0c0c0c0c0c0c0c0c0c0c", "Test With Truncation", 16)
        )
        assertEquals(
            "9ea886efe268dbecce420c75",
            hash("707172737475767778797A7B7C7D7E7F808182838485868788898A8B8C8D8E8F909192939495969798999A9B9C9D9E9FA0", "Sample #4", 12)
        )
    }

    @Test
    fun test_LargerThanBlockSizeKeyAndLargerThanOneBlockSizeData() {
        assertEquals(
            "217e44bb08b6e06a2d6c30f3cb9f537f97c63356",
            hash(
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
                "This is a test using a larger than block-size key and a larger than block-size data. The key needs to be hashed before being used by the HMAC algorithm."
            )
        )
        assertEquals(
            "aa4ae5e15272d00e95705637ce8a3b55ed402112",
            hash("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", "Test Using Larger Than Block-Size Key - Hash Key First")
        )
        assertEquals(
            "e8e99d0f45237d786d6bbaa7965c7808bbff1a91",
            hash("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", "Test Using Larger Than Block-Size Key and Larger Than One Block-Size Data")
        )
    }

    @Test
    fun test_Seq() {
        val expectedOutput = listOf(
            "06e8ad50fc1035823661d979e2968968cecd03d9",
            "0ce34deaad5cf1131d9528fab8e46e12f8fe3052",
            "23924849643d03bbeac71755a878a83bd83f5280",
            "6119dd9a7024a23f293a3b67efa2bf1d82ec0220",
            "379dc76ac2d322fd8e5117cca765391bc0e10942",
            "7897cc86cff17a3f95c7af02cca03546f5cc2368",
            "1fa1ef3980e86b8df2c8e744309381727ed10e8e",
            "03b2b726d71dac6a2bee63eaa09631da78f5958b",
            "b8cac4c104997a547374803b5898057b3f8110a9",
            "e165e07f8d542fb288c7d367198d0618de3c9917",
            "18125f046c675f434b3c53a28c301fb2d91b5d34",
            "faab993f2feae442d28fdbb613d2c768ed13342d",
            "b657e7ee3a65c6484d007e21484813d9aed1264c",
            "eeec2bb7bac158742711ed13090fa20462a5e5c0",
            "12367f3a4e1501d32d1731b39cd2db2c5df5d011",
            "57dd9da36e7a4e567a2c5ae9f6230cf661855d90",
            "e37110ddd295d93990c4531d95564e74c0ebe264",
            "b2115c4e923ec640e5b4b507f7bc97fe700e12dd",
            "ed20c67345867ab07e9171b06c9b3b2928f43188",
            "6ca7dfc9f8f432ded42e4efe9f2d70d82507802d",
            "b39eb4d2c190e0ce8fa2c994e92d18cfbcd8f736",
            "91be5abf1b35f6227772e36337f258420cf51314",
            "eb957199ef666c6d0eacc64fc4261d11c715bb23",
            "2a18d8d4ab1f8c528c9d368bf5a7cffc2168d067",
            "d4dc370d482d82932701df8ceac9337682c2551b",
            "db9665a6a26dbde20238f04e9f1a368d26564e4f",
            "d5ae212c9e543f2656699b59deed54caaca9a071",
            "be8890f9dec6a02ae2848d8505b6408e884e6d1a",
            "e8d9dd9faa3080560b0ede798b745fee2a1e5479",
            "e219219d2cb8c363c2687f578446ade1c0404287",
            "e8e7767b35ed8d0965f68272ace61924cb044262",
            "1b26689c1ef55448a61dfaef98b6e7206a9675ea",
            "fe850390864e98a17fc43c3c871383169741b46d",
            "3f63068d536a282c53e5c003bceec96646cf7455",
            "2962c292ce247f11acb7e1f981447c51e9bbe63c",
            "b28909a2b7b2e0e13fdcb1124b0bdc31d7d2fede",
            "8da0fc30c8322dabd67d61e82fc92351894789ac",
            "543dac6d449fe2ddc3201927d08695f68f832905",
            "371540f3092f77867f0ca9da69318c7673f68388",
            "7eaf32204ea5993c87e9a12c67ada4c85d253281",
            "fc4994baa05f592901085ed7da188ec3a9bf36e3",
            "ebfe77592ef34e81bda05305876411484dc0744f",
            "25f64e8f076305d6f5741ea58232f68b725b8f6e",
            "5dba03f7e4b4226666f0d8d5bf49fee77951d121",
            "98e1d56d723dcacf227d2ac67bf2d6e7fd013497",
            "53550bc55a367d87416ffa25261362e7d4618da2",
            "b18434bcccc5f08b35397c1a6684d60f4f3a452f",
            "ff2bf38dfc6909b46a01e055d173f67a7e456341",
            "dafa445432ed37fec99059db8a0bc528e788e95d",
            "7ff823c570f8b4c0e483165c076aea7b5e727632",
            "bc4fc948ab621fe1419cf6006dc04e7d7b32fa23",
            "1678afcc3fbd1063e7c82cacad5b6a933a93091a",
            "97dc2f9f56738fdaffd555bf09274153fc2fd009",
            "74f5cb4f0900441b7affc278c01a3038df3d60c8",
            "021f66143270c9d58f26ab193dba81a811917cbc",
            "f486d1c8127813feeea8a693c4b8ecb5bb53c3a2",
            "8397cab8eed5b2164fec6be688971dfa2138934e",
            "e4477ce9bf8cc5a4ccde039b4e3000f1a0f4153a",
            "d6d2d1e3ee4d643ac4b38836ae54e846f99b376d",
            "9545b2c6279371d4d928aee24328121d43de1e5e",
            "947ed38ec087c4e53f417e8216408863a8ebfcb2",
            "32518a2326acde1e962b3d0d2bf950f318894e83",
            "5d21d368fb9d879adc27b341d608bcf860ab14f4",
            "e2bedd94d565a51915b1ec6fa9de18c62d12533a",
            "15abf657db6473c9e2f017c7a2f4dba3ce7f33dd",
            "0c9daf8d959dae3b66ff8a21a94bafc523abc462",
            "a36be72b501d435cb627c4555a426c4adaf3d666",
            "1c171979d67a014a0422d6c3561c817a354cf67d",
            "b75485b08ed052a1f4c3bacce3c563df4ba82418",
            "17297624219c5955b3af81e5ed61c6a5d05bd54d",
            "38a9ac8544f0ef24a623433c05e7f068430da13e",
            "1e9eeead73e736d7b4f5abb87ba0faba623fb2e5",
            "4b9d59879eac80e4dab3537e9ca9a877f7fae669",
            "7f76f2f875b2674b826c18b118942fbf1e75be55",
            "1716a7804a9a5abc9e737bdf5189f2784ce4f54b",
            "168027edf2a2641f364af5df1cb277a6e944ea32",
            "fbc67ded8c1a1bebbbc974e4787d2ba3205f2b1b",
            "33dd26c53f3914fecf26d287e70e85d6971c3c41",
            "97906268286cd38e9c7a2faf68a973143d389b2f",
            "45c55948d3e062f8612ec98fee91143ab17bcfc8",
            "ae1337c129df65513480e57e2a82b595096bf50f",
            "cec4b5351f038ebcfda4787b5de44ed8da30cd36",
            "6156a6742d90a212a02e3a7d4d7496b11abcfc3c",
            "3040f072df33ebf813da5760c6eb433270f33e8e",
            "ee1b015c16f91442bad83e1f5138bd5af1eb68e7",
            "a929c6b8fd5599d1e20d6a0865c12793fd4e19e0",
            "c0bfb5d2d75fb9fe0231ea1fce7bd1fdaf337ee0",
            "ab5f421a2210b263154d4dabb8db51f61f8047db",
            "1b8f5346e3f0573e9c0c9294dd55e37b999d9630",
            "09daa959e5a00edc10121f2453892117dd3963af",
            "acb6da427617b5cd69c5b74599d0503b46fc9e44",
            "9e1bb68b50bd441fb4340da570055bbf056f77a2",
            "d3e0c8e0c30bcb9017e76f96eec709bf5f269760",
            "be61bb1bc00a6be1cf7efe59c1b9467d414cf643",
            "19d693b52266a2833eca2bb929fbf4fce691a5c9",
            "b99816886d9fe43313358d6815231e50c3b62b05",
            "7a73ee3f1cf18b5e2006a20bb9e098e98b6513ca",
            "dec620f008ef65a790a7d1139ace6e8b8efcca5e",
            "b6ba0ebd215cf1b35742a41eb81a269acb67c9a4",
            "3a0faad14d3b64be4edb9d5109dc05dffa7680e2",
            "12e62ce53283b5422d3ea5d8d00bc7f0ae8a127c",
            "aa36f0cc6b50ab30286ba52bcb9bb5c1bd672d62",
            "55120c68b419fe5e12db526d4abfc84871e5dec9",
            "372bf92a9a2507509c3d3932b32444b7be1c9bac",
            "7ab4b04eec091f4ada0807ddd743609bcd898404",
            "20cb412425e88482e7d184efef79577be97bafda",
            "deb91399a7bfb8323bc8e6a5f4045125277c1335",
            "6769f41624e553b3092f5e6390e4d983b851c98c",
            "716760e4f99b59e90a4f914e1fb72a6d2c4b607a",
            "da0aa5548b5c0af0cc494f34cab662a30372dd11",
            "17a0e2ca5ef666eb34e2ed9c10ebc5ddcd0d9bbb",
            "1b3614af749ee359f64f3be3650210cc3c3498ed",
            "346e604622cf8d6b7d03b9fe74e7a684aecca999",
            "629e46882d214f9bd78418c2a97900b2049f1c83",
            "765f86114e942214e099e684e76e94f95e279568",
            "002ed578f79094b3d7e28cc3b06cd230163f1586",
            "52cc9748778af5c8e8b41f9b948abcecf446be91",
            "9326190bf3a15a060b106b1602c7a159e287fd4c",
            "18a5dfbae6e7c9418973d18905a8915dcef7b95b",
            "6d25bf1e8f1244acb6998aa7b1cb09f36662f733",
            "5f9806c0c1a82cea6646503f634a698100a6685d",
            "c3362ce612139290492225d96ab33b2adff7af1e",
            "3d42a5c1eafc725ff0907b600443eef70e9b827e",
            "7ff97ffc5d4f40650d7a7e857e03c5d76edd6767",
            "3a92f2a18e8f593e6a8287921e15e2914df651ef",
            "cde6f2f58166285390b71640a19bd83ca605c942",
            "21a227a8da7a9f5d15c41354196d79fe524de6f0",
            "ebe93ab44146621baab492823a74210d3e9fd35c",
            "6560bd2cde7403083527e597c60988bb1eb21ff1"
        )

        var key = ByteArray(20) {
            it.toByte()
        }

        expectedOutput.forEachIndexed { index, output ->
            assertEquals(output, hash(key, ByteArray(index) { it.toByte() }))
            key = output.toBinary()
        }
    }
}
