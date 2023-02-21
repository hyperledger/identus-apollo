package io.iohk.atala.prism.apollo.ecdsa

import io.iohk.atala.prism.apollo.utils.ECConfig
import io.iohk.atala.prism.apollo.utils.Encodable
import io.iohk.atala.prism.apollo.utils.KMMECPrivateKeyCommonStaticInterface
import io.iohk.atala.prism.apollo.utils.decodeHex
import kotlin.js.JsExport

private val zeroSignature = "3006020100020100".decodeHex()

interface KMMECDSASignatureCommonStaticInterface {

    fun toKMMECDSASignatureFromBytes(encoded: ByteArray): KMMECDSASignature {
        require(encoded.size <= ECConfig.SIGNATURE_MAX_BYTE_SIZE) {
            "Expected signature byte length to be less than ${ECConfig.SIGNATURE_MAX_BYTE_SIZE}, but got ${encoded.size} bytes"
        }
        return KMMECDSASignature(encoded)
    }
}

@JsExport
public abstract class KMMECDSASignatureCommon(data: ByteArray) : Encodable {
    init {
        require(!data.contentEquals(zeroSignature)) {
            "Signature is invalid as (r, s) = (0, 0)"
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as KMMECDSASignatureCommon

        if (!getEncoded().contentEquals(other.getEncoded())) return false

        return true
    }

    override fun hashCode(): Int =
        getEncoded().hashCode()
}

expect class KMMECDSASignature(data: ByteArray) : KMMECDSASignatureCommon {

    companion object : KMMECDSASignatureCommonStaticInterface
}
