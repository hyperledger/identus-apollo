package org.hyperledger.identus.apollo.utils

import node.buffer.Buffer
import org.hyperledger.identus.apollo.base64.base64UrlEncoded
import org.hyperledger.identus.apollo.utils.external.eddsa

/**
 * Represents a private key in the KMMEd cryptographic system.
 *
 * @property raw The raw value of the private key.
 * @property keyPair The key pair object associated with the private key.
 * @constructor Creates a KMMEdPrivateKey object.
 * @param bytes The byte array representing the private key.
 */
@OptIn(ExperimentalJsExport::class)
@JsExport
actual class KMMEdPrivateKey(bytes: ByteArray) {
    val raw: Buffer
    private val keyPair: eddsa.KeyPair

    init {
        val ed25519 = eddsa("ed25519")

        raw = Curve25519Parser.parseRaw(bytes)
        keyPair = ed25519.keyFromSecret(raw)
    }

    /**
     * Base64 url encodes the raw value
     * @return Buffer
     */
    fun getEncoded(): Buffer {
        return Buffer.from(raw.toByteArray().base64UrlEncoded)
    }

    /**
     * PublicKey associated with this PrivateKey
     * @return KMMEdPublicKey
     */
    fun publicKey(): KMMEdPublicKey {
        return KMMEdPublicKey(keyPair.getPublic())
    }

    /**
     * Cryptographically sign a given [message]
     * @param message - the ByteArray to be signed
     * @return ByteArray - signature Hex converted to ByteArray
     */
    actual fun sign(message: ByteArray): ByteArray {
        val sig = keyPair.sign(Buffer.from(message))

        return sig.toHex().encodeToByteArray()
    }

    /**
     * Method convert an ed25519 private key to a x25519 private key
     *
     * @return KMMX25519PrivateKey private key
     */
    actual fun x25519PrivateKey(): KMMX25519PrivateKey {
        val rawX25519Prv = convertSecretKeyToX25519(this.raw.toByteArray())
        return KMMX25519PrivateKey(rawX25519Prv)
    }
}
