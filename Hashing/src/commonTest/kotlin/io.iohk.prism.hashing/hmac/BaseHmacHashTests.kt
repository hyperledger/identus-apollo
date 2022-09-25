package io.iohk.prism.hashing.hmac

import io.iohk.prism.hashing.internal.toBinary
import kotlin.jvm.JvmOverloads

abstract class BaseHmacHashTests {
    abstract fun hash(key: ByteArray, stringToHash: ByteArray, outputLength: Int? = null): String

    @JvmOverloads
    fun hash(key: ByteArray, stringToHash: String, outputLength: Int? = null): String {
        return hash(key, stringToHash.encodeToByteArray(), outputLength)
    }

    @JvmOverloads
    fun hash(key: String, stringToHash: String, outputLength: Int? = null): String {
        return hash(key.toBinary(), stringToHash.encodeToByteArray(), outputLength)
    }

    @JvmOverloads
    fun hash(key: String, stringToHash: ByteArray, outputLength: Int? = null): String {
        return hash(key.toBinary(), stringToHash, outputLength)
    }
}
