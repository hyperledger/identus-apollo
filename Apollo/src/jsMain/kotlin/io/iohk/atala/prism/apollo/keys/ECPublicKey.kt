package io.iohk.atala.prism.apollo.keys

import com.ionspin.kotlin.bignum.integer.BigInteger
import io.iohk.atala.prism.apollo.externals.base

private fun computeCurvePoint(basePoint: base.BasePoint): ECPoint {
    val x = BigInteger.parseString(basePoint.getX().toString())
    val y = BigInteger.parseString(basePoint.getY().toString())
    return ECPoint(x, y)
}

@JsExport
public actual class ECPublicKey internal constructor(basePoint: base.BasePoint) :
    ECPublicKeyCommon(computeCurvePoint(basePoint))
