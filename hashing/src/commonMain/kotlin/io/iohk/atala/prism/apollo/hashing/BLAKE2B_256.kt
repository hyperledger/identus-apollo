package io.iohk.atala.prism.apollo.hashing

import io.iohk.atala.prism.apollo.hashing.internal.BLAKE2B

final class BLAKE2B_256 : BLAKE2B(256) {
    override val blockLength: Int
        get() = 128

    override fun toString(): String = "BLAKE2B-256"

    public final class Keyed(
        key: ByteArray,
        salt: ByteArray? = null,
        personalisation: ByteArray? = null
    ) : BLAKE2B(key, 256, salt, personalisation) {
        override val blockLength: Int
            get() = 128

        override fun toString(): String = "BLAKE2B-256"
    }
}
