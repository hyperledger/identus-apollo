package io.iohk.atala.prism.apollo.utils

import io.iohk.atala.prism.apollo.base64.base64UrlDecodedBytes
import io.iohk.atala.prism.apollo.utils.external.eddsa
import node.buffer.Buffer
import node.buffer.BufferEncoding

@ExperimentalJsExport
@JsExport
actual class KMMEdPublicKey(bytes: ByteArray) {
    val raw: Buffer
    private val keyPair: eddsa.KeyPair

    init {
        val ed25519 = eddsa("ed25519")

        raw = parseRaw(bytes)
        val pub = raw.toString(BufferEncoding.hex)

        // TODO: Report a bug in elliptic, this method is not expecting a Buffer (bytes)
        // Internally it expects to find an array, if not Buffer.slice.concat fails when Array.slice.concat doesn't
        // Must keep this...
        keyPair = ed25519.keyFromPublic(pub)
    }

    private fun parseRaw(bytes: ByteArray): Buffer {
        val buffer = Buffer.from(bytes)

        if (buffer.length === 43) {
            return Buffer.from(buffer.toByteArray().decodeToString().base64UrlDecodedBytes)
        }

        if (buffer.length === 32) {
            return buffer
        }

        throw Error("invalid raw key");
    }

    actual fun verify(message: ByteArray, sig: ByteArray): Boolean {
        return keyPair.verify(Buffer.from(message), sig.decodeToString())
    }
}
