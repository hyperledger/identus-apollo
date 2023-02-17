package io.iohk.atala.prism.apollo.utils

interface Encodable {
    /**
     * @return encoded version of the entity
     */
    public fun getEncoded(): ByteArray
}
