package io.iohk.atala.prism.apollo.utils

import io.iohk.atala.prism.apollo.secp256k1.Secp256k1
import kotlin.random.Random

actual class KMMECKeyPair actual constructor(actual val privateKey: KMMECPrivateKey, actual val publicKey: KMMECPublicKey) {

    internal constructor(privateNative: ByteArray, publicNative: ByteArray) : this(KMMECPrivateKey(privateNative), KMMECPublicKey(publicNative))

    actual companion object : ECKeyPairGeneration {
        private val random = Random.Default

        private fun randomBytes(length: Int): ByteArray {
            val buffer = ByteArray(length)
            random.nextBytes(buffer)
            return buffer
        }
        override fun generateECKeyPair(curve: EllipticCurve): KMMECKeyPair {
            when (curve) {
                EllipticCurve.SECP256k1 -> {
                    val privateKey = randomBytes(32)
                    check(Secp256k1.secKeyVerify(privateKey))
                    val publicKey = Secp256k1.pubkeyCreate(privateKey)
                    return KMMECKeyPair(privateKey, publicKey)
                }
                EllipticCurve.SECP256r1 -> {
                    throw NotImplementedError("It has not been develop")
                }
            }
        }
    }
}
