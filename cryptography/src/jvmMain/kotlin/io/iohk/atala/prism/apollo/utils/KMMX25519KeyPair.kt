package io.iohk.atala.prism.apollo.utils

import org.bouncycastle.crypto.generators.X25519KeyPairGenerator
import org.bouncycastle.crypto.params.X25519KeyGenerationParameters
import org.bouncycastle.crypto.params.X25519PrivateKeyParameters
import org.bouncycastle.crypto.params.X25519PublicKeyParameters
import java.security.SecureRandom

actual class KMMX25519KeyPair actual constructor(
    actual val privateKey: KMMX25519PrivateKey,
    actual val publicKey: KMMX25519PublicKey
) {
    actual companion object : X25519KeyPairGeneration {
        override fun generateKeyPair(): KMMX25519KeyPair {
            val generator = X25519KeyPairGenerator()
            generator.init(X25519KeyGenerationParameters(SecureRandom()))
            val keyPair = generator.generateKeyPair()

            return KMMX25519KeyPair(
                KMMX25519PrivateKey((keyPair.private as X25519PrivateKeyParameters).encoded),
                KMMX25519PublicKey((keyPair.public as X25519PublicKeyParameters).encoded)
            )
        }
    }
}
