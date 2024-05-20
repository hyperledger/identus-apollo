package org.hyperledger.identus.apollo.derivation

import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport
import kotlin.jvm.JvmStatic

/**
 * Represents derivation path in BIP 32 protocol
 */
@OptIn(ExperimentalJsExport::class)
@JsExport
data class DerivationPath(val axes: List<DerivationAxis>) {
    /**
     * Creates child derivation path for given index, hardened or not
     */
    fun derive(axis: DerivationAxis): DerivationPath =
        copy(axes = axes + axis)

    /**
     * Returns a string representation of the DerivationPath object.
     *
     * @return The string representation of the DerivationPath object.
     */
    override fun toString(): String =
        (listOf("m") + axes.map { it.toString() }).joinToString("/")

    companion object {
        /**
         * Constructs empty derivation path
         */
        @JvmStatic
        fun empty(): DerivationPath = DerivationPath(emptyList())

        /**
         * Parses string representation of derivation path
         *
         * @param path Path to parse in format m/axis1/axis2/.../axisn where all axes are number between 0 and 2^31^ - 1 and
         * optionally a ' added after to mark hardened axis e.g. m/21/37'/0
         */
        @JvmStatic
        fun fromPath(path: String): DerivationPath {
            val splitPath = path.split("/")
            if (splitPath.firstOrNull()?.trim()?.lowercase() != "m") {
                throw IllegalArgumentException("Path needs to start with m or M")
            } else {
                return DerivationPath(splitPath.drop(1).map(this::parseAxis))
            }
        }

        /**
         * Parses the given axis string and returns the corresponding DerivationAxis object.
         *
         * @param axis The axis string to parse. The string should be a number between 0 and 2^31^ - 1. If the axis is hardened,
         *              a ' should be added after the number. Example: "21'" for a hardened axis or "7" for a normal axis.
         * @return The parsed DerivationAxis object.
         */
        private fun parseAxis(axis: String): DerivationAxis {
            val hardened = axis.endsWith("'")
            val axisNumStr = if (hardened) axis.substring(0, axis.length - 1) else axis
            val axisNum = axisNumStr.toInt()
            return if (hardened) DerivationAxis.hardened(axisNum) else DerivationAxis.normal(axisNum)
        }
    }
}
