package io.iohk.atala.prism.apollo.derivation

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

        private fun parseAxis(axis: String): DerivationAxis {
            val hardened = axis.endsWith("'")
            val axisNumStr = if (hardened) axis.substring(0, axis.length - 1) else axis
            val axisNum = axisNumStr.toInt()
            return if (hardened) DerivationAxis.hardened(axisNum) else DerivationAxis.normal(axisNum)
        }
    }
}
