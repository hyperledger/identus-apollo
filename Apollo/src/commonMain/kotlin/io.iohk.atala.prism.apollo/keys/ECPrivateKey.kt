package io.iohk.atala.prism.apollo.keys

import com.ionspin.kotlin.bignum.integer.BigInteger
import io.iohk.atala.prism.apollo.ECConfig

public abstract class ECPrivateKeyCommon
@Throws(ECPrivateKeyInitializationException::class)
internal constructor(
    internal val d: BigInteger
) :
    ECKey() {
    init {
        if (d < BigInteger.TWO || d >= ECConfig.n)
            throw ECPrivateKeyInitializationException(
                "Private key D should be in range [2; ${ECConfig.n})"
            )
    }

    public fun getD(): BigInteger = d
}

public expect class ECPrivateKey : ECPrivateKeyCommon
