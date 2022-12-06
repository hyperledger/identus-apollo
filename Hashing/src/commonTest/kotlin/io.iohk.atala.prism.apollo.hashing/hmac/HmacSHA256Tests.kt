package io.iohk.atala.prism.apollo.hashing.hmac

import io.iohk.atala.prism.apollo.hashing.SHA256
import io.iohk.atala.prism.apollo.hashing.internal.JsIgnore
import io.iohk.atala.prism.apollo.hashing.internal.toBinary
import io.iohk.atala.prism.apollo.hashing.internal.toHexString
import kotlin.test.Test
import kotlin.test.assertEquals

class HmacSHA256Tests : BaseHmacHashTests() {

    override fun hash(key: ByteArray, stringToHash: ByteArray, outputLength: Int?): String {
        val hash = SHA256().createHmac(key, outputLength)
        return hash.digest(stringToHash).toHexString()
    }

    /**
     * From:
     * - NIST test data
     * - OpenSSL
     * - https://tools.ietf.org/rfc/rfc4231.txt
     * - https://github.com/crypto-browserify/hash-test-vectors/blob/master/hmac.json
     * - https://github.com/peazip/PeaZip/blob/welcome/peazip-sources/t_hmac.pas
     * - https://github.com/xsc/pandect/blob/main/test/pandect/hmac_test.clj
     */

    @Test
    fun test_Strings() {
        assertEquals(
            "f7bc83f430538424b13298e6aa6fb143ef4d59a14946175997479dbc2d1a3cd8",
            hash("6b6579", "The quick brown fox jumps over the lazy dog")
        )
        assertEquals(
            "a21b1f5d4cf4f73a4dd939750f7a066a7f98cc131cb16a6692759021cfab8181",
            hash(ByteArray(32) { (it + 1).toByte() }, "abc")
        )
        assertEquals(
            "104fdc1257328f08184ba73131c53caee698e36119421149ea8c712456697d30",
            hash(ByteArray(32) { (it + 1).toByte() }, "abcdbcdecdefdefgefghfghighijhijkijkljklmklmnlmnomnopnopq")
        )
        assertEquals(
            "470305fc7e40fe34d3eeb3e773d95aab73acf0fd060447a5eb4595bf33a9d1a3",
            hash(ByteArray(32) { (it + 1).toByte() }, "abcdbcdecdefdefgefghfghighijhijkijkljklmklmnlmnomnopnopqabcdbcdecdefdefgefghfghighijhijkijkljklmklmnlmnomnopnopq")
        )
        assertEquals(
            "b0344c61d8db38535ca8afceaf0bf12b881dc200c9833da726e9376c2e32cff7",
            hash("0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b", "Hi There")
        )
        assertEquals(
            "5bdcc146bf60754e6a042426089575c75a003f089d2739839dec58b964ec3843",
            hash("4a656665", "what do ya want for nothing?")
        )
        assertEquals(
            "2274b195d90ce8e03406f4b526a47e0787a88a65479938f1a5baa3ce0f079776",
            hash("", "My test data")
        )
        assertEquals(
            "bab53058ae861a7f191abe2d0145cbb123776a6369ee3f9d79ce455667e411dd",
            hash("313233343536", "My test data")
        )
        assertEquals(
            "8bb9a1db9806f20df7f77b82138c7914d174d59e13dc4d0169c9057b133e1d62",
            hash("000102030405060708090A0B0C0D0E0F101112131415161718191A1B1C1D1E1F202122232425262728292A2B2C2D2E2F303132333435363738393A3B3C3D3E3F", "Sample message for keylen=blocklen")
        )
        assertEquals(
            "a28cf43130ee696a98f14a37678b56bcfcbdd9e5cf69717fecf5480f0ebdf790",
            hash("000102030405060708090A0B0C0D0E0F101112131415161718191A1B1C1D1E1F", "Sample message for keylen<blocklen")
        )
        assertEquals(
            "bdccb6c72ddeadb500ae768386cb38cc41c63dbb0878ddb9c7a38a431b78378d",
            hash("000102030405060708090A0B0C0D0E0F101112131415161718191A1B1C1D1E1F202122232425262728292A2B2C2D2E2F303132333435363738393A3B3C3D3E3F404142434445464748494A4B4C4D4E4F505152535455565758595A5B5C5D5E5F60616263", "Sample message for keylen=blocklen")
        )
    }

    @Test
    @JsIgnore
    fun test_Hexs() {
        assertEquals(
            "83038173da2181cc0c8c0f92e79c4810e33a6aaad6d09c127cda8cb29d10b734",
            hash("4a656665", "7768617420646f2079612077616e74207768617420646f2079612077616e7420".toBinary())
        )
        assertEquals(
            "773ea91e36800e46854db8ebd09181a72959098b3ef8c122d9635514ced565fe",
            hash("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", "dddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd".toBinary())
        )
        assertEquals(
            "82558a389a443c0ea4cc819899f2083a85f0faa3e578f8077a2e3ff46729665b",
            hash("0102030405060708090a0b0c0d0e0f10111213141516171819", "cdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcd".toBinary())
        )
    }

    @Test
    @JsIgnore
    fun test_Truncation() {
        assertEquals(
            "a3b6167473100ee06e0c796c2955552b",
            hash("0c0c0c0c0c0c0c0c0c0c0c0c0c0c0c0c0c0c0c0c", "Test With Truncation", 16)
        )
    }

    @Test
    @JsIgnore
    fun test_LargerThanBlockSizeKeyAndLargerThanOneBlockSizeData() {
        assertEquals(
            "60e431591ee0b67f0d8a26aacbf5b77f8e0bc6213728c5140546040f0ee37f54",
            hash("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", "Test Using Larger Than Block-Size Key - Hash Key First")
        )
        assertEquals(
            "9b09ffa71b942fcb27635fbcd5b0e944bfdc63644f0713938a7f51535c3a35e2",
            hash("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", "This is a test using a larger than block-size key and a larger than block-size data. The key needs to be hashed before being used by the HMAC algorithm.")
        )
        assertEquals(
            "6953025ed96f0c09f80a96f78e6538dbe2e7b820e3dd970e7ddd39091b32352f",
            hash("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", "Test Using Larger Than Block-Size Key - Hash Key First")
        )
        assertEquals(
            "6355ac22e890d0a3c8481a5ca4825bc884d3e7a1ff98a2fc2ac7d8e064c3b2e6",
            hash("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", "Test Using Larger Than Block-Size Key and Larger Than One Block-Size Data")
        )
    }

    @Test
    @JsIgnore
    fun test_Seq() {
        val expectedOutput = listOf(
            "d38b42096d80f45f826b44a9d5607de72496a415d3f4a1a8c88e3bb9da8dc1cb",
            "12b06c3218c858558cad1da6fe409898c31014d66cbe4ecd47c910ec975e104d",
            "edbef6aa747c951f25ab6aaa0d874648cf18ffecc4c9159f8fc71e971fac6d21",
            "03436338a166e9051599ab268cd74867c6159378069a9ff46fc07cae375eda68",
            "634758df0774a587f3ac6ad7988d0965524de24ebe4dff07ef622bcb8da71acd",
            "0c08e52c7cff8b5f70781197069dc8f209552d241687ba0d24661cccc28d3937",
            "749f473e0d934694ab9917569a61591ca50bef18cabded51666df243de879d53",
            "b1e12cfe0273f5d27192d1a4b70eec4ddc714b66c8bb1921c63381f78cec5219",
            "1c60f13a1c539788e989bac2ebd4f8e126ee6ed82c2e25817c63b2b633fabd33",
            "5643f445b2c0656a49bb3db5088c9e2e4b2082c2b611bba0dae5791f2faa5d43",
            "c467f47251dad4694c9c7a6758e54cebd68fc933c7c57458020774a2a2b4288b",
            "85c90cf2719bebf40ef8d501fda20c342bc406e728551bc0275ada1747bd981f",
            "06b72dac895b008da249b7b1d8a5133f09d86bf82de2c4251bfa6c3d8c4cf03f",
            "49edb6714a556df324e41a3ce5b57006e38fd7ca8b90feea2acab429204747be",
            "7411921d759da0b491d6d4cc372db79cc163f146c345b4a73d93eeb4c262a1df",
            "5c37ffbd1f0512af443265b2f3e8b6d01ad9b45ff6f373d2cd0a7c6e48d03e26",
            "773165fd16d51e51cd8a958e548902b47bbd0a6e156c31b6fea036f6d8c4a90c",
            "5b4be909754ebc8ecbbb8b5da6298b8341b35d92e17ce7281909eba1ef568347",
            "c6eef2d12f54815561eeed3426d7aa7e671e26d42384b9478d91fc6b14cc76f8",
            "4c9fa0575cd96bb1def6ea79f5ec7a1f0478e86352812f690c2c2bdb70028bcc",
            "7f87ba45fc41ec30e76f61e4eadec013ce2b4c49ca6fe6d2fa525f6bbd45e103",
            "9b8ca1d70339a0894e16ce4e76f6655addd3eeb598f3dd80fecc5eeef3f638c3",
            "e4608aea430a638799991b748bb858c91af58f56b226e1901d28336b30498279",
            "af4f9c52079b28546fbb44eeba20c7af0bf493d34ef6967b07ca32fc4de25adb",
            "fe51f3a9313eedaaa991350ab4d1d7045d42aacf3ac7155da3ad9a2f1de3a73e",
            "c1f5aed9d77f85404a4b308a139d33f351b20c91a738e698bd8182f124d96c82",
            "3cac12a252b93b7d724af9119fd3c18e85e88401f93bff42aa05711b9833b1f6",
            "e61d4e94c212324a64b1a0c04b2237a9a1c5cc003d83ea80bceb45452dcb42f2",
            "d01ba47dabce4704b6820ec0ecdbef137b9c4acb80dc99b7c9220cfd9f9ce363",
            "aed502c53a8b2c76f671376cddbd0596376b3664b917cd9c9adbc489543d4721",
            "3405afd96584c5e5963362948d112a70155877be3b5efd479f226b73351abaf0",
            "5fa0290dc68b72b1fa27dbaf157923c706b3f52cde9c4ee38cda31d376b0bc0d",
            "c1391c694c985ccba707a8c78ad05e2180af6b4da5bb877aac5e2ab33b4890e2",
            "b018e7b15f92dbec58f767633bca3bd0d84b6d5b9443784dc1757166d7aa1c16",
            "8d9e2c84967004e3957df59d502bc11cf8c8959368117ec5db56ac958a3e791b",
            "b0eaf9c0e869d7a304ddb30061a73c580b0a6f9d49e15442ecfbb3b5a851855b",
            "0b48b0d8c3acf7b4f9ecf8e46563c921b1b6720b6c650d72dd1126b6763cd595",
            "8879d239edb09f6606957d96a1f4bf37eac0f3419881eea79e8bf1364fb3ff6d",
            "cc663e436de42e32ea110f9d90eb990d9151c9f06d51243d2076b0cc45361736",
            "732dc3b1f809e55c498c53fc75a23966caea16be984f795cb1bc94d026fab30e",
            "f1f0eec77d97a0234d0f19b2fb12a96b6e2ff8626f79a74d4af26cde1344d838",
            "75c9d8c7344668c478d8ae6d9e2c41e336e7a2504cdd43b73ccbf78b4c05eeb1",
            "4b149bca6429408b242e76c52c4d3a0a5f5437ec0ab6d24d71eb1ac5496d75ba",
            "edb65ebebc0411b4fdaf186033e306ad500711ccb80e770e99523bb2672a237a",
            "d1bbff5a48346a0dfd5cffaa7a2af08c27f3fc2908d7a5d2f575e07ca9e72474",
            "e8efb6373dd3457610e57750738358a50026d2c6704a98148cdd69bff7b70551",
            "8e3733b729ceb97444bcca405044b98f45fc59bba86444a3fc0f4df4854b5c4d",
            "868f3ee8f4d4dfedc3ffaeee1fa069f5fbb2cb818e63c28151c1566634189234",
            "3f5396115dc7f17aab19a3a9779cffcca57de7a7c1a42f748fec49b7d8c2b82d",
            "dc2a5e3e176a693ad8cae551a505729b78fbde778b526e28953bc1a56b54840e",
            "dc91fd745e9a7a9d0b41c79b3b3939b84bdf78beb007f9aaf8ff82084759223a",
            "e73dcf5413f17d4eccec813dc060ef907c2e952af92dd247a0ae2be798e6a40b",
            "696b5ee4c1e1d8b60b0015eea2389c9a35088022fff10034d0d09fa722a2a3e6",
            "f86c07265389512b2ce240a89ea29d61c6c79c2738faca157b0de43294485682",
            "db31cbbfd28d6f8564219911efb748a5663e482dba26e38634e8e27e3cf65707",
            "2f9675313aab7a940ae77ca906d0342a448fdba3f7589d14b1344d586ea157de",
            "7d829fd994258ef2afdef22c8cd5cc1d29a9a55b62847b3b6f5db630421cf999",
            "a6cdb9bc9af75ea4680e895e8eddce76f536f7cca571d62781a06ddb3424fa50",
            "1b4186a34eb07f5b3127f2be0f3943610679db0f6babc7da03b416fa577d36e2",
            "7b5dff3459dc10b9b7aa2b2829094f97706db5b2f133b8bf9f48d90253d68359",
            "2abb68160300028bbf3b5a414970d11df4fd6f4b4a35029def8492adfb19a480",
            "b1b13abf9d20c42e755d63ec63c016126259c8a6c3f9ab3f0f6ac5d0bd44eca2",
            "9addd17e5cf407cdbb12e5e52a50ce134f1b48a2a2af90d7308344fb5a70485f",
            "6a4c06df40ba515c56476471d4a94f87a2b91eaff6c66510892f2f20a342b736",
            "555d424206c003bad0b08beea76dfc81b307c79bbb6e4f15325b2ecd37e04423",
            "8a58733e0b990d0d82f93f77df36e30dcfd03b3181b73c544bb097a3a73b6ac9",
            "6fcccca4172e30a281a702e36e7bca07370d4b57272385077a44d5f7933dd2fc",
            "3b1a91e49af88b1832f8e91109c7cc5dbee2847d9acd2a57404dbb565480ac75",
            "69584075c278763cb0b09d4c9e15e9300a191bf99907049f14ec8de24d86c121",
            "2ee24340d13e68b10b95c3f77d55027f98bde6ba5328d0c02cf89965687c062b",
            "c04b37f5932f427d40e21eeab7c9594b16bfcf4f5fe2bf175cd63c62f2ceeaa2",
            "058e1ac8971add2617a4bf7d02b46a8b74a4d52b25643df9729a1e7df6ccc86f",
            "18001f246abc760197482e25f3ac64b14a795e55b41b505d6027261bfde7c52c",
            "4aeaaed524b173e08e54a83e2d9a8b8824e6e2f1b89203d698e9bce7c3242f8f",
            "7d82cfb1d7427302889cadba23a99154cbac0c9adec94eaf29eb07dc86b0b7e2",
            "18d42e92ba532a409ceda8e3a07e751b430800827f5a9f14d93e3ed231ba08af",
            "8cfba378d8595372dce5d9a6e726c23512f84c0c1ec3c66adf6b6c55df63936a",
            "de1a6e280a9054c91b826785928f37a16e1d2a9a3cec831185b26d2b8ede158c",
            "920c40b4204c7f3d4775176bd245ba0276604c568b3c29943c9aef1a1c93428a",
            "935bb39e5fbce5c4a15ac2a854475578cf80308e531ca86818dabe69bed8824a",
            "d608e561471cc09ec0865c826242ca26aa1c90bdf1625e1a38b96e3ee0cc5f04",
            "efe2a8d806a1a71596a05a2f5f48d18cfd4a742247b04e8089fab27291a8dd50",
            "80235be35ddea5d49f124d8be3d143f87ccba7d0608c7e2cabbaab01bb95e477",
            "e9410e0dc14f3be36a49a5ca673c12e18cbe4f0817e0c1cbd2069349f8a09bbb",
            "b2042a81a36f27b4cb96dbb52a61f701a815869ff5aa0cdcad0327e1ed1c2f22",
            "e9e5a9501b24952dcfbb9d59cf95a9a9e6a27fb7315eb472d1e2b7f523d06d42",
            "99193b4fafeffc932b261ef169250b96901abf877424ff667cc0da0154c50498",
            "1d9c7f7e681d20e1e0324efe71c8b6913fe8ca87ee52e443335115ab2c458e7f",
            "7308db7e2591d2342109c5084b1174f07d289fbe91472fb2d8c06df39f826b84",
            "90f06adc29070dc50a23d3f093007e273e783491a70a2f0ad6ba40e34f02518d",
            "e676deedc972019f10fec24b4aeac0a97870e924f7b1d6d3ecf91ef38a2ac544",
            "b5da3b40fbf373795e67a6338f9ac3ad742741f34048930d9336d429d02ee78f",
            "6fde20988863ce157042ee52065eeda233bb2e6ec0464b9dcf2aac1f3a18971f",
            "428d4cff477f0f0379f634d1e7c15e4ce6da067adc45221a860c9c3ac4235753",
            "9ec80b57e921da3f81d13b65aa851f5971e4074c96e0d8b64e50a7f5089c1fc8",
            "9088151bef766d0896a48eb6dcc8a09d151c3396fbf3a9fe193c5e7bf9030b01",
            "86d853024a762536666316f363bb867efe25fbd03bdd28ea7522973a1a1bd95c",
            "007104bd935b532ba4702a78c505d67b41358a61db8069585b91b1445dc346b5",
            "5c5709f6202948e805fac25c454ecfadfac693955864494e511f0cd1fc9cfdcf",
            "0b010f71c5323cc96d3b8df71170968096e44969ea55b4c3dac632d30d81d529",
            "54621ec4f31cc7f6273601d81674612b44726b5cc4a76ead2bbc3d32dbf62a9d",
            "28efe1ab745be64e5dd7286c97360ff2d287f862adbe44380f85e1388008079f",
            "831bfa684c25542676ad52819249a10d9ef9c2505d69cc1397d0d39d08b39e5d",
            "ef7922c40cd96a47c5e7ae4d958b495f1d6954edc20596e303cfba43190a9efa",
            "3a0262ebc746a7c044c1db043951f7eac645c40f554898d3d7b2b7aac4ebd396",
            "1f2cfba7275639a12da7cd1986f920c47850de3fe13c931618c0fac765820ed5",
            "7ac8913c0975101e187fdaddac5b5ec467a25869c4e630eadbb42dd2dfe4958a",
            "d386591f326c91d274fe625a667b6f9f6f7d99cf56acb365a218f1cf8e167a70",
            "66286cb1b61156b005cbfc94c2cab1a6694d7f123411b8a123f2acd821c291f2",
            "844d1038e710690050da737d56fd6b17c261c7be512713e62033384b53c40902",
            "7ef970c40080f554851277f4e950c6f378b0a3da3cd1be250d976162f8a4ee79",
            "9bc20a2b67566688bcac77fcf30259f11d9b2fd2277d033e6aae19e36058a353",
            "796c72d95bba1a4341c6b0397e165dd21cfbef55555b35c717ce33b6c6ade490",
            "1e6a9c1f78aff266ef8fb25c32c1fdfb4a0f64affd046d257470bf6daef61d6d",
            "0e1ad927ad658c5e0321333af8ae4ed69903b4f22c5dff90ac93268507a7c86b",
            "07b7a778e2931704e7feca284ff3b14071e255a2b824ad0a2272d21448579cee",
            "a8d810df06368a0e825d6db4394916e43e217bee9303ad4096a8e1cad37b8703",
            "6a9c7d302cca1ee170366f355d8f40ae3a20d28bfcb2ba163dcb68e08dacb748",
            "40c3a8b08ff9f767491e4243d1808572fdaf1d8cd21ab47115849531513d0750",
            "f26ea6760aa80360398371855783815bcd34431e0ccec58a34a67997ace43cef",
            "eea78d68a509988ed6d7e3f27fc22f3ebcd570ef0fe242a0251457eac4c3c1f4",
            "af977819b87f2e63c0e131dfa2a31c555ad831adca6de0fc1be48d21a1e7e666",
            "846a75df3691b2bf224fb0e66e360a2e8bb1da32422190f2b319b73e6900ad42",
            "ffa997fcfabc9fcad4b58b0ef848890fb23b974cd57fa07223037450c371b116",
            "0028c776965a0ae5e9e70d9b833bf328bdbcd06c5a12a2f1c510911e60aa304a",
            "7fa234c59957c214a7be8d1b909c540b48e54414ee5fd1081b4c339fd2204515",
            "a840beebf2c2e80af2e4830bb26f71aee48a9c65de4a9425da9f98fa3a37dd84",
            "a95332415ea29a8ca6fdb0f771e3f2262c6907dc45b0ac8bc229f6009323c3a9",
            "8b185702392bc1e061414539546904553a62510bc2e9e045892d64daa6b32a76"
        )

        var key = ByteArray(32) {
            it.toByte()
        }

        expectedOutput.forEachIndexed { index, output ->
            assertEquals(output, hash(key, ByteArray(index) { it.toByte() }))
            key = output.toBinary()
        }
    }
}
