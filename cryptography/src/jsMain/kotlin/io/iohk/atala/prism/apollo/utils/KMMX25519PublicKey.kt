package io.iohk.atala.prism.apollo.utils

import io.iohk.atala.prism.apollo.base64.base64UrlEncoded
import node.buffer.Buffer

@ExperimentalJsExport
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
