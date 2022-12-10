package io.iohk.atala.prism.apollo

import io.iohk.atala.prism.apollo.util.BytesOps

public interface Encodable {
    /**
     * @return encoded version of the entity
     */
    public fun getEncoded(): ByteArray

    /**
     * @return hex version of [getEncoded]
     */
    public fun getHexEncoded(): String {
        return BytesOps.bytesToHex(getEncoded())
    }
}
