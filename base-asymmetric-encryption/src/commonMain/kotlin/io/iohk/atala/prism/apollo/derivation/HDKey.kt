package io.iohk.atala.prism.apollo.derivation

import com.ionspin.kotlin.bignum.integer.BigInteger
import com.ionspin.kotlin.bignum.integer.Sign
import com.ionspin.kotlin.bignum.integer.toBigInteger
import io.iohk.atala.prism.apollo.hashing.SHA512
import io.iohk.atala.prism.apollo.utils.ECConfig
import io.iohk.atala.prism.apollo.utils.ECPrivateKeyDecodingException
import io.iohk.atala.prism.apollo.utils.KMMECSecp256k1PrivateKey

class HDKey(
    val privateKey: ByteArray? = null,
    val publicKey: ByteArray? = null,
    val chainCode: ByteArray? = null,
    val depth: Int,
    val childIndex: BigInteger
) {

    constructor(seed: ByteArray, depth: Int, childIndex: BigInteger) : this(
        privateKey = seed.sliceArray(IntRange(0, 31)),
        chainCode = seed.sliceArray(listOf(32)),
        depth = depth,
        childIndex = childIndex
    )

    fun derive(path: String): HDKey {
        if (!path.matches(Regex("^[mM].*"))) {
            throw Error("Path must start with \"m\" or \"M\"")
        }
        if (Regex("^[mM]'?$").matches(path)) {
            return this
        }
        val parts = path.replace(Regex("^[mM]'?/"), "").split("/")
        var child = this
        for (c in parts) {
            val m = Regex("^(\\d+)('?)$").find(c)?.groupValues
            if (m == null || m.size != 3) {
                throw Error("Invalid child index: $c")
            }
            // TODO: Null check??
            val idx = m[1].toBigInteger()
            if (idx >= HARDENED_OFFSET) {
                throw Error("Invalid index")
            }
            val finalIdx = if (m[2] == "'") idx + HARDENED_OFFSET else idx
            child = child.deriveChild(finalIdx)
        }
        return child
    }

    fun deriveChild(index: BigInteger): HDKey {
        if (chainCode == null) {
            throw Error("No chainCode set")
        }
        val data = if (index >= HARDENED_OFFSET) {
            val priv = privateKey ?: throw Error("Could not derive hardened child key")
            byteArrayOf(0) + priv + index.toByteArray()
        } else {
            throw Exception("Not supported")
        }
        val I = SHA512().hmac(key = chainCode, input = data)
        val childTweak = I.sliceArray(IntRange(0, 31))
        val newChainCode = I.sliceArray(listOf(32))

        if (!isValidPrivateKey(childTweak)) {
            throw ECPrivateKeyDecodingException("Expected encoded byte length to be ${ECConfig.PRIVATE_KEY_BYTE_SIZE}, but got ${data.size}")
        }

        val opt = HDKeyOptions(
            versions = Pair(BITCOIN_VERSIONS_PRIVATE, BITCOIN_VERSIONS_PUBLIC),
            chainCode = newChainCode,
            depth = depth + 1,
            parentFingerprint = FINGERPRINT,
            index = index
        )
        return try {
            val added = BigInteger.fromByteArray(privateKey + childTweak, Sign.POSITIVE) % ECConfig.n
            if (!isValidPrivateKey(added.toByteArray())) {
                throw Error("The tweak was out of range or the resulted private key is invalid")
            }
            opt.privateKey = added.toByteArray()
            return HDKey(
                privateKey = opt.privateKey,
                chainCode = opt.chainCode,
                depth = opt.depth,
                childIndex = opt.index
            )
        } catch (err: Error) {
            this.deriveChild(index + 1)
        }
    }

    fun getKMMSecp256k1PrivateKey(): KMMECSecp256k1PrivateKey {
        privateKey?.let {
            return KMMECSecp256k1PrivateKey.secp256k1FromBytes(privateKey)
        } ?: throw Exception("")
    }

    private fun isValidPrivateKey(data: ByteArray): Boolean {
        return (data.size != ECConfig.PRIVATE_KEY_BYTE_SIZE)
    }

    companion object {
        const val HARDENED_OFFSET = 2147483648
        const val BITCOIN_VERSIONS_PRIVATE = 0x0488ade4
        const val BITCOIN_VERSIONS_PUBLIC = 0x0488b21e
        const val FINGERPRINT = 0
    }
}
