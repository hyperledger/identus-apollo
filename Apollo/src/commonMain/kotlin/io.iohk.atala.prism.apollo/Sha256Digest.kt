package io.iohk.atala.prism.apollo

import io.iohk.atala.prism.apollo.util.BytesOps
import kotlin.js.JsExport
import kotlin.jvm.JvmStatic

/**
 * This class represents a hash digest returned by SHA-256 cryptographic hash function. Contains
 * exactly 32 bytes.
 */
@JsExport
public data class Sha256Digest internal constructor(val value: ByteArray) {
    public companion object {
        private const val BYTE_LENGTH = 32
        private val HEX_STRING_RE = Regex("^[0-9a-fA-F]{64}$")

        /**
         * @param string a string consisting of 64 hex characters (one of 0-9, A-F, a-f)
         * @return SHA-256 digest corresponding to the given hex string
         */
        @JvmStatic
        public fun fromHex(string: String): Sha256Digest {
            if (HEX_STRING_RE.matches(string))
                return Sha256Digest(BytesOps.hexToBytes(string))
            else
                throw IllegalArgumentException("The given hex string doesn't correspond to a valid SHA-256 hash encoded as string")
        }

        /**
         * @param bytes a 32-length byte array
         * @return SHA-256 digest corresponding to the given byte array
         */
        @JvmStatic
        public fun fromBytes(bytes: ByteArray): Sha256Digest {
            if (bytes.size == BYTE_LENGTH)
                return Sha256Digest(bytes)
            else
                throw IllegalArgumentException("The given byte array does not correspond to a SHA256 hash. It must have exactly $BYTE_LENGTH bytes")
        }
    }

    /**
     * Hex representation of the underlying byte array.
     */
    val hexValue: String
        get() = BytesOps.bytesToHex(value)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as Sha256Digest

        if (!value.contentEquals(other.value)) return false

        return true
    }

    override fun hashCode(): Int {
        return value.contentHashCode()
    }
}
