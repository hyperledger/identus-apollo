package io.iohk.atala.prism.apollo.hashing.internal

import kotlin.jvm.JvmOverloads

/**
 * [HMAC] is defined in RFC 2104 (also FIPS 198a).
 */
expect final class HMAC @JvmOverloads constructor(dig: Digest, key: ByteArray, outputLength: Int? = null) : HashingBase
