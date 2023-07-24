package io.iohk.atala.prism.apollo.utils

import io.iohk.atala.prism.apollo.utils.external.eddsa
import node.buffer.Buffer

@ExperimentalJsExport
@JsExport
actual class KMMEdPublicKey(val raw: ByteArray) {
    private val keyPair: eddsa.KeyPair

    init {
        val ed25519 = eddsa("ed25519")

        keyPair = ed25519.keyFromPublic(raw)
    }

    actual fun verify(message: ByteArray, sig: ByteArray): Boolean {
        return keyPair.verify(Buffer.from(message), sig.decodeToString())
    }
}
