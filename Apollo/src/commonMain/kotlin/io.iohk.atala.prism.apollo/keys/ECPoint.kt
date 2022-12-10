package io.iohk.atala.prism.apollo.keys

import com.ionspin.kotlin.bignum.integer.BigInteger
import kotlin.js.JsExport
import kotlin.js.JsName

@JsExport
public data class ECPoint(val x: ECCoordinate, val y: ECCoordinate) {
    @JsName("fromBigIntegers")
    public constructor(x: BigInteger, y: BigInteger) : this(ECCoordinate(x), ECCoordinate(y))
}
