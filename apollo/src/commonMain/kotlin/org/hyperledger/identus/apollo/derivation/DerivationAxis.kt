package io.iohk.atala.prism.apollo.derivation

import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport
import kotlin.jvm.JvmStatic

/**
 * Represent an axis on BIP 32 key derivation path
 *
 * BIP 32 standard defines how keys can be derived from another one for index between 0 and 2^32^ - 1, where
 * indices between 0 and 2^31^ - 1 are called normal indices and between 2^31^ and 2^32^ - 1 hardened indices.
 * Natural way to represent such thing is unsigned 32-bit integer, but JVM (and Scala) doesn't support such
 * data type. That is why signed 32-bit is used instead, with the same bit representation. In other words
 * unsigned index used here is equivalent to canonical, unsigned one modulo 2^32^.
 *
 * Implementation details are mostly hidden from the user, so user can either choose to create a normal
 * axis, providing number between 0 and 2^31^ - 1 or hardened one, providing a number from the same range.
 */
@OptIn(ExperimentalJsExport::class)
@JsExport
class DerivationAxis internal constructor(val i: Int) {
    /**
     * Represents if the axis is hardened
     */
    val hardened: Boolean
        get() = ((i shr 31) and 1) == 1

    /**
     * Number corresponding to the axis (different for index), always between 0 and 2^31^
     */
    val number: Int
        get() = i and (1 shl 31).inv()

    /**
     * Renders axis as number with optional ' for hardened path, e.g. 1 or 7'
     */
    override fun toString(): String =
        if (hardened) {
            "$number'"
        } else {
            i.toString()
        }

    /**
     * Returns the hash code value for the object.
     *
     * @return the hash code value for the object.
     */
    override fun hashCode(): Int {
        return i.hashCode()
    }

    /**
     * Checks if the given object is equal to this DerivationAxis.
     *
     * @param other the object to compare
     * @return true if the given object is equal to this DerivationAxis, false otherwise
     */
    override fun equals(other: Any?): Boolean {
        return other is DerivationAxis && number == other.number
    }

    companion object {
        /**
         * Creates normal (non-hardened) axis
         * @param num number corresponding to the axis, must be between 0 and 2^31^ - 1
         */
        @JvmStatic
        fun normal(num: Int): DerivationAxis {
            require(num >= 0)
            return DerivationAxis(num)
        }

        /**
         * Creates hardened axis
         * @param num number corresponding to the axis, must be between 0 and 2^31^ - 1
         */
        @JvmStatic
        fun hardened(num: Int): DerivationAxis {
            require(num >= 0)
            return DerivationAxis(num or (1 shl 31))
        }
    }
}
