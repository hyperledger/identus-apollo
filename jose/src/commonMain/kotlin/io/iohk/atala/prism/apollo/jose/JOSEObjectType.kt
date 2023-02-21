package io.iohk.atala.prism.apollo.jose

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.jvm.JvmStatic

/**
 * JOSE object type, represents the {@code typ} header parameter in unsecured,
 * JSON Web Signature (JWS) and JSON Web Encryption (JWE) objects. This class
 * is immutable.
 *
 * <p>Includes constants for the following standard types:
 *
 * <ul>
 *     <li>{@link #JOSE}
 *     <li>{@link #JOSE_JSON JOSE+JSON}
 *     <li>{@link #JWT}
 * </ul>
 *
 * <p>Additional types can be defined using the constructor.
 */
@Serializable
class JOSEObjectType
/**
 * Creates a new JOSE object type.
 *
 * @param type The object type. Must not be {@code null}.
 */
constructor(
    /**
     * The object type.
     */
    private val type: String
) {
    /**
     * Returns the JSON string representation of this JOSE object type.
     *
     * @return The JSON string representation.
     */
    fun toJSONString(): String {
        return Json.encodeToString(this)
    }

    /**
     * Overrides [hashCode]
     *
     * @return The object hash code.
     */
    override fun hashCode(): Int {
        return type.lowercase().hashCode()
    }

    /**
     * Returns the string representation of this JOSE object type.
     *
     * @return The string representation.
     */
    override fun toString(): String {
        return type
    }

    /**
     * Overrides [equals].
     *
     * @param other The object to compare to.
     *
     * @return {@code true} if the objects have the same value, otherwise
     *         {@code false}.
     */
    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other == null || this::class != other::class) {
            return false
        }

        other as JOSEObjectType
        if (type.equals(other.type, ignoreCase = true).not()) {
            return false
        }

        return true
    }

    companion object {
        /**
         * Compact encoded JOSE object type.
         */
        @JvmStatic
        val JOSE = JOSEObjectType("JOSE")

        /**
         * JSON-encoded JOSE object type.
         */
        @JvmStatic
        val JOSE_JSON = JOSEObjectType("JOSE+JSON")


        /**
         * JSON Web Token (JWT) object type.
         */
        @JvmStatic
        val JWT = JOSEObjectType("JWT")
    }
}
