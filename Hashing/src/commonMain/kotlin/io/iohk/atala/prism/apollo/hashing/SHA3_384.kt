package io.iohk.atala.prism.apollo.hashing

import io.iohk.atala.prism.apollo.hashing.internal.HMACInterface
import io.iohk.atala.prism.apollo.hashing.internal.KeccakDigest

/**
 * This class implements the SHA3-384 digest algorithm under the [KeccakDigest] API.
 * SHA3-384 is defined by FIPS PUB 202.
 */
final class SHA3_384 : KeccakDigest(0x06), HMACInterface {
    override val digestLength: Int
        get() = 48

    override fun toString() = "SHA3-384"
}
