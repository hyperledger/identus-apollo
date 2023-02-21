package io.iohk.atala.prism.apollo.jose

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.jvm.JvmStatic

/**
 * The base class for algorithm names, with optional implementation
 * requirement.
 *
 * <ul>
 *     <li>{@link #NONE none}
 * </ul>
 *
 */
@Serializable
class Algorithm(
    /**
     * The algorithm name.
     */
    private val name: String,
    /**
     * The implementation requirement, {@code null} if not known.
     */
    @Transient
    private val req: Requirement? = null
) {

    /**
     * Returns the JSON string representation of this algorithm.
     *
     * @return The JSON string representation.
     */
    fun toJSONString(): String {
        return Json.encodeToString(this)
    }

    /**
     * Returns the string representation of this algorithm.
     *
     * @return The string representation.
     */
    override fun toString(): String {
        return name
    }

    companion object {
        /**
         * No algorithm (unsecured JOSE object without signature / encryption).
         */
        @JvmStatic
        val NONE: Algorithm = Algorithm("none", Requirement.REQUIRED)

        /**
         * Parses an algorithm.
         *
         * @param string The string to parse.
         *
         * @return The JOSE algorithm
         */
        @JvmStatic
        fun parse(string: String): Algorithm {
            return Algorithm(string)
        }
    }
}
