package io.iohk.atala.prism.apollo.utils

actual class KMMX25519PublicKey {
    public val raw: ByteArray

    constructor(raw: ByteArray) {
        this.raw = raw
    }
}
