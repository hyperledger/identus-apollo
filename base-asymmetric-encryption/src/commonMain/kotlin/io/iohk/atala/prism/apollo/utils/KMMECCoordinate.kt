package io.iohk.atala.prism.apollo.utils

import com.ionspin.kotlin.bignum.integer.BigInteger
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

@OptIn(ExperimentalJsExport::class)
@JsExport
data class KMMECCoordinate(val coordinate: BigInteger) {
    fun bytes(): ByteArray = coordinate.toByteArray().padStart(PRIVATE_KEY_BYTE_SIZE, 0)

    companion object {
        internal val PRIVATE_KEY_BYTE_SIZE: Int = 32
    }
}
