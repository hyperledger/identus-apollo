package io.iohk.atala.prism.apollo.utils

import io.iohk.atala.prism.apollo.base64.base64UrlEncoded
import io.iohk.atala.prism.apollo.utils.external.eddsa
import node.buffer.Buffer

@ExperimentalJsExport
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
}
