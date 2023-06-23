package io.iohk.atala.prism.apollo.hashing.internal

import io.iohk.atala.prism.apollo.hashing.external.BlockHash
import io.iohk.atala.prism.apollo.hashing.external.hash

/**
 * [HMAC] is defined in RFC 2104 (also FIPS 198a).
 */
actual final class HMAC actual constructor(
    private var dig: Digest,
    val key: ByteArray,
    outputLength: Int?
) : HashingBase() {
    private var outputLength: Int = outputLength ?: -1
    private var dataToHash: ByteArray = byteArrayOf()

    override val blockLength: Int
        get() = 64
    override val digestLength: Int
        get() = if (outputLength < 0) dig.digestLength else outputLength

    override fun toString() = "HMAC/$dig"

    override fun engineReset() {
        return
    }

    override fun processBlock(data: ByteArray) {
        return
    }

    override fun doPadding(output: ByteArray, outputOffset: Int) {
        return
    }

    override fun doInit() {
        return
    }

    override fun update(input: ByteArray) {
        dataToHash += input
    }

    @Suppress("UNCHECKED_CAST_TO_EXTERNAL_INTERFACE")
    override fun digest(): ByteArray {
        val localKey = key
        val localData = dataToHash
        when (dig.toString()) {
            "MD2" -> throw NotImplementedError("Not implemented")
            "MD4" -> throw NotImplementedError("Not implemented") // "md4"
            "MD5" -> throw NotImplementedError("Not implemented") // "md5"
            "SHA-0" -> throw NotImplementedError("Not implemented")
            "SHA-1" -> throw NotImplementedError("Not implemented") // "sha1"
            "SHA-512/224" -> throw NotImplementedError("Not implemented") // "sha512_224"
            "SHA-512/256" -> throw NotImplementedError("Not implemented") // "sha512_256"
            "SHA3-224" -> throw NotImplementedError("Not implemented")
            "SHA3-256" -> throw NotImplementedError("Not implemented")
            "SHA3-384" -> throw NotImplementedError("Not implemented")
            "SHA3-512" -> throw NotImplementedError("Not implemented")
            else -> {
                when (dig.toString()) {
                    "SHA-224" -> {
                        val hmac = hash.hmac(hash.sha224 as BlockHash<Any>, localKey)
                        return hmac.update(localData).digest().map { it.toByte() }.toByteArray()
                    }
                    "SHA-256" -> {
                        val hmac = hash.hmac(hash.sha256 as BlockHash<Any>, localKey)
                        return hmac.update(localData).digest().map { it.toByte() }.toByteArray()
                    }
                    "SHA-384" -> {
                        val hmac = hash.hmac(hash.sha384 as BlockHash<Any>, localKey)
                        return hmac.update(localData).digest().map { it.toByte() }.toByteArray()
                    }
                    "SHA-512" -> {
                        val hmac = hash.hmac(hash.sha512 as BlockHash<Any>, localKey)
                        return hmac.update(localData).digest().map { it.toByte() }.toByteArray()
                    }
                    else -> throw NotImplementedError("Not implemented")
                }
            }
        }
//        return if (jsTypeOf(window) != "undefined") {
//            js("""
//                var algorithm = { name: "HMAC", hash: "SHA-256" };
//                window.crypto.subtle.importKey(
//                    "raw",
//                    localKey,
//                    algorithm,
//                    false,
//                    ["sign", "verify"]
//                );
//            """) as ByteArray
//        } else {
//            val result = js("""
//                var crypto = require('crypto');
//                crypto
//            """)
//            println(result)
//            js("""
//                var crypto = require('crypto');
//                console.log(crypto);
//                var hmac = crypto.createHmac(algorithm, localKey);
//                hmac.update(localData);
//                hmac.digest();
//            """) as ByteArray
//        }
    }

    override fun digest(input: ByteArray): ByteArray {
        dataToHash += input
        return digest()
    }
}
