package io.iohk.atala.prism.apollo

import org.spongycastle.crypto.digests.SHA256Digest
import org.spongycastle.crypto.macs.HMac
import org.spongycastle.crypto.params.KeyParameter

/**
 * HMAC-SHA-256 Android implementation.
 */

public actual object HmacSha256 {

    /**
     * Compute HMAC-SHA-256 data authentication code using shared key.
     */
    public actual fun compute(
        data: ByteArray,
        key: SymmetricKey
    ): ByteArray {
        val hmac = HMac(SHA256Digest())
        hmac.init(KeyParameter(key.raw))
        hmac.update(data, 0, data.size)
        val out = ByteArray(32)
        hmac.doFinal(out, 0)
        return out
    }
}
