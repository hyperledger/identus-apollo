package io.iohk.atala.prism.apollo.utils

import io.iohk.atala.prism.apollo.base64.base64UrlDecodedBytes
import io.iohk.atala.prism.apollo.utils.external.eddsa
import node.buffer.Buffer

@ExperimentalJsExport
@JsExport
actual class KMMEdPrivateKey(bytes: ByteArray) {
    val raw: Buffer
    private val keyPair: eddsa.KeyPair

    init {
        val ed25519 = eddsa("ed25519")

        raw = this.parseRaw(bytes)
        keyPair = ed25519.keyFromSecret(raw)
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

    actual fun publicKey(): KMMEdPublicKey {
        return KMMEdPublicKey(keyPair.getPublic())
    }

    actual fun sign(message: ByteArray): ByteArray {
        val sig = keyPair.sign(Buffer.from(message))

        return sig.toHex().encodeToByteArray()
    }
}
