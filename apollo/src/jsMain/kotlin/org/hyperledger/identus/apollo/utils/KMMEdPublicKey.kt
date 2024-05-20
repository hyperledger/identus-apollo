package org.hyperledger.identus.apollo.utils

import org.hyperledger.identus.apollo.base64.base64UrlEncoded
import org.hyperledger.identus.apollo.utils.external.eddsa
import node.buffer.Buffer
import node.buffer.BufferEncoding

/**
 * Represents a public key in the KMMEd cryptographic system.
 *
 * @property raw The raw value of the public key.
 * @property keyPair The key pair object associated with the public key.
 * @constructor Creates a KMMEdPublicKey object.
 * @param bytes The byte array representing the public key.
 */
@OptIn(ExperimentalJsExport::class)
@JsExport
actual class KMMEdPublicKey(bytes: ByteArray) {
    val raw: Buffer
    private val keyPair: eddsa.KeyPair

    init {
        val ed25519 = eddsa("ed25519")

        raw = Curve25519Parser.parseRaw(bytes)
        val pub = raw.toString(BufferEncoding.hex)

        // TODO: Report a bug in elliptic, this method is not expecting a Buffer (bytes)
        // Internally it expects to find an array, if not Buffer.slice.concat fails when Array.slice.concat doesn't
        // Must keep this...
        keyPair = ed25519.keyFromPublic(pub)
    }

    /**
     * Base64 url encodes the raw value
     * @return Buffer
     */
    fun getEncoded(): Buffer {
        return Buffer.from(raw.toByteArray().base64UrlEncoded.encodeToByteArray())
    }

    /**
     * Confirm a message signature was signed with the corresponding PrivateKey
     * @param message - the message that was signed
     * @param sig - signature
     * @return Boolean
     */
    actual fun verify(message: ByteArray, sig: ByteArray): Boolean {
        return keyPair.verify(Buffer.from(message), sig.decodeToString())
    }
}
