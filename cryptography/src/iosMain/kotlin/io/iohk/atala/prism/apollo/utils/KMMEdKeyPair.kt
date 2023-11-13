package io.iohk.atala.prism.apollo.utils

actual class KMMEdKeyPair actual constructor(
    actual val privateKey: KMMEdPrivateKey,
    actual val publicKey: KMMEdPublicKey
) {
    public actual companion object : Ed25519KeyPairGeneration {
        public override fun generateKeyPair(): KMMEdKeyPair {
            val privateKey = KMMEdPrivateKey()
            return KMMEdKeyPair(privateKey, privateKey.publicKey())
        }
    }

    @Throws(RuntimeException::class)
    actual fun sign(message: ByteArray): ByteArray {
        return privateKey.sign(message)
    }

    @Throws(RuntimeException::class)
    actual fun verify(message: ByteArray, sig: ByteArray): Boolean {
        return publicKey.verify(message, sig)
    }
}
