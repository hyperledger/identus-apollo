package io.iohk.prism.hashing

import io.iohk.prism.hashing.internal.BLAKE2B

final class BLAKE2B_512 : BLAKE2B(512) {
    override val blockLength: Int
        get() = 128

    override fun toString(): String = "BLAKE2B-512"

    public final class Keyed(
        key: ByteArray,
        salt: ByteArray? = null,
        personalisation: ByteArray? = null
    ) : BLAKE2B(key, 512, salt, personalisation) {
        override val blockLength: Int
            get() = 128

        override fun toString(): String = "BLAKE2B-512"
    }
}
