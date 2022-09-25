package io.iohk.prism.hashing

import io.iohk.prism.hashing.internal.BLAKE2B

final class BLAKE2B_160 : BLAKE2B(160) {
    override val blockLength: Int
        get() = 128

    override fun toString(): String = "BLAKE2B-160"

    public final class Keyed(
        key: ByteArray,
        salt: ByteArray? = null,
        personalisation: ByteArray? = null
    ) : BLAKE2B(key, 160, salt, personalisation) {
        override val blockLength: Int
            get() = 128

        override fun toString(): String = "BLAKE2B-160"
    }
}
