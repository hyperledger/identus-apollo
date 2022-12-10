package io.iohk.atala.prism.apollo.keys

import com.ionspin.kotlin.bignum.integer.BigInteger
import com.ionspin.kotlin.bignum.integer.Sign

public actual class ECPrivateKey(internal val key: UByteArray) :
    ECPrivateKeyCommon(BigInteger.fromUByteArray(key, Sign.POSITIVE)) {

    override fun getEncoded(): ByteArray {
        return key.toByteArray()
    }
}
