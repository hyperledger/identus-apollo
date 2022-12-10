package io.iohk.atala.prism.apollo.derivation

import kotlin.js.JsExport
import kotlin.jvm.JvmStatic

/** Represent an axis on BIP 32 key derivation path
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
@JsExport
public class DerivationAxis internal constructor(public val i: Int) {
    public companion object {
        /** Creates normal (non-hardened) axis
         *  @param num number corresponding to the axis, must be between 0 and 2^31^ - 1
         */
        @JvmStatic
        public fun normal(num: Int): DerivationAxis {
            require(num >= 0)
            return DerivationAxis(num)
        }

        /** Creates hardened axis
         *  @param num number corresponding to the axis, must be between 0 and 2^31^ - 1
         */
        @JvmStatic
        public fun hardened(num: Int): DerivationAxis {
            require(num >= 0)
            return DerivationAxis(num or (1 shl 31))
        }
    }

    /** Represents if the axis is hardened */
    public val hardened: Boolean
        get() = ((i shr 31) and 1) == 1

    /** Number corresponding to the axis (different for index), always between 0 and 2^31^ */
    public val number: Int
        get() = i and (1 shl 31).inv()

    /** Renders axis as number with optional ' for hardened path, e.g. 1 or 7' */
    override fun toString(): String =
        if (hardened) {
            "$number'"
        } else {
            i.toString()
        }

    override fun hashCode(): Int {
        return i.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        return other is DerivationAxis && number == other.number
    }
}
