package io.iohk.atala.prism.apollo.utils

import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPrivateKey
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey
import org.bouncycastle.jce.provider.BouncyCastleProvider
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.SecureRandom
import java.security.spec.ECGenParameterSpec

actual class KMMECSecp256k1KeyPair actual constructor(actual val privateKey: KMMECSecp256k1PrivateKey, actual val publicKey: KMMECSecp256k1PublicKey) {

    internal constructor(privateKey: BCECPrivateKey, publicKey: BCECPublicKey) : this(KMMECSecp256k1PrivateKey(privateKey), KMMECSecp256k1PublicKey(publicKey))

    actual companion object : Secp256k1KeyPairGeneration {
        @JvmStatic
        override fun generateSecp256k1KeyPair(): KMMECSecp256k1KeyPair {
            val provider = BouncyCastleProvider()
            val generator: KeyPairGenerator = KeyPairGenerator.getInstance("EC", provider)
            generator.initialize(ECGenParameterSpec(KMMEllipticCurve.SECP256k1.value), SecureRandom())
            val keypair: KeyPair = generator.generateKeyPair()

            val privateKey: BCECPrivateKey = keypair.private as BCECPrivateKey
            val publicKey: BCECPublicKey = keypair.public as BCECPublicKey

            return KMMECSecp256k1KeyPair(privateKey, publicKey)
        }
    }
}
