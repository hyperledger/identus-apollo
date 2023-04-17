package io.iohk.atala.prism.apollo.utils

import org.bouncycastle.jce.provider.BouncyCastleProvider
import java.security.KeyPair
import java.security.KeyPairGenerator

actual class KMMEdKeyPair actual constructor(actual val privateKey: KMMEdPrivateKey, actual val publicKey: KMMEdPublicKey) {
    actual companion object : Ed25519KeyPairGeneration {
        override fun generateEd25519KeyPair(): KMMEdKeyPair {
            val provider = BouncyCastleProvider()
            val generator = KeyPairGenerator.getInstance("Ed25519", provider)
            val javaKeyPair: KeyPair = generator.generateKeyPair()
            return KMMEdKeyPair(
                KMMEdPrivateKey(javaKeyPair.private),
                KMMEdPublicKey(javaKeyPair.public)
            )
        }
    }
}