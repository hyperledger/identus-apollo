package org.hyperledger.identus.apollo.utils

import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

@OptIn(ExperimentalJsExport::class)
@JsExport
interface Encodable {
    /**
     * @return encoded version of the entity
     */
    fun getEncoded(): ByteArray
}
