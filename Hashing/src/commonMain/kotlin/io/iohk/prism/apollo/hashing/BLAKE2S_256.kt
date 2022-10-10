package io.iohk.prism.apollo.hashing

import io.iohk.prism.apollo.hashing.internal.BLAKE2S

final class BLAKE2S_256 : BLAKE2S(256) {

    override fun toString(): String = "BLAKE2s-256"

    public final class Keyed(
        key: ByteArray,
        salt: ByteArray? = null,
        personalisation: ByteArray? = null
    ) : BLAKE2S(key, 256 shr 3, salt, personalisation)
}
