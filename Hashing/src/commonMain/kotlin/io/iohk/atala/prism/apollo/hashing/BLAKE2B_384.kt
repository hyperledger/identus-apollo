package io.iohk.atala.prism.apollo.hashing

import io.iohk.atala.prism.apollo.hashing.internal.BLAKE2B

final class BLAKE2B_384 : BLAKE2B(384) {
    override val blockLength: Int
        get() = 128

    override fun toString(): String = "BLAKE2B-384"

    public final class Keyed(
        key: ByteArray,
        salt: ByteArray? = null,
        personalisation: ByteArray? = null
    ) : BLAKE2B(key, 384, salt, personalisation) {
        override val blockLength: Int
            get() = 128

        override fun toString(): String = "BLAKE2B-384"
    }
}
