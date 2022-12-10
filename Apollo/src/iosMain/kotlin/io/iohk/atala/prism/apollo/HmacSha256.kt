package io.iohk.atala.prism.apollo

import kotlinx.cinterop.addressOf
import kotlinx.cinterop.convert
import kotlinx.cinterop.usePinned
import platform.CoreCrypto.*

/**
 * HMAC-SHA-256 iOS implementation.
 */
public actual object HmacSha256 {

    /**
     * Compute HMAC-SHA-256 data authentication code using shared key.
     */
    public actual fun compute(
        data: ByteArray,
        key: SymmetricKey
    ): ByteArray {
        val digest = UByteArray(CC_SHA256_DIGEST_LENGTH)

        key.raw.usePinned { keyPinned ->
            data.usePinned { dataPinned ->
                digest.usePinned { digestPinned ->
                    CCHmac(
                        kCCHmacAlgSHA256,
                        keyPinned.addressOf(0),
                        key.raw.size.convert(),
                        dataPinned.addressOf(0),
                        data.size.convert(),
                        digestPinned.addressOf(0)
                    )
                }
            }
        }

        return digest.toByteArray()
    }
}
