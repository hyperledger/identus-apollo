package io.iohk.atala.prism.apollo.utils

import org.bouncycastle.jcajce.spec.XDHParameterSpec
import org.bouncycastle.jce.provider.BouncyCastleProvider
import java.security.KeyPairGenerator

actual class KMMX25519KeyPair actual constructor(
    actual val privateKey: KMMX25519PrivateKey,
    actual val publicKey: KMMX25519PublicKey
) {
    actual companion object : X25519KeyPairGeneration {
        override fun generateKeyPair(): KMMX25519KeyPair {
            val provider = BouncyCastleProvider()
            val kpg = KeyPairGenerator.getInstance("X25519", provider)
            kpg.initialize(XDHParameterSpec(XDHParameterSpec.X25519))
            val javaKeyPair = kpg.generateKeyPair()
            return KMMX25519KeyPair(
                KMMX25519PrivateKey(javaKeyPair.private),
                KMMX25519PublicKey(javaKeyPair.public)
            )
        }
    }
}
