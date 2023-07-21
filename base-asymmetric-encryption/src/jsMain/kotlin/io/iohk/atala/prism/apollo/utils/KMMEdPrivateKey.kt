package io.iohk.atala.prism.apollo.utils

import io.iohk.atala.prism.apollo.utils.external.eddsa
import node.buffer.Buffer

@ExperimentalJsExport
@JsExport
actual class KMMEdPrivateKey(val raw: ByteArray) {
    private val keyPair: eddsa.KeyPair

    init {
        val ed25519 = eddsa("ed25519")

        keyPair = ed25519.keyFromSecret(raw)
    }

    actual fun sign(message: ByteArray): ByteArray {
        val sig = keyPair.sign(Buffer.from(message))

        return sig.toHex().encodeToByteArray()
    }
}
