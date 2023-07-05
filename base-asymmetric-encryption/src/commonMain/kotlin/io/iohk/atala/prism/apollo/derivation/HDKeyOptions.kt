package io.iohk.atala.prism.apollo.derivation

import com.ionspin.kotlin.bignum.integer.BigInteger

data class HDKeyOptions(
    val versions: Pair<Int, Int>,
    val chainCode: ByteArray,
    val depth: Int,
    val parentFingerprint: Int,
    val index: BigInteger,
    var privateKey: ByteArray? = null,
    var publicKey: ByteArray? = null
)
