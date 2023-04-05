package io.iohk.atala.prism.apollo.ecdsa

import io.iohk.atala.prism.apollo.hashing.SHA256
import io.iohk.atala.prism.apollo.utils.KMMECSecp256k1KeyPair
import io.iohk.atala.prism.apollo.utils.KMMECSecp256k1PrivateKey
import io.iohk.atala.prism.apollo.utils.KMMECSecp256k1PublicKey
import org.bitcoinj.core.ECKey
import org.bitcoinj.core.Sha256Hash
import org.bitcoinj.crypto.TransactionSignature
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertTrue

class BouncyCastleBitcoinJCompatibility {
    // crash happens inside class => fr.acinq.secp256k1.jni.NativeSecp256k1JvmLoader
    // method name load
    // Unable to load JNI which I believe according to its code here => https://github.com/ACINQ/secp256k1-kmp/blob/master/jni/jvm/src/main/kotlin/fr/acinq/secp256k1/jni/NativeSecp256k1JvmLoader.kt
    // Compiler unable to find the JNI file to load
    private val Secp256k1 = fr.acinq.secp256k1.Secp256k1.get()

    @Test
    fun shouldSignBouncyCastleThenVerifyByBitcoinJ() {
        val textToSign = "Hello IOG!"
        val keyPair = KMMECSecp256k1KeyPair.generateSecp256k1KeyPair()

        val signature = KMMECDSA.sign(
            ECDSAType.ECDSA_SHA256,
            textToSign.encodeToByteArray(),
            keyPair.privateKey
        )

        val bouncyCastlePublicKey = keyPair.publicKey
        val publicKey = ECKey.fromPublicOnly(bouncyCastlePublicKey.getEncoded())
        val hash = Sha256Hash.wrap(Sha256Hash.hash(textToSign.toByteArray()))
        val libsecp256k1Signature = TransactionSignature.decodeFromDER(signature)

        assertTrue(publicKey.verify(hash, libsecp256k1Signature))
    }

    @Test
    fun shouldSignBitcoinJThenVerifyByBouncyCastle() {
        val textToSign = "Hello IOG!"
        val ecKey = ECKey().decompress()
        val publicKeyBytes = ecKey.pubKey

        // Sign the message using libsecp256k1 (bitcoinj)
        val hash = Sha256Hash.wrap(Sha256Hash.hash(textToSign.toByteArray()))
        val libsecp256k1Signature = ecKey.sign(hash)
        val signatureBytes = libsecp256k1Signature.encodeToDER()

        val pk = KMMECSecp256k1PublicKey.secp256k1FromBytes(publicKeyBytes)

        // Verify the signature using BouncyCastle (KMMECDSA)
        val isSignatureValid = KMMECDSA.verify(
            ECDSAType.ECDSA_SHA256,
            textToSign.encodeToByteArray(),
            pk,
            signatureBytes
        )

        assertTrue(isSignatureValid)
    }

    // Compatibility Tests
    // @Test // For testing uncomment the previous comment
    fun codeUsageExample() {
        val message = "Hello IOG!"

        // 1. Generate KeyPair
        val bouncyCastleKeyPair = bouncyCastleGenerateSecp256k1KeyPair()
        val nativeKeyPair = nativeGenerateSecp256k1KeyPair()
        // 2. Sign Message
        val bouncyCastleSignature = bouncyCastleECDSASign(message, bouncyCastleKeyPair.privateKey)
        val nativeSignature = nativeECDSASign(message, nativeKeyPair.first)
        // 3. Verify Message
        val bouncyCastleVerify = bouncyCastleECDSAVerify(message, bouncyCastleSignature, bouncyCastleKeyPair.publicKey)
        val nativeVerify = nativeECDSAVerify(message, nativeSignature, nativeKeyPair.second)
    }

    // Helper methods for BouncyCastle

    /**
     * Generate KeyPair using BouncyCastle
     */
    private fun bouncyCastleGenerateSecp256k1KeyPair(): KMMECSecp256k1KeyPair {
        return KMMECSecp256k1KeyPair.generateSecp256k1KeyPair()
    }

    /**
     * Sign ECDSA using BouncyCastle
     */
    private fun bouncyCastleECDSASign(messageToSign: String, privateKey: KMMECSecp256k1PrivateKey): ByteArray {
        val message = messageToSign.encodeToByteArray()
        return KMMECDSA.sign(ECDSAType.ECDSA_SHA256, message, privateKey)
    }

    /**
     * Verify ECDSA using BouncyCastle
     */
    private fun bouncyCastleECDSAVerify(messageToVerify: String, signature: ByteArray, publicKey: KMMECSecp256k1PublicKey): Boolean {
        val message = messageToVerify.encodeToByteArray()
        return KMMECDSA.verify(ECDSAType.ECDSA_SHA256, message, publicKey, signature)
    }

    // Helper methods for Native

    /**
     * Generate random bytes with a fixed length
     */
    private fun randomBytes(length: Int): ByteArray {
        val buffer = ByteArray(length)
        Random.Default.nextBytes(buffer)
        return buffer
    }

    /**
     * Generate KeyPair using Native
     */
    private fun nativeGenerateSecp256k1KeyPair(): Pair<ByteArray, ByteArray> {
        val private = randomBytes(32)
        val public = Secp256k1.pubkeyCreate(private)
        return Pair(private, public)
    }

    /**
     * Sign ECDSA using Native
     */
    private fun nativeECDSASign(messageToSign: String, privateKey: ByteArray): ByteArray {
        val message = SHA256().digest(messageToSign.encodeToByteArray())
        val sig = Secp256k1.sign(message, privateKey)
        val der = Secp256k1.compact2der(sig)
        return Secp256k1.signatureNormalize(der).first
    }

    /**
     * Verify ECDSA using Native
     */
    private fun nativeECDSAVerify(messageToVerify: String, signature: ByteArray, publicKey: ByteArray): Boolean {
        val message = SHA256().digest(messageToVerify.encodeToByteArray())
        return Secp256k1.verify(signature, message, publicKey)
    }
}
