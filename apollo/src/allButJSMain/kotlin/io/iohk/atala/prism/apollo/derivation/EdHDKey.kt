package io.iohk.atala.prism.apollo.derivation

import com.ionspin.kotlin.bignum.integer.BigInteger
import com.ionspin.kotlin.bignum.integer.toBigInteger
import ed25519_bip32.XPrvWrapper
import io.iohk.atala.prism.apollo.utils.ECConfig
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport
import kotlin.js.JsName


/**
 * Represents and HDKey with its derive methods
 */
@OptIn(ExperimentalJsExport::class)
@JsExport
class EdHDKey(
    val privateKey: ByteArray,
    val chainCode: ByteArray,
    val publicKey: ByteArray? = null,
    val depth: Int = 0,
    val index: BigIntegerWrapper = BigIntegerWrapper(0)
) {
    /**
     * Constructs a new EdHDKey object from a seed, depth, and child index.
     *
     * @param seed The seed used to derive the private key and chain code.
     * @param depth The depth of the HDKey.
     * @param childIndex The child index of the HDKey.
     *
     * @throws IllegalArgumentException if the seed length is not equal to 64.
     */

    fun initFromSeed(seed: ByteArray): EdHDKey {
        require(seed.size == 64) {
            "Seed expected byte length to be ${ECConfig.PRIVATE_KEY_BYTE_SIZE}"
        }

        val key = seed.sliceArray(0 until 32)
        val chainCode = seed.sliceArray(32 until seed.size)
        val wrapper = XPrvWrapper.fromNonextendedNoforce(key, chainCode)

        return EdHDKey(
            privateKey = wrapper.extendedSecretKey(),
            chainCode = wrapper.chainCode(),
        )
    }

    /**
     * Method to derive an HDKey by a path
     *
     * @param path value used to derive a key
     */
    fun derive(path: String): EdHDKey {
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
            val idx = m[1].toBigInteger()
            if (idx >= HDKey.HARDENED_OFFSET) {
                throw Error("Invalid index")
            }
            val finalIdx = if (m[2] == "'") idx + HDKey.HARDENED_OFFSET else idx

            child = child.deriveChild(BigIntegerWrapper(finalIdx))
        }

        return child
    }

    /**
     * Method to derive an HDKey child by index
     *
     * @param index value used to derive a key
     */
    fun deriveChild(wrappedIndex: BigIntegerWrapper): EdHDKey {
        val extendedKey = privateKey.plus(chainCode)
        val wrapper = XPrvWrapper.fromBytes(extendedKey)
        val derived = wrapper.derive(wrappedIndex.value.uintValue())

        return EdHDKey(
            privateKey = derived.extendedSecretKey(),
            chainCode = derived.chainCode(),
            depth = depth + 1,
            index = wrappedIndex
        )
    }
}