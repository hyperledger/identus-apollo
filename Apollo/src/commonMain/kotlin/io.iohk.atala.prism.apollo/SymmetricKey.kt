package io.iohk.atala.prism.apollo

import kotlin.js.JsExport

@JsExport
public data class SymmetricKey(internal val raw: ByteArray) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as SymmetricKey

        return raw.contentEquals(other.raw)
    }

    override fun hashCode(): Int {
        return raw.contentHashCode()
    }
}
