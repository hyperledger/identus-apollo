package org.hyperledger.identus.apollo.utils

import node.buffer.Buffer
import org.hyperledger.identus.apollo.base64.base64UrlEncoded

/**
 * Represents a public key for the X25519 elliptic curve encryption algorithm.
 *
 * @property raw The binary representation of the public key.
 * @constructor Creates a new instance of [KMMX25519PublicKey] with the given raw value.
 */
@OptIn(ExperimentalJsExport::class)
@JsExport
actual class KMMX25519PublicKey(bytes: ByteArray) {
    val raw: Buffer

    init {
        raw = Curve25519Parser.parseRaw(bytes)
    }

    /**
     * Base64 url encodes the raw value
     * @return Buffer
     */
    fun getEncoded(): Buffer {
        return Buffer.from(raw.toByteArray().base64UrlEncoded.encodeToByteArray())
    }
}
