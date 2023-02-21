package io.iohk.atala.prism.apollo.utils

import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPrivateKey
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey
import org.bouncycastle.jce.provider.BouncyCastleProvider
import java.security.KeyPair
import java.security.KeyPairGenerator

actual class KMMECKeyPair actual constructor(actual val privateKey: KMMECPrivateKey, actual val publicKey: KMMECPublicKey) {

    private constructor(privateKey: BCECPrivateKey, publicKey: BCECPublicKey) : this(KMMECPrivateKey(privateKey), KMMECPublicKey(publicKey))

    actual companion object : ECKeyPairGeneration {
        @JvmStatic
        override fun generateECKeyPair(): KMMECKeyPair {
            val provider = BouncyCastleProvider()
            val generator: KeyPairGenerator = KeyPairGenerator.getInstance("EC", provider)
            val keypair: KeyPair = generator.generateKeyPair()

            val privateKey: BCECPrivateKey = keypair.private as BCECPrivateKey
            val publicKey: BCECPublicKey = keypair.public as BCECPublicKey

            return KMMECKeyPair(privateKey, publicKey)
        }
    }
}
