package io.iohk.atala.prism.apollo.utils

import com.ionspin.kotlin.bignum.integer.BigInteger
import io.iohk.atala.prism.apollo.utils.external.base.BasePoint

actual class KMMECPublicKey(val nativeValue: BasePoint) : KMMECPublicKeyCommon(computeCurvePoint(nativeValue)) {
    actual companion object : KMMECPublicKeyCommonStatic {
        private fun computeCurvePoint(basePoint: BasePoint): KMMECPoint {
            val x = BigInteger.parseString(basePoint.getX().toString())
            val y = BigInteger.parseString(basePoint.getY().toString())
            return KMMECPoint(x, y)
        }
    }
}
