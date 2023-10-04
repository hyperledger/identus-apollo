package io.iohk.atala.prism.apollo.utils

import org.bouncycastle.crypto.generators.Ed25519KeyPairGenerator
import org.bouncycastle.crypto.params.Ed25519KeyGenerationParameters
import org.bouncycastle.crypto.params.Ed25519PrivateKeyParameters
import org.bouncycastle.crypto.params.Ed25519PublicKeyParameters
import java.security.SecureRandom

actual class KMMEdKeyPair actual constructor(
    actual val privateKey: KMMEdPrivateKey,
    actual val publicKey: KMMEdPublicKey
) {
    actual companion object : Ed25519KeyPairGeneration {
        override fun generateKeyPair(): KMMEdKeyPair {
            val generator = Ed25519KeyPairGenerator()
            generator.init(Ed25519KeyGenerationParameters(SecureRandom()))
            val pair = generator.generateKeyPair()
            return KMMEdKeyPair(
                privateKey = KMMEdPrivateKey((pair.private as Ed25519PrivateKeyParameters).encoded),
                publicKey = KMMEdPublicKey((pair.public as Ed25519PublicKeyParameters).encoded),
            )
        }
    }

    actual fun sign(message: ByteArray): ByteArray {
        return privateKey.sign(message)
    }

    actual fun verify(message: ByteArray, sig: ByteArray): Boolean {
        return publicKey.verify(message, sig)
    }
}
