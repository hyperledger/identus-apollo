package io.iohk.atala.prism.apollo.utils

interface Encodable {
    /**
     * @return encoded version of the entity
     */
    fun getEncoded(): ByteArray
}
