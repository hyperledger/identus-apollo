package io.iohk.atala.prism.apollo.utils

import com.ionspin.kotlin.bignum.integer.BigInteger as KMMBigInteger
import com.ionspin.kotlin.bignum.integer.Sign
import java.lang.IllegalStateException
import java.math.BigInteger

fun BigInteger.toUnsignedByteArray(): ByteArray {
    return toByteArray().dropWhile { it == 0.toByte() }.toByteArray()
}

fun BigInteger.toKotlinBigInteger(): KMMBigInteger {
    val sign = when (this.signum()) {
        -1 -> Sign.NEGATIVE
        0 -> Sign.ZERO
        1 -> Sign.POSITIVE
        else -> throw IllegalStateException("Illegal BigInteger sign")
    }
    return KMMBigInteger.fromByteArray(this.toUnsignedByteArray(), sign)
}

fun KMMBigInteger.toJavaBigInteger(): BigInteger {
    return BigInteger(this.signum(), this.toByteArray())
}
