package io.iohk.atala.prism.apollo

/**
 * HMAC-SHA-256 facade.
 */
public expect object HmacSha256 {

    /**
     * Compute HMAC-SHA-256 data authentication code using shared key.
     */

    public fun compute(data: ByteArray, key: SymmetricKey): ByteArray
}
