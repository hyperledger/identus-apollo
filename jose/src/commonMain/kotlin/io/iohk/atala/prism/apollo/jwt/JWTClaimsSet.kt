package io.iohk.atala.prism.apollo.jwt

import io.iohk.atala.prism.apollo.jose.Payload
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.jvm.JvmOverloads
import kotlin.jvm.JvmStatic

/**
 * JSON Web Token (JWT) claims set.
 *
 * <p>Supports all {{@link #getRegisteredNames()} registered claims} of the JWT
 * specification:
 *
 * <ul>
 *     <li>iss - Issuer
 *     <li>sub - Subject
 *     <li>aud - Audience
 *     <li>exp - Expiration Time
 *     <li>nbf - Not Before
 *     <li>iat - Issued At
 *     <li>jti - JWT ID
 * </ul>
 *
 * <p>The set may also contain custom claims; these will be serialised and
 * parsed along the registered ones.
 *
 * <p>Example JWT claims set:
 *
 * <pre>
 * {
 *   "sub"                        : "joe",
 *   "exp"                        : 1300819380,
 *   "http://example.com/is_root" : true
 * }
 * </pre>
 *
 * <p>Example usage:
 *
 * <pre>
 * val claimsSet: JWTClaimsSet = new JWTClaimsSet.Builder()
 *     .subject("joe")
 *     .expirationTime(new Date(1300819380 * 1000l)
 *     .claim("http://example.com/is_root", true)
 *     .build()
 * </pre>
 */
@Serializable
class JWTClaimsSet
/**
 * Creates a new JWT claims set.
 *
 * @param claims The JWT claims set as a map.
 */
private constructor(
    private val claims: Map<String, @Contextual Any?>
) {

    /**
     * Gets the specified claim (registered or custom).
     *
     * @param name The name of the claim. Must not be `null`.
     *
     * @return The value of the claim, `null` if not specified.
     */
    fun getClaim(name: String): Any? {
        return claims[name]
    }

    /**
     * Gets the specified claim (registered or custom) as [String]
     *
     * @param name The name of the claim.
     *
     * @return The value of the claim, {@code null} if not specified.
     *
     * @throws ParseException If the claim value is not of the required type.
     */
    @Throws(ParseException::class)
    fun getStringClaim(name: String): String {
        val value = claims[name]
        return if (value == null || value is String) {
            (value as String?)!!
        } else {
            throw ParseException("The $name claim is not a String")
        }
    }

    /**
     * Gets the specified claims (registered or custom) as an [Array] of [String].
     *
     * @param name The name of the claim. Must not be `null`.
     *
     * @return The value of the claim, `null` if not specified.
     *
     * @throws ParseException If the claim value is not of the required type.
     */
    @Throws(ParseException::class)
    fun getStringArrayClaim(name: String): Array<String>? {
        if (getClaim(name) == null) {
            return null
        }
        val list: List<*> = try {
            getClaim(name) as List<*>
        } catch (e: ClassCastException) {
            throw ParseException("The $name claim is not a list / JSON array")
        }
        val stringArray = arrayOfNulls<String>(list.size)
        for (i in stringArray.indices) {
            try {
                stringArray[i] = list[i] as String?
            } catch (e: ClassCastException) {
                throw ParseException("The $name claim is not a list / JSON array of strings")
            }
        }
        return stringArray.requireNoNulls()
    }

    /**
     * Gets the specified claims (registered or custom) as a [List] of [String].
     *
     * @param name The name of the claim. Must not be `null`.
     *
     * @return The value of the claim, `null` if not specified.
     *
     * @throws ParseException If the claim value is not of the required type.
     */
    @Throws(ParseException::class)
    fun getStringListClaim(name: String?): List<String>? {
        val stringArray = getStringArrayClaim(name!!) ?: return null
        return stringArray.toList()
    }

    /**
     * Gets the specified claim (registered or custom) as [Boolean].
     *
     * @param name The name of the claim. Must not be `null`.
     *
     * @return The value of the claim, `null` if not specified.
     *
     * @throws ParseException If the claim value is not of the required type.
     */
    @Throws(ParseException::class)
    fun getBooleanClaim(name: String): Boolean? {
        val value = getClaim(name)
        return if (value == null || value is Boolean) {
            value as Boolean?
        } else {
            throw ParseException("The \"$name\" claim is not a Boolean")
        }
    }

    /**
     * Gets the specified claim (registered or custom) as [Int].
     *
     * @param name The name of the claim. Must not be `null`.
     *
     * @return The value of the claim, `null` if not specified.
     *
     * @throws ParseException If the claim value is not of the required type.
     */
    @Throws(ParseException::class)
    fun getIntegerClaim(name: String): Int? {
        return when (val value = getClaim(name)) {
            null -> {
                null
            }
            is Number -> {
                value.toInt()
            }
            else -> {
                throw ParseException("The \"$name\" claim is not an Integer")
            }
        }
    }

    /**
     * Gets the specified claim (registered or custom) as [Long].
     *
     * @param name The name of the claim. Must not be `null`.
     *
     * @return The value of the claim, `null` if not specified.
     *
     * @throws ParseException If the claim value is not of the required
     * type.
     */
    @Throws(ParseException::class)
    fun getLongClaim(name: String): Long? {
        return when (val value = getClaim(name)) {
            null -> {
                null
            }
            is Number -> {
                value.toLong()
            }
            else -> {
                throw ParseException("The \"$name\" claim is not a Number")
            }
        }
    }

    /**
     * Gets the specified claim (registered or custom) as [LocalDateTime].
     * The claim may be represented by a Date object or a number of a seconds since the Unix epoch.
     *
     * @param name The name of the claim. Must not be `null`.
     *
     * @return The value of the claim, `null` if not specified.
     *
     * @throws ParseException If the claim value is not of the required type.
     */
    @Throws(ParseException::class)
    fun getDateClaim(name: String): LocalDateTime? {
        return when (val value = getClaim(name)) {
            null -> {
                null
            }
            is LocalDateTime -> {
                value as LocalDateTime?
            }
            is Number -> {
                Instant.fromEpochMilliseconds(value.toLong()).toLocalDateTime(TimeZone.UTC)
            }
            else -> {
                throw ParseException("The \"$name\" claim is not a Date")
            }
        }
    }

    /**
     * Gets the specified claim (registered or custom) as [Float].
     *
     * @param name The name of the claim. Must not be `null`.
     *
     * @return The value of the claim, `null` if not specified.
     *
     * @throws ParseException If the claim value is not of the required type.
     */
    @Throws(ParseException::class)
    fun getFloatClaim(name: String): Float? {
        return when (val value = getClaim(name)) {
            null -> {
                null
            }
            is Number -> {
                value.toFloat()
            }
            else -> {
                throw ParseException("The \"$name\" claim is not a Float")
            }
        }
    }

    /**
     * Gets the specified claim (registered or custom) as [Double].
     *
     * @param name The name of the claim. Must not be `null`.
     *
     * @return The value of the claim, `null` if not specified.
     *
     * @throws ParseException If the claim value is not of the required type.
     */
    @Throws(ParseException::class)
    fun getDoubleClaim(name: String): Double? {
        return when (val value = getClaim(name)) {
            null -> {
                null
            }
            is Number -> {
                value.toDouble()
            }
            else -> {
                throw ParseException("The \"$name\" claim is not a Double")
            }
        }
    }

    /**
     * Gets the issuer (`iss`) claim.
     *
     * @return The issuer claim, `null` if not specified.
     */
    fun getIssuer(): String? {
        return try {
            getStringClaim(JWTClaimNames.ISSUER)
        } catch (e: ParseException) {
            null
        }
    }

    /**
     * Gets the subject (`sub`) claim.
     *
     * @return The subject claim, `null` if not specified.
     */
    fun getSubject(): String? {
        return try {
            getStringClaim(JWTClaimNames.SUBJECT)
        } catch (e: ParseException) {
            null
        }
    }

    /**
     * Gets the audience (`aud`) claim.
     *
     * @return The audience claim, empty list if not specified.
     */
    fun getAudience(): List<String>? {
        val audValue = getClaim(JWTClaimNames.AUDIENCE)
        if (audValue is String) {
            // Special case
            return listOf(audValue)
        }
        val aud: List<String>? = try {
            getStringListClaim(JWTClaimNames.AUDIENCE)
        } catch (e: ParseException) {
            null
        }
        return aud
    }

    /**
     * Gets the expiration time (`exp`) claim.
     *
     * @return The expiration time, `null` if not specified.
     */
    fun getExpirationTime(): LocalDateTime? {
        return try {
            getDateClaim(JWTClaimNames.EXPIRATION_TIME)
        } catch (e: ParseException) {
            null
        }
    }

    /**
     * Gets the not-before (`nbf`) claim.
     *
     * @return The not-before claim, `null` if not specified.
     */
    fun getNotBeforeTime(): LocalDateTime? {
        return try {
            getDateClaim(JWTClaimNames.NOT_BEFORE)
        } catch (e: ParseException) {
            null
        }
    }

    /**
     * Gets the issued-at (`iat`) claim.
     *
     * @return The issued-at claim, `null` if not specified.
     */
    fun getIssueTime(): LocalDateTime? {
        return try {
            getDateClaim(JWTClaimNames.ISSUED_AT)
        } catch (e: ParseException) {
            null
        }
    }

    /**
     * Gets the JWT ID (`jti`) claim.
     *
     * @return The JWT ID claim, `null` if not specified.
     */
    fun getJWTID(): String? {
        return try {
            getStringClaim(JWTClaimNames.JWT_ID)
        } catch (e: ParseException) {
            null
        }
    }

    /**
     * Returns a JOSE object payload representation of this claims set.
     *
     * @return The payload representation.
     */
    fun toPayload(): Payload {
        return Payload()
    }

    /**
     * Returns a JSON object string representation of this claims set. The
     * claims are serialised according to their insertion order. Claims
     * with {@code null} values are not output.
     *
     * @return The JSON object string representation.
     */
    override fun toString(): String {
        return Json.encodeToString(this)
    }

    companion object {
        /**
         * The registered claim names.
         */
        @JvmStatic
        val REGISTERED_CLAIM_NAMES: Set<String> = setOf(
            JWTClaimNames.ISSUER,
            JWTClaimNames.SUBJECT,
            JWTClaimNames.AUDIENCE,
            JWTClaimNames.EXPIRATION_TIME,
            JWTClaimNames.NOT_BEFORE,
            JWTClaimNames.ISSUED_AT,
            JWTClaimNames.JWT_ID
        )

        /**
         * Gets the registered JWT claim names.
         *
         * @return The registered claim names, as an unmodifiable set.
         */
        @JvmStatic
        fun getRegisteredNames(): Set<String> {
            return REGISTERED_CLAIM_NAMES
        }

        /**
         * Parses a JSON Web Token (JWT) claims set from the specified JSON String.
         *
         * @param jsonString The JSON String to parse.
         *
         * @return The JWT claims set.
         */
        @JvmStatic
        fun parse(jsonString: String): JWTClaimsSet {
            return Json.decodeFromString(jsonString)
        }

        /**
         * Builder for constructing JSON Web Token (JWT) claims sets.
         *
         * <p>Example usage:
         *
         * <pre>
         * val claimsSet: JWTClaimsSet = new JWTClaimsSet.Builder()
         *     .subject("joe")
         *     .expirationDate(Date(1300819380 * 1000l)
         *     .claim("http://example.com/is_root", true)
         *     .build()
         * </pre>
         */
        class Builder {
            /**
             * The claims.
             */
            private val claims: MutableMap<String, Any?> = mutableMapOf()

            /**
             * Sets the issuer ({@code iss}) claim.
             *
             * @param iss The issuer claim, {@code null} if not specified.
             *
             * @return This builder.
             */
            @JvmOverloads
            fun issuer(iss: String? = null): Builder {
                claims[JWTClaimNames.ISSUER] = iss
                return this
            }

            /**
             * Sets the subject (`sub`) claim.
             *
             * @param sub The subject claim, `null` if not specified.
             *
             * @return This builder.
             */
            @JvmOverloads
            fun subject(sub: String? = null): Builder {
                claims[JWTClaimNames.SUBJECT] = sub
                return this
            }

            /**
             * Sets the audience (`aud`) claim.
             *
             * @param aud The audience claim, `null` if not
             * specified.
             *
             * @return This builder.
             */
            @JvmOverloads
            fun audience(aud: List<String>? = null): Builder {
                claims[JWTClaimNames.AUDIENCE] = aud
                return this
            }

            /**
             * Sets a single-valued audience (`aud`) claim.
             *
             * @param aud The audience claim, `null` if not
             * specified.
             *
             * @return This builder.
             */
            @JvmOverloads
            fun audience(aud: String? = null): Builder {
                aud?.let {
                    claims[JWTClaimNames.AUDIENCE] = listOf(aud)
                } ?: run {
                    claims[JWTClaimNames.AUDIENCE] = null
                }
                return this
            }

            /**
             * Sets the expiration time (`exp`) claim.
             *
             * @param exp The expiration time, `null` if not
             * specified.
             *
             * @return This builder.
             */
            @JvmOverloads
            fun expirationTime(exp: LocalDateTime? = null): Builder {
                claims[JWTClaimNames.EXPIRATION_TIME] = exp
                return this
            }

            /**
             * Sets the not-before (`nbf`) claim.
             *
             * @param nbf The not-before claim, `null` if not
             * specified.
             *
             * @return This builder.
             */
            @JvmOverloads
            fun notBeforeTime(nbf: LocalDateTime? = null): Builder {
                claims[JWTClaimNames.NOT_BEFORE] = nbf
                return this
            }

            /**
             * Sets the issued-at (`iat`) claim.
             *
             * @param iat The issued-at claim, `null` if not
             * specified.
             *
             * @return This builder.
             */
            @JvmOverloads
            fun issueTime(iat: LocalDateTime? = null): Builder {
                claims[JWTClaimNames.ISSUED_AT] = iat
                return this
            }

            /**
             * Sets the JWT ID (`jti`) claim.
             *
             * @param jti The JWT ID claim, `null` if not specified.
             *
             * @return This builder.
             */
            @JvmOverloads
            fun jwtID(jti: String? = null): Builder {
                claims[JWTClaimNames.JWT_ID] = jti
                return this
            }

            /**
             * Sets the specified claim (registered or custom).
             *
             * @param name  The name of the claim to set.
             * @param value The value of the claim to set, `null` if
             * not specified. Should map to a JSON entity.
             *
             * @return This builder.
             */
            @JvmOverloads
            fun claim(name: String, value: Any? = null): Builder {
                claims[name] = value
                return this
            }

            /**
             * Gets the claims (registered and custom).
             *
             * Note that the registered claims Expiration-Time
             * (`exp`), Not-Before-Time (`nbf`) and Issued-At
             * (`iat`) will be returned as `Date` instances.
             *
             * @return The claims, as an unmodifiable map, empty map if none.
             */
            fun getClaims(): Map<String, Any?> {
                return claims
            }

            /**
             * Builds a new JWT claims set.
             *
             * @return The JWT claims set.
             */
            fun build(): JWTClaimsSet {
                return JWTClaimsSet(claims)
            }
        }
    }
}
