package io.iohk.atala.prism.apollo.util

import com.ionspin.kotlin.bignum.integer.Sign
import java.lang.IllegalStateException
import java.math.BigInteger

internal fun BigInteger.toUnsignedByteArray(): ByteArray {
    return toByteArray().dropWhile { it == 0.toByte() }.toByteArray()
}

internal fun ByteArray.toKotlinBigInteger(): com.ionspin.kotlin.bignum.integer.BigInteger {
    return com.ionspin.kotlin.bignum.integer.BigInteger.fromByteArray(this, Sign.POSITIVE)
}

internal fun BigInteger.toKotlinBigInteger(): com.ionspin.kotlin.bignum.integer.BigInteger {
    val sign = when (this.signum()) {
        -1 -> Sign.NEGATIVE
        0 -> Sign.ZERO
        1 -> Sign.POSITIVE
        else -> throw IllegalStateException("Illegal BigInteger sign")
    }
    return com.ionspin.kotlin.bignum.integer.BigInteger.fromByteArray(this.toUnsignedByteArray(), sign)
}

internal fun com.ionspin.kotlin.bignum.integer.BigInteger.toJavaBigInteger(): BigInteger {
    return BigInteger(this.signum(), this.toByteArray())
}
