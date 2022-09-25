package io.iohk.prism.hashing

import io.iohk.prism.hashing.internal.BLAKE2S

final class BLAKE2S_224 : BLAKE2S(224) {

    override fun toString(): String = "BLAKE2s-224"

    public final class Keyed(
        key: ByteArray,
        salt: ByteArray? = null,
        personalisation: ByteArray? = null
    ) : BLAKE2S(key, 224 shr 3, salt, personalisation)
}
