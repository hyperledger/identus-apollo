package io.iohk.atala.prism.apollo.utils

import com.ionspin.kotlin.bignum.integer.BigInteger

// TODO(Create KMMSecp256k1PrivateKey to contains all below implementation to better separate responsibilities)

abstract class KMMECPrivateKeyCommon(val d: BigInteger)

expect class KMMECPrivateKey : KMMECPrivateKeyCommon
