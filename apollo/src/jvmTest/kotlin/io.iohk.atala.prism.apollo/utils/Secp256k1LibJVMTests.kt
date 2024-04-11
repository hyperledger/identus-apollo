package io.iohk.atala.prism.apollo.utils

import io.iohk.atala.prism.apollo.base64.base64UrlDecodedBytes
import io.iohk.atala.prism.apollo.secp256k1.Secp256k1Lib
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class Secp256k1LibJVMTests {

    @Test
    fun testVerification() {
        val pubKeyBase64 = "BD-l4lrQ6Go-oN5XtdpY6o5dyf2V2v5EbMAvRjVGJpE1gYVURJfxKMpNPnKlLr4MOLNVaYvBNOoy9L50E8jVx8Q"
        val signatureBase64 =
            "MEUCIQCFeGlhJrH-9R70X4JzrurWs52SwuxCnJ8ky6riFwMOrwIgT7zlLo7URMHW5tiMgG73IOw2Dm3XyLl1iqW1-t5NFWQ"
        val message = "Test"

        assertTrue {
            Secp256k1Lib().verify(
                pubKeyBase64.base64UrlDecodedBytes,
                signatureBase64.base64UrlDecodedBytes,
                message.encodeToByteArray()
            )
        }
    }

    @OptIn(ExperimentalStdlibApi::class)
    @Test
    fun testSignatureNormalisationFromJS() {
        val signatureHex =
            "3045022100bca0478b0f400277626355d9f411bf0534afb1236758099c0c8ffdd761664a7202207e629a601f1bbe3e0df10899e62919c2a5c902f773120a2bddeb1cde14bae1fd"
        val publicKeyHex =
            "042e6d481b0b8761414166ea01431ed339d9150a58c2e521cb6cc8638988299b835acfc2cc1c7a7fd9ea54acc8060c37367be8ed7fb70c13203e702ef7a20d8b06"
        val dataHex = "4174616c61507269736d2057616c6c65742053444b"
        val publicKey = publicKeyHex.hexToByteArray()
        val signature = signatureHex.hexToByteArray()
        val data = dataHex.hexToByteArray()
        val valid = Secp256k1Lib().verify(publicKey, signature, data)
        assertEquals(valid, true)
    }

    @Test
    fun testJVMVerification() {
        val pubKeyBase64 = "BD-l4lrQ6Go-oN5XtdpY6o5dyf2V2v5EbMAvRjVGJpE1gYVURJfxKMpNPnKlLr4MOLNVaYvBNOoy9L50E8jVx8Q"
        val signatureBase64 =
            "MEUCIQCFeGlhJrH-9R70X4JzrurWs52SwuxCnJ8ky6riFwMOrwIgT7zlLo7URMHW5tiMgG73IOw2Dm3XyLl1iqW1-t5NFWQ"
        val message = "Test"
        assertTrue {
            Secp256k1Lib().verify(
                pubKeyBase64.base64UrlDecodedBytes,
                signatureBase64.base64UrlDecodedBytes,
                message.encodeToByteArray()
            )
        }
    }

    @Test
    fun testJVMVerificationBitcoin() {
        val ecPublicKeyBase64 =
            "BC7OYUnD57Qxel3-gyGuUIeicvRYhkFMOw9vsz70WMHzt8X8jiX358Jv9KYrMOHpkHE6jpb8CTvGabgIJUPkX_4"
        val messageBase64 =
            "eyJ0eXAiOiJKV1QiLCJhbGciOiJFUzI1NksifQ.eyJub25jZSI6IjQ3YmM5ZmMwLWVhODAtNDlmOC04OTcxLWJjYzY0MmJmZDNjMCIsImlzcyI6ImRpZDpwcmlzbTphZjJlNGJiOWU1MTRmODg5ZTdkNTY2MDZjNmYzZWVhYmNmMDgxZTc0ZTQ4NDMwN2Q3NTQ4Mzg0Y2ZiOTE4ZTdlOkNzY0JDc1FCRW1RS0QyRjFkR2hsYm5ScFkyRjBhVzl1TUJBRVFrOEtDWE5sWTNBeU5UWnJNUklnTHM1aFNjUG50REY2WGY2RElhNVFoNkp5OUZpR1FVdzdEMi16UHZSWXdmTWFJTGZGX0k0bDktZkNiX1NtS3pEaDZaQnhPbzZXX0FrN3htbTRDQ1ZENUZfLUVsd0tCMjFoYzNSbGNqQVFBVUpQQ2dselpXTndNalUyYXpFU0lDN09ZVW5ENTdReGVsMy1neUd1VUllaWN2Ulloa0ZNT3c5dnN6NzBXTUh6R2lDM3hmeU9KZmZud21fMHBpc3c0ZW1RY1RxT2x2d0pPOFpwdUFnbFEtUmZfZyIsInZwIjp7IkBjb250ZXh0IjpbImh0dHBzOlwvXC93d3cudzMub3JnXC8yMDE4XC9wcmVzZW50YXRpb25zXC92MSJdLCJ0eXBlIjpbIlZlcmlmaWFibGVQcmVzZW50YXRpb24iXX0sImF1ZCI6ImRvbWFpbiJ9"
        val rawSignatureBase64 =
            "ZGjNy5vyOaDCPfZRqpjbolcPZXD3WmM_VugjIgVhY2ANARaJ_PnNCnTFFYUgajzml8kIhyIPQsVOchQDQz1RMA"
        val swiftSignatureBase64 =
            "MEQCIGBjYQUiI-hWP2Na93BlD1ei25iqUfY9wqA58pvLzWhkAiAwUT1DAxRyTsVCDyKHCMmX5jxqIIUVxXQKzfn8iRYBDQ"
        val nimbusDerSignatureBase64 =
            "MEQCIGRozcub8jmgwj32UaqY26JXD2Vw91pjP1boIyIFYWNgAiANARaJ_PnNCnTFFYUgajzml8kIhyIPQsVOchQDQz1RMA=="

        val publicKey = ecPublicKeyBase64.base64UrlDecodedBytes
        val message = messageBase64.encodeToByteArray()

        assertTrue {
            Secp256k1Lib().verify(
                publicKey,
                rawSignatureBase64.base64UrlDecodedBytes,
                message
            )
        }

        assertTrue {
            Secp256k1Lib().verify(
                publicKey,
                swiftSignatureBase64.base64UrlDecodedBytes,
                message
            )
        }

        assertTrue {
            Secp256k1Lib().verify(
                publicKey,
                nimbusDerSignatureBase64.base64UrlDecodedBytes,
                message
            )
        }
    }
}
