package io.iohk.atala.prism.apollo.keys

import com.ionspin.kotlin.bignum.integer.BigInteger
import io.iohk.atala.prism.apollo.ECConfig
import io.iohk.atala.prism.apollo.util.padStart
import kotlin.js.JsExport

@JsExport
public data class ECCoordinate(val coordinate: BigInteger) {
    public fun bytes(): ByteArray = coordinate.toByteArray().padStart(ECConfig.PRIVATE_KEY_BYTE_SIZE, 0)
}
