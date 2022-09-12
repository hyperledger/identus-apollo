package io.iohk.prism.hashing.hmac

import io.iohk.prism.hashing.internal.toBinary

abstract class BaseHmacHashTests {
    abstract fun hash(key: ByteArray, stringToHash: ByteArray, outputLength: Int? = null): String

    fun hash(key: ByteArray, stringToHash: String, outputLength: Int? = null): String {
        return hash(key, stringToHash.encodeToByteArray(), outputLength)
    }

    fun hash(key: String, stringToHash: String, outputLength: Int? = null): String {
        return hash(key.toBinary(), stringToHash.encodeToByteArray(), outputLength)
    }

    fun hash(key: String, stringToHash: ByteArray, outputLength: Int? = null): String {
        return hash(key.toBinary(), stringToHash, outputLength)
    }
}
