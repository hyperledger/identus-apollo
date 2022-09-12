package io.iohk.prism.hashing.hmac

import io.iohk.prism.hashing.SHA224
import io.iohk.prism.hashing.internal.toBinary
import io.iohk.prism.hashing.internal.toHexString
import kotlin.test.Test
import kotlin.test.assertEquals

class HmacSHA224Tests : BaseHmacHashTests() {

    override fun hash(key: ByteArray, stringToHash: ByteArray, outputLength: Int?): String {
        val hash = SHA224().createHmac(key, outputLength)
        return hash.digest(stringToHash).toHexString()
    }

    /**
     * From:
     * - NIST test data
     * - https://tools.ietf.org/rfc/rfc4231.txt
     * - https://github.com/crypto-browserify/hash-test-vectors/blob/master/hmac.json
     * - https://github.com/xsc/pandect/blob/main/test/pandect/hmac_test.clj
     */

    @Test
    fun test_Strings() {
        assertEquals(
            "c7405e3ae058e8cd30b08b4140248581ed174cb34e1224bcc1efc81b",
            hash("000102030405060708090A0B0C0D0E0F101112131415161718191A1B1C1D1E1F202122232425262728292A2B2C2D2E2F303132333435363738393A3B3C3D3E3F", "Sample message for keylen=blocklen")
        )
        assertEquals(
            "e3d249a8cfb67ef8b7a169e9a0a599714a2cecba65999a51beb8fbbe",
            hash("000102030405060708090A0B0C0D0E0F101112131415161718191A1B", "Sample message for keylen<blocklen")
        )
        assertEquals(
            "91c52509e5af8531601ae6230099d90bef88aaefb961f4080abc014d",
            hash("000102030405060708090A0B0C0D0E0F101112131415161718191A1B1C1D1E1F202122232425262728292A2B2C2D2E2F303132333435363738393A3B3C3D3E3F404142434445464748494A4B4C4D4E4F505152535455565758595A5B5C5D5E5F60616263", "Sample message for keylen=blocklen")
        )
        assertEquals(
            "896fb1128abbdf196832107cd49df33f47b4b1169912ba4f53684b22",
            hash("0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b", "Hi There")
        )
        assertEquals(
            "a30e01098bc6dbbf45690f3a7e9e6d0f8bbea2a39e6148008fd05e44",
            hash("4a656665", "what do ya want for nothing?")
        )
        assertEquals(
            "88ff8b54675d39b8f72322e65ff945c52d96379988ada25639747e69",
            hash("6b6579", "The quick brown fox jumps over the lazy dog")
        )
    }

    @Test
    fun test_Hexs() {
        assertEquals(
            "7fb3cb3588c6c1f6ffa9694d7d6ad2649365b0c1f65d69d1ec8333ea",
            hash("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", "dddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd".toBinary())
        )
        assertEquals(
            "6c11506874013cac6a2abc1bb382627cec6a90d86efc012de7afec5a",
            hash("0102030405060708090a0b0c0d0e0f10111213141516171819", "cdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcd".toBinary())
        )
        assertEquals(
            "4cd18ac6b4a70fda4033f69d458a8e0d653c650e4cb5db6b459f7bae",
            hash("4a656665", "7768617420646f2079612077616e74207768617420646f2079612077616e7420".toBinary())
        )
    }

    @Test
    fun test_Truncation() {
        assertEquals(
            "0e2aea68a90c8d37c988bcdb9fca6fa8",
            hash("0c0c0c0c0c0c0c0c0c0c0c0c0c0c0c0c0c0c0c0c", "Test With Truncation", 16)
        )
    }

    @Test
    fun test_LargerThanBlockSizeKeyAndLargerThanOneBlockSizeData() {
        assertEquals(
            "95e9a0db962095adaebe9b2d6f0dbce2d499f112f2d2b7273fa6870e",
            hash("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", "Test Using Larger Than Block-Size Key - Hash Key First")
        )
        assertEquals(
            "3a854166ac5d9f023f54d517d0b39dbd946770db9c2b95c9f6f565d1",
            hash("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", "This is a test using a larger than block-size key and a larger than block-size data. The key needs to be hashed before being used by the HMAC algorithm.")
        )
    }

    @Test
    fun test_Seq() {
        val expectedOutput = listOf(
            "6e99e862e532e8936d78b5f02909b130ab09806b2af02f7cb9d39d12",
            "1d1d08669fc34cdc5fe5621a524e7181cd5b5bafca3da56d2e15fcd9",
            "014a21f82d0caad15eb74dd892187d7ad93f2beb549a596dff2c9aa9",
            "5f600f19eded821aeed09781792f9435458a32a60ffc1b678fe2c905",
            "8d933e18052e7fd1f98e5e7d02384da60f3e743801032256282ae2ca",
            "21362a65b49c33568251cd1366eb13a4e683359855c00f3ad6710896",
            "1e1814b72bfb185265af94fa622e4a1a70826c06f2be2efd96e4e168",
            "118f2e1c2f1ab8af2bd17842fcbfac966f5b21a81996e3cbadf76442",
            "2c6c72703e33a20ea0333629503ebcc41b64db829064a5c7897c465b",
            "794046abc3bd8165d12c2453ffa3fc518d1a6498a48c91053bea2966",
            "e6c3b6e2dc215702960633c976b86b8378d7780ff884910454032c7e",
            "de7cff6e85d9411fbd58b28facf72dfdafa115614bef3119f6527104",
            "11cf7495adc07ec29eaa7b3464f772d49999a5e1832f71fce18cf7f1",
            "a7541e63945fcad62d2570b015079df0422e96075986b45772860f38",
            "afd3eb7ebfba79cc68e4f6f6a2d758969b5c5c014ffb53cff21c2841",
            "28d942e37cb92ede2e6f994e9eee2ba01077d099f3562fef97a8cac6",
            "34c7562962548ac9661759b4fc347d6a82cd47991ea06e855571cde1",
            "da76fa12d69d1fdba5e544495bbe45f620be147b73d6aa64d3b3c298",
            "fbf1911fa019cb7aca20e3f93ecc0d5e8d60dca0a1a7420c63ba1864",
            "565fede0ee20842b82d59644929c2a1a426e397b38faa772781fe018",
            "7b9c2ba77b2989904f194021d308089e23f00954275ae9ad87306a31",
            "66cbf93ed8071ffa36b61f3aabfdbfe714c3c055b2fbdcd3cf369025",
            "d96f10ecbfad7fdddf60bf1511e94869ed1d992051539e50d5f32831",
            "5473f93f0d979d77c3c6b9ceeb2f3dc1058d81401669ef4aeafa17e7",
            "5b5a75a7d99c1b40961533c345b95fbf0afa916d6e133967fcaa15f2",
            "2a1e50e18c37ab7bd928ae14c206fac9b3e869173ca337fb9374565d",
            "bf2b241659c96007adc25d9567947baa740555d066636731eeae3c97",
            "6e1e7b64a70b190beebdb9da82c8e4b160cc73b8ffa224a6b92180b3",
            "be36a5f8dae9294b3995d278cbe9273e66f04d46890b44ec55028c3b",
            "9983c289ce2f806f41182752a753e0a890217daf3778b3ad2ed6685e",
            "8b0f08edf2cbe25e8f9ee4d2948ba6bf81672bf4f509530328a8baa2",
            "b65fb77e6cb86e5f409eac2f1b5a05e1910213563f816121afa8cf14",
            "5d15e17c8c159ea5df5f126b12ace777eab36a0082c57df71e4d9609",
            "dccb3d17c8756f2546b3e5b24b1678438959d83a56524415666dae05",
            "d28dab7ca715ac86bf4469d743a0005aee0101f339350661d46a1684",
            "e7a1ccc4b2b300457dcc64534152119390b69610c7ff9dd3a683439a",
            "29380148da403ad5911c7bd52c783ea97ec306f2b32bc426c4d7fd35",
            "56df59cd635f025925a968591e60df2cbab22f98b67c78122f3ce868",
            "c20ef10ae9cd99cbb54c94c971780e0487899d7a810fa51a6553dcf5",
            "5b78837f366097cab6d31624c06b099bdc71286e3ad8873509abf4ce",
            "8da09589c44e710b9f84014fe553074e72e0a86c9418efbbe420d2c8",
            "eee18fa2bb5a5cd16017b4621acc4211ef7cd60613a8c879b0afc0d0",
            "ad9670fcd043e6f91ce986e6f55905337248b72e7b8551ae72ed32bf",
            "97fa4fba4815da49f6127c96c969574aa9543b338f93bf9171d2547e",
            "838d5ac81ea6bacb827327e8efe96cc2b14d92c55b40ce58f4da181e",
            "ca99480dc8480fa07784ef02074453664dbc92257366514060f07c93",
            "93b0e493d272470f9f274dfe4b9ddf183b26011090e15861fa21caf2",
            "770cae487ae5890dc0b931ec17623293efa5b22ee1ed496a37eb9fce",
            "6f1d5ca0446e7b82da02847ed1761cf02d646e56fb0cab9b120e5282",
            "2a8a1254f6ccc3d656397a5f2d64c266412fc5207866b073b77dbdef",
            "e8cb788aaa965ed87ff2c7b5f3107684326dcbb0e667217e0ea62c51",
            "85bdb6d1486f27827d5870812beee2c3976e0ded4bd2f994bbec12aa",
            "a14e0343fad6bd78e0a8e3bcd6d0b6c83b1220fe6c89f57f44bc805c",
            "2c60d71f2d4bec90cf10804dcedb9311637b34d62e9cb68b8503162a",
            "36397d66b434ba744174da541f080cf6582f10322c7fb1869a100141",
            "f612e4ea307f56447112cab5d2ebea7d12c7c4427d9155d4085687fd",
            "9798b420980748993bc78e3601b8aeee2d2cf6e59799c7b07b88435e",
            "50bed37f1ee78fae16d178fecec2ebe4776c8e5fc738f9506e8af676",
            "2755438a9ac457b81999d9e1e479c36dd9ae1f920f5be6d109ed7431",
            "f3dc2238b13ba706a048253f86b79045b72ef767cf25dc62f96daea0",
            "11900a3154c4dfc49b941258a134c9201dfd280728bdb3f8bc7903f8",
            "fc584202454dd7c9258f72a6258e42f3c2669fd138fd7aee6200c4cb",
            "185355c13e146ea89387c332225df31cf114aec0ba3a5a5b53667709",
            "8194dabd2f7a02dddd7b752ab5669821519640ee3b0059fd333f3401",
            "2cd6946c6db676ed1ec272ae34735a0546afb8d996323272c39a814c",
            "b7a344bc5effea97ac49894a85b96f9b570e680dfbb28c76f7f9a180",
            "9011b80655a9cc7964cbc4bee1cc03074003cccff5da553b289ecf6a",
            "6bde25371b7ea9abe31a524e49caae40db220e405463d93fc7f66904",
            "35694194e10d0ebca6758099d09c99c3cab37afa52fc4f4361c510f3",
            "4e7a79f362d7ae5b1680f30e6770ca46fe6264c9fca566718c01ef67",
            "9dd18d21e413ae12112afbe16684bfd4faed7467a2fd5904ef0b493c",
            "7532d374b66b1e5b17eb49810dc3c04264553e4c36f4550d1e860b70",
            "35eb09c82a624b1e3ecd965ed8522e9572ebf26791efa667b4db952c",
            "b9c17df6f2a6506fb1dfcf1a9089974c45760a438330ae7547dfe685",
            "a7dd0267c15b36d8bd1879f879e894fb9f33f254556b87bffedd71a0",
            "68a354d120cd63a5d34eee84b7e5e5bc1e5df6e021f712bd4270b781",
            "441dc4884130d48ba134e2fba86af643c8eb79cd1aa4688f82e0d3dc",
            "17a3f16deafdbc1da00bd14d9c24497be765f41e2ec79578421ed8b9",
            "8756a267d0cad54bfc848fcc4d6b6c94d39caf07831ee35324dcd35f",
            "004ebada70f19bab48e6072e2090941dedb5cc0a7b624e4bbb671382",
            "b7f8d35cb865977423710fa1e0f939808e68abb54bd7eb0427da03de",
            "f3d0aaa2f912ff95251d3cf51ebf79b940db56839dea8ba5872d1fde",
            "0835b2dc376beae873f1fa337d75c72fd1bf0f72a81669aa891f2722",
            "7cf9a7d57cadec3f013d4bd87c00b420cbff73670a9cbb072d18ebeb",
            "68ac0a34930329f5aa40137987208481e34d8b9c08ef7a85ae3ab38b",
            "00492f706d84b903d5355fdc0b68c2c33b484a95a173fdc4ac945028",
            "6f6c509cdcc84ce1c36ab76c9bf30e4422c90c869c164c64696ab5b7",
            "4c0a35d512bd0db15915de08fea8e6027063a1780c104f6273cad5c7",
            "27087f6425878d64a56bd5accc0e303f803b7208f20aefef75501f03",
            "4ef78140430ef60f3ca12aaf8132674b0ddb154f495029b4051c2a36",
            "bcca3153ef93aaf21ca02d235a23d3013976295e704223cb37e860ba",
            "20cc8d4c64e09b00abf23864bd7ede542f5be480afc4b9551b301eba",
            "eca3f86da00098d91f866c58558bb7b00c9e4239cf83c5a3e76291b3",
            "7ad9ab198858820d20373c45173d76af8d68f829d9a250ecadee0da1",
            "3e1c202f2d589bdab015306ad063784e5bea48ae8d1daf45d571d2fd",
            "990c44330d56ebc9edd951f8cb92d5847f4bd3c6442906f57a828fa9",
            "c92f9fcc6220edef52b6f842635a83914b236862f6ccbed16f4899de",
            "0e41c85d5c6d625e1884ef7438dd9ebac818ab50cc265a73165928d0",
            "ae087d57f9cdbcdf4dd68a3e8d5bdfec709a532a4a646cb31785506c",
            "4cb03aefd24c833b5350996eb261e803f6db698fb81f37f8a5c3d891",
            "e680bd218ae972999becdc905f4d39251ecf49b29cf0a13af5fb09a1",
            "64326d6b692b0a17045434bff13282acb91e7b690339f7fcebcc9ae6",
            "20cd91504ab04e2d3cd849808f2362943becb310f4a0bf6e3bd47751",
            "80f607e2d79e1efb0458e47c8e5726cdb8387bc05f42d6eae3239a20",
            "f83c023d6f539967ab24309dd28321599782acfcfc76b77186307300",
            "70164a250799dbe6c5bd3edcdedb16d2516a9fc1bba294c49f753824",
            "1883397c9c4c9d33fb9e1e03325edcea1606d7abf86c4387dabc449e",
            "1355dfa06822cc1f216c131f2baa92a10bbf109ba3e648419a35c0f3",
            "9e35b9b307990b7d664b9eb7f06efdd23037f859acb6b96a5287a846",
            "ccca26c8f8405ff62421558255f2da06f73f17d1ae1763a0bf8430db",
            "b4fae909368405206333491674559b9094da4c48913d9eaca28ad75d",
            "3a5e7d9273f91e10545fe6861d4fc223a5eb0f7b4fbfbc9931634c25",
            "96553cf0c5c6f6a17feed04024fce1d292c392e60b3595ff53007ad9",
            "ca9b79f403412f71fbc10e094b35088576eb3f7f8b5d08757d89f45b",
            "cf60cc5b1822e4a12eeb3e1e5f4aa79e345d8c8fcc546d57dcc7c784",
            "807d65c33e74da0e2d5e3788084c61ae3e8771fdfe643d1269a7901a",
            "a5418dbca94a1f9692ffdb3f7aeed75806cd9fd47171a6b67921c0a8",
            "c2b880c9e9d78b0c397d72c8b6684276e8c22a7f4d6821db7c998775",
            "ea447ea731673e5deab57012cc9e0d3a7b2443165b665822963fd6b5",
            "0f6d50c04357df9240802977779d7f2214fbdbae95b6d8f59b414964",
            "a3b24b29b29bbf32a01f21fff13f44fcaa5fed50718803ac3baac548",
            "e31e36c38a7f2525ecadeca047533830a9c46d609e297142ab3dacaa",
            "592ff0c399a6cc1606fa3f404da4bf8618a4df159cbb7e05dcd30beb",
            "eedd6a5902091adb8ef491f820613740da73a160d825121912613ddb",
            "3a2fcbfcb007f45cb0eedbdd5a765ea0cb7a142ce3c024114d6d61dc",
            "5d29e1732898854af468bbfa5b87065bb811af8f55c91e82e888e842",
            "fd1f646d021ef31f634ef5fb0506620686b9f7d9b5c672734ca10fdf",
            "5e43945ba9de62c364e34cc1361fffee9be8974d7cf5d2e06428916b",
            "0ff4da564729a0e9984e15bc69b00fa2e54711573bee3ad608f511b5"
        )

        var key = ByteArray(28) {
            it.toByte()
        }

        expectedOutput.forEachIndexed { index, output ->
            assertEquals(output, hash(key, ByteArray(index) { it.toByte() }))
            key = output.toBinary()
        }
    }
}
