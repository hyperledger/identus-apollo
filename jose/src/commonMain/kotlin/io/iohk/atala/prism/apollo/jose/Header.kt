package io.iohk.atala.prism.apollo.jose

import io.iohk.atala.prism.apollo.base64.base64UrlPadDecoded
import io.iohk.atala.prism.apollo.base64.base64UrlPadEncoded
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.jsonObject
import kotlin.jvm.JvmStatic

/**
 * The base abstract class for unsecured ({@code alg=none}), JSON Web Signature (JWS) and
 * JSON Web Encryption (JWE) headers.
 */
abstract class Header
/**
 * Creates a new abstract header.
 *
 * @param alg The algorithm ({@code alg}) parameter.
 * @param typ The type ({@code typ}) parameter
 * @param cty The content type ({@code cty}) parameter
 * @param crit The names of the critical header ({@code crit}) parameters or empty set
 * @param customParams The custom parameters, empty map
 */
constructor(
    /**
     * The algorithm (`alg`) parameter.
     */
    private val alg: Algorithm,
    /**
     * The JOSE object type ({@code typ}) parameter.
     */
    private val typ: JOSEObjectType,
    /**
     * The content type (`cty`) parameter.
     */
    private val cty: String,
    /**
     * The critical headers (`crit`) parameter.
     */
    private val crit: Set<String> = setOf(),
    /**
     * Custom header parameters.
     */
    private val customParams: Map<String, Any> = mapOf()
) {

    /**
     * Gets the names of all included parameters (registered and custom) in
     * the header instance.
     *
     * @return The included parameters.
     */
    fun getIncludedParams(): Set<String> {
        val includedParameters: MutableSet<String> = HashSet(customParams.keys)
        includedParameters.add(HeaderParameterNames.ALGORITHM)
        includedParameters.add(HeaderParameterNames.TYPE)
        includedParameters.add(HeaderParameterNames.CONTENT_TYPE)
        if (crit.isNotEmpty()) {
            includedParameters.add(HeaderParameterNames.CRITICAL)
        }
        return includedParameters
    }

    /**
     * Returns a Base64URL representation of the header. If the header was
     * parsed always returns the original Base64URL (required for JWS
     * validation and authenticated JWE decryption).
     *
     * @return The original parsed Base64URL representation of the header,
     * or a new Base64URL representation if the header was created from scratch.
     */
    fun toBase64URL(): String {
        return toString().base64UrlPadEncoded
    }

    /**
     * Returns a JSON string representation of the header. All custom
     * parameters will be included if they serialise to a JSON entity and
     * their names don't conflict with the registered ones.
     *
     * @return The JSON string representation of the header.
     */
    fun toJSONString(): String {
        return Json.encodeToString(this)
    }

    /**
     * Returns a JSON string representation of the header. All custom
     * parameters will be included if they serialise to a JSON entity and
     * their names don't conflict with the registered ones.
     *
     * @return The JSON string representation of the header.
     */
    override fun toString(): String {
        return toJSONString()
    }

    companion object {
//        /**
//         * The max allowed string length when parsing a JOSE header (after the
//         * BASE64URL decoding). 20K chars should be sufficient to accommodate
//         * JOSE headers with an X.509 certificate chain in the {@code x5c}
//         * header parameter.
//         */
//        const val MAX_HEADER_STRING_LENGTH: Int = 20000

        /**
         * Parses a {@link PlainHeader}, {@link JWSHeader} or {@link JWEHeader}
         * from the specified JSON object string.
         *
         * @param jsonString The JSON object string to parse.
         *
         * @return The header.
         */
        @JvmStatic
        fun parse(jsonString: String): Header {
            return Json.decodeFromString(jsonString)
        }


        @JvmStatic
        fun parseBase64URL(base64URL: String): Header {
            return Json.decodeFromString(base64URL.base64UrlPadDecoded)
        }
    }
}