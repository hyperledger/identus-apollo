package io.iohk.atala.prism.apollo.rsa

import io.iohk.atala.prism.apollo.utils.KMMRSAKeyPair
import io.iohk.atala.prism.apollo.utils.RSAAsymmetricAlgorithm
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
actual class RSATests {
    @Test
    actual fun testRSA() = runTest {
        val msgToSign = "Hello IOG!"
        val keyPair = KMMRSAKeyPair.generateRSAKeyPair(RSAAsymmetricAlgorithm.RSA, 2048)
        val rsa = RSA()
        val signature = rsa.sign(keyPair.privateKey, msgToSign.encodeToByteArray(), RSASignatureType.RSASHA256)

        assertTrue(rsa.verify(keyPair.publicKey, msgToSign.encodeToByteArray(), signature, RSASignatureType.RSASHA256))
    }

    @Test
    actual fun testRSAPSS() = runTest {
        val msgToSign = "Hello IOG!"
        val keyPair = KMMRSAKeyPair.generateRSAKeyPair(RSAAsymmetricAlgorithm.RSAPSS, 2048)
        val rsa = RSA()
        val signature = rsa.sign(keyPair.privateKey, msgToSign.encodeToByteArray(), RSASignatureType.RSAPSSSHA256)

        assertTrue(rsa.verify(keyPair.publicKey, msgToSign.encodeToByteArray(), signature, RSASignatureType.RSAPSSSHA256))
    }
}
