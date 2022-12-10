package io.iohk.atala.prism.apollo

import io.iohk.atala.prism.apollo.externals.hash

@JsExport
public actual object Sha256 {
    public actual fun compute(bytes: ByteArray): Sha256Digest {
        val array = bytes.map { it.toUByte().toInt() }.toTypedArray()
        val sha256Digest = hash.sha256.invoke().update(array).digest()
        return Sha256Digest(sha256Digest.map { it.toByte() }.toByteArray())
    }
}
