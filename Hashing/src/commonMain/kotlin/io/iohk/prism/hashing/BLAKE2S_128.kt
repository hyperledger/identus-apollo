package io.iohk.prism.hashing

import io.iohk.prism.hashing.internal.BLAKE2S

final class BLAKE2S_128 : BLAKE2S(128) {

    override fun toString(): String = "BLAKE2s-128"

    public final class Keyed(
        key: ByteArray,
        salt: ByteArray? = null,
        personalisation: ByteArray? = null
    ) : BLAKE2S(key, 128 shr 3, salt, personalisation)
}
