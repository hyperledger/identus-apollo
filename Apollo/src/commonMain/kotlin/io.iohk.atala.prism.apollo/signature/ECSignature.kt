package io.iohk.atala.prism.apollo.signature

import io.iohk.atala.prism.apollo.Encodable
import io.iohk.atala.prism.apollo.util.BytesOps
import kotlin.js.JsExport

private val zeroSignature = BytesOps.hexToBytes("3006020100020100")

@JsExport
public abstract class ECSignatureCommon(data: ByteArray) : Encodable {
    init {
        require(!data.contentEquals(zeroSignature)) {
            "Signature is invalid as (r, s) = (0, 0)"
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as ECSignatureCommon

        if (!getEncoded().contentEquals(other.getEncoded())) return false

        return true
    }

    override fun hashCode(): Int =
        getEncoded().hashCode()
}

public expect class ECSignature(data: ByteArray) : ECSignatureCommon
