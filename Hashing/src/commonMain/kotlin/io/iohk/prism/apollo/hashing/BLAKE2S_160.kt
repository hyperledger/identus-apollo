package io.iohk.prism.apollo.hashing

import io.iohk.prism.apollo.hashing.internal.BLAKE2S

final class BLAKE2S_160 : BLAKE2S(160) {

    override fun toString(): String = "BLAKE2s-160"

    public final class Keyed(
        key: ByteArray,
        salt: ByteArray? = null,
        personalisation: ByteArray? = null
    ) : BLAKE2S(key, 160 shr 3, salt, personalisation)
}
