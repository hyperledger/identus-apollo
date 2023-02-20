package io.iohk.atala.prism.apollo.ecdsa

import io.iohk.atala.prism.apollo.utils.KMMECKeyPair
import kotlin.test.Test
import kotlin.test.assertTrue

class KMMECDSATests {
    @Test
    fun testECDSA() {
        val textToSign = "Hello IOG!"
        val keyPair = KMMECKeyPair.generateSecp256k1KeyPair()

        val signature = KMMECDSA.sign(
            ECDSAType.ECDSA_SHA256,
            textToSign.encodeToByteArray(),
            keyPair.privateKey
        )

        assertTrue(
            KMMECDSA.verify(
                ECDSAType.ECDSA_SHA256,
                textToSign.encodeToByteArray(),
                keyPair.publicKey,
                signature
            )
        )
    }
}
