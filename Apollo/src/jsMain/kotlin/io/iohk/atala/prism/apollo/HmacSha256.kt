package io.iohk.atala.prism.apollo

import io.iohk.atala.prism.apollo.externals.BlockHash
import io.iohk.atala.prism.apollo.externals.hash

/**
 * HMAC-SHA-256 JavaScript implementation.
 */
@JsExport
public actual object HmacSha256 {

    /**
     * Compute HMAC-SHA-256 data authentication code using shared key.
     */
    public actual fun compute(
        data: ByteArray,
        key: SymmetricKey
    ): ByteArray {
        val hmac = hash.hmac(hash.sha256 as BlockHash<Any>, key.raw) // ugly type casting, but it works
        return hmac.update(data).digest().map { it.toByte() }.toByteArray()
    }
}
