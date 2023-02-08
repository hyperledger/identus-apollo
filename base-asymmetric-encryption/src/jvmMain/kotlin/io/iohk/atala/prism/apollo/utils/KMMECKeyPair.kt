package io.iohk.atala.prism.apollo.utils

import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.SecureRandom
import java.security.interfaces.ECPrivateKey
import java.security.interfaces.ECPublicKey
import java.security.spec.ECGenParameterSpec

actual class KMMECKeyPair actual constructor(actual val privateKey: KMMECPrivateKey, actual val publicKey: KMMECPublicKey) {

    private constructor(privateKey: ECPrivateKey, publicKey: ECPublicKey) : this(KMMECPrivateKey(privateKey), KMMECPublicKey(publicKey))

    actual companion object : ECKeyPairGeneration {
        @JvmStatic
        override fun generateECKeyPair(curve: EllipticCurve): KMMECKeyPair {
            val generator: KeyPairGenerator = KeyPairGenerator.getInstance("EC")
            generator.initialize(ECGenParameterSpec(curve.value), SecureRandom())
            val keypair: KeyPair = generator.generateKeyPair()

            val privateKey: ECPrivateKey = keypair.private as ECPrivateKey
            val publicKey: ECPublicKey = keypair.public as ECPublicKey

            return KMMECKeyPair(privateKey, publicKey)
        }
    }
}
