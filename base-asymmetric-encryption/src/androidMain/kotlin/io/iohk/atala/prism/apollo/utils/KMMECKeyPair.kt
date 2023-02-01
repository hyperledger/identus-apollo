package io.iohk.atala.prism.apollo.utils

import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPrivateKey
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey
import org.bouncycastle.jce.provider.BouncyCastleProvider
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.SecureRandom
import java.security.Security
import java.security.spec.ECGenParameterSpec

actual class KMMECKeyPair actual constructor(actual val privateKey: KMMECPrivateKey, actual val publicKey: KMMECPublicKey) {

    init {
        Security.removeProvider("BC")
        Security.addProvider(BouncyCastleProvider())
    }

    private constructor(privateKey: BCECPrivateKey, publicKey: BCECPublicKey) : this(KMMECPrivateKey(privateKey), KMMECPublicKey(publicKey))

    actual companion object : ECKeyPairGeneration, Secp256k1KeyPairGeneration {
        @JvmStatic
        private val provider = BouncyCastleProvider()

        @JvmStatic
        override fun generateECKeyPair(): KMMECKeyPair {
            val generator: KeyPairGenerator = KeyPairGenerator.getInstance("EC", provider)
            val keypair: KeyPair = generator.generateKeyPair()

            val privateKey: BCECPrivateKey = keypair.private as BCECPrivateKey
            val publicKey: BCECPublicKey = keypair.public as BCECPublicKey

            return KMMECKeyPair(privateKey, publicKey)
        }

        @JvmStatic
        override fun generateSecp256k1KeyPair(): KMMECKeyPair {
            val generator: KeyPairGenerator = KeyPairGenerator.getInstance("EC", provider)
            generator.initialize(ECGenParameterSpec(KMMEllipticCurve.SECP256k1.value), SecureRandom())
            val keypair: KeyPair = generator.generateKeyPair()

            val privateKey: BCECPrivateKey = keypair.private as BCECPrivateKey
            val publicKey: BCECPublicKey = keypair.public as BCECPublicKey

            return KMMECKeyPair(privateKey, publicKey)
        }
    }
}
