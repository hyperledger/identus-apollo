package io.iohk.atala.prism.apollo.utils

import com.ionspin.kotlin.bignum.integer.BigInteger
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport
import kotlin.js.JsName

@OptIn(ExperimentalJsExport::class)
@JsExport
data class KMMECPoint(val x: KMMECCoordinate, val y: KMMECCoordinate) {
    @JsName("fromBigIntegersStrings")
    constructor(x: String, y: String) : this(KMMECCoordinate(BigInteger.parseString(x)), KMMECCoordinate(BigInteger.parseString(y)))

    @JsName("fromBigIntegers")
    constructor(x: BigInteger, y: BigInteger) : this(KMMECCoordinate(x), KMMECCoordinate(y))
}
