package io.iohk.atala.prism.apollo

import kotlinx.cinterop.addressOf
import kotlinx.cinterop.convert
import kotlinx.cinterop.usePinned
import platform.CoreCrypto.*

public actual object Sha256 {
    public actual fun compute(bytes: ByteArray): Sha256Digest {
        val digest = UByteArray(CC_SHA256_DIGEST_LENGTH)
        bytes.usePinned { bytesPinned ->
            digest.usePinned { digestPinned ->
                CC_SHA256(bytesPinned.addressOf(0), bytes.size.convert(), digestPinned.addressOf(0))
            }
        }
        return Sha256Digest(digest.toByteArray())
    }
}
