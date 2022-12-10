@file:JvmName("ECPublicKeyJvm")

package io.iohk.atala.prism.apollo.keys

import io.iohk.atala.prism.apollo.GenericJavaCryptography
import io.iohk.atala.prism.apollo.util.toKotlinBigInteger
import java.security.PublicKey

private fun computeCurvePoint(key: PublicKey): ECPoint {
    val javaPoint = GenericJavaCryptography.publicKeyPoint(key)
    return ECPoint(javaPoint.affineX.toKotlinBigInteger(), javaPoint.affineY.toKotlinBigInteger())
}

public actual class ECPublicKey(internal val key: PublicKey) :
    ECPublicKeyCommon(computeCurvePoint(key))
