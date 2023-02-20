package io.iohk.atala.prism.apollo.utils

import com.ionspin.kotlin.bignum.integer.BigInteger

// TODO(Create KMMSecp256k1PrivateKey to contains all below implementation to better separate responsibilities)

interface KMMECPrivateKeyCommonStaticInterface {
    fun secp256k1FromBigInteger(d: BigInteger): KMMECPrivateKey
}

abstract class KMMECPrivateKeyCommon(val d: BigInteger) : Encodable {
    abstract fun getPublicKey(): KMMECPublicKey
}

expect class KMMECPrivateKey : KMMECPrivateKeyCommon {
    companion object : KMMECPrivateKeyCommonStaticInterface
}
