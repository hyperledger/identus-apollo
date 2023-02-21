package io.iohk.atala.prism.apollo.jose

/**
 * JSON Web Signature (JWS) and JSON Web Encryption (JWE) header parameter
 * names.
 *
 * The header parameter names defined in
 * [RFC 7515](https://datatracker.ietf.org/doc/html/rfc7515) (JWS),
 * [RFC 7516](https://datatracker.ietf.org/doc/html/rfc7516) (JWE)
 * and other JOSE related standards are tracked in a
 * [JWS and JWE header parameters registry](https://www.iana.org/assignments/jose/jose.xhtml#web-signature-encryption-header-parameters) administered by IANA.
 */
object HeaderParameterNames {
    ////////////////////////////////////////////////////////////////////////////////
    // Generic JWS and JWE Header Parameters
    ////////////////////////////////////////////////////////////////////////////////
    /**
     * Used in [JWSHeader] and [JWEHeader].
     *
     * @see [RFC 7515 "alg"
     * @see [RFC 7516 "alg"](https://datatracker.ietf.org/doc/html/rfc7516.section-4.1.1)](https://datatracker.ietf.org/doc/html/rfc7515.section-4.1.1)
     */
    const val ALGORITHM = "alg"

    /**
     * Used in [JWEHeader].
     *
     * @see [RFC 7516 "enc"](https://datatracker.ietf.org/doc/html/rfc7516.section-4.1.2)
     */
    const val ENCRYPTION_ALGORITHM = "enc"

    /**
     * Used in [JWEHeader].
     *
     * @see [RFC 7516 "zip"](https://datatracker.ietf.org/doc/html/rfc7516.section-4.1.3)
     */
    const val COMPRESSION_ALGORITHM = "zip"

    /**
     * Used in [JWSHeader] and [JWEHeader].
     *
     * @see [RFC 7515 "jku"
     * @see [RFC 7516 "jku"](https://datatracker.ietf.org/doc/html/rfc7516.section-4.1.4)](https://datatracker.ietf.org/doc/html/rfc7515.section-4.1.2)
     */
    const val JWK_SET_URL = "jku"

    /**
     * Used in [JWSHeader] and [JWEHeader].
     *
     * @see [RFC 7515 "jwk"
     * @see [RFC 7516 "jwk"](https://datatracker.ietf.org/doc/html/rfc7516.section-4.1.5)](https://datatracker.ietf.org/doc/html/rfc7515.section-4.1.3)
     */
    const val JWK = "jwk"

    /**
     * Used in [JWSHeader] and [JWEHeader].
     *
     * @see [RFC 7515 "kid"
     * @see [RFC 7516 "kid"](https://datatracker.ietf.org/doc/html/rfc7516.section-4.1.6)](https://datatracker.ietf.org/doc/html/rfc7515.section-4.1.4)
     */
    const val KEY_ID = "kid"

    /**
     * Used in [JWSHeader] and [JWEHeader].
     *
     * @see [RFC 7515 "x5u"
     * @see [RFC 7516 "x5u"](https://datatracker.ietf.org/doc/html/rfc7516.section-4.1.7)](https://datatracker.ietf.org/doc/html/rfc7515.section-4.1.5)
     */
    const val X_509_CERT_URL = "x5u"

    /**
     * Used in [JWSHeader] and [JWEHeader].
     *
     * @see [RFC 7515 "x5c"
     * @see [RFC 7516 "x5c"](https://datatracker.ietf.org/doc/html/rfc7516.section-4.1.8)](https://datatracker.ietf.org/doc/html/rfc7515.section-4.1.6)
     */
    const val X_509_CERT_CHAIN = "x5c"

    /**
     * Used in [JWSHeader] and [JWEHeader].
     *
     * @see [RFC 7515 "x5t"
     * @see [RFC 7516 "x5t"](https://datatracker.ietf.org/doc/html/rfc7516.section-4.1.9)](https://datatracker.ietf.org/doc/html/rfc7515.section-4.1.7)
     */
    const val X_509_CERT_SHA_1_THUMBPRINT = "x5t"

    /**
     * Used in [JWSHeader] and [JWEHeader].
     *
     * @see [RFC 7515 "x5t.S256"
     * @see [RFC 7516 "x5t.S256"](https://datatracker.ietf.org/doc/html/rfc7516.section-4.1.10)](https://datatracker.ietf.org/doc/html/rfc7515.section-4.1.8)
     */
    const val X_509_CERT_SHA_256_THUMBPRINT = "x5t#S256"

    /**
     * Used in [JWSHeader] and [JWEHeader].
     *
     * @see [RFC 7515 "typ"
     * @see [RFC 7516 "typ"](https://datatracker.ietf.org/doc/html/rfc7516.section-4.1.11)](https://datatracker.ietf.org/doc/html/rfc7515.section-4.1.9)
     */
    const val TYPE = "typ"

    /**
     * Used in [JWSHeader] and [JWEHeader].
     *
     * @see [RFC 7515 "cty"
     * @see [RFC 7516 "cty"](https://datatracker.ietf.org/doc/html/rfc7516.section-4.1.12)](https://datatracker.ietf.org/doc/html/rfc7515.section-4.1.10)
     */
    const val CONTENT_TYPE = "cty"

    /**
     * Used in [JWSHeader] and [JWEHeader].
     *
     * @see [RFC 7515 "crit"
     * @see [RFC 7516 "crit"](https://datatracker.ietf.org/doc/html/rfc7516.section-4.1.13)](https://datatracker.ietf.org/doc/html/rfc7515.section-4.1.11)
     */
    const val CRITICAL = "crit"

    ////////////////////////////////////////////////////////////////////////////////
    // Algorithm-Specific Header Parameters
    ////////////////////////////////////////////////////////////////////////////////
    /**
     * Used in [JWEHeader] with ECDH key agreement.
     *
     * @see [RFC 7518 "epk"](https://datatracker.ietf.org/doc/html/rfc7518.section-4.6.1.1)
     */
    const val EPHEMERAL_PUBLIC_KEY = "epk"

    /**
     * Used in [JWEHeader] with ECDH key agreement.
     *
     * @see [RFC 7518 "apu"](https://datatracker.ietf.org/doc/html/rfc7518.section-4.6.1.2)
     */
    const val AGREEMENT_PARTY_U_INFO = "apu"

    /**
     * Used in [JWEHeader] with ECDH key agreement.
     *
     * @see [RFC 7518 "apv"](https://datatracker.ietf.org/doc/html/rfc7518.section-4.6.1.3)
     */
    const val AGREEMENT_PARTY_V_INFO = "apv"

    /**
     * Used in [JWEHeader] with AES GCN key encryption.
     *
     * @see [RFC 7518 "iv"](https://datatracker.ietf.org/doc/html/rfc7518.section-4.7.1.1)
     */
    const val INITIALIZATION_VECTOR = "iv"

    /**
     * Used in [JWEHeader] with AES GCN key encryption.
     *
     * @see [RFC 7518 "tag"](https://datatracker.ietf.org/doc/html/rfc7518.section-4.7.1.2)
     */
    const val AUTHENTICATION_TAG = "tag"

    /**
     * Used in [JWEHeader] with PBES2 key encryption.
     *
     * @see [RFC 7518 "p2s"](https://datatracker.ietf.org/doc/html/rfc7518.section-4.8.1.1)
     */
    const val PBES2_SALT_INPUT = "p2s"

    /**
     * Used in [JWEHeader] with PBES2 key encryption.
     *
     * @see [RFC 7518 "p2c"
    ](https://datatracker.ietf.org/doc/html/rfc7518.section-4.8.1.2) */
    const val PBES2_COUNT = "p2c"

    /**
     * Used in [JWEHeader] with ECDH-1PU key agreement.
     *
     * @see ["skid" Header Parameter](https://datatracker.ietf.org/doc/html/draft-madden-jose-ecdh-1pu-04.section-2.2.1)
     */
    const val SENDER_KEY_ID = "skid"

    ////////////////////////////////////////////////////////////////////////////////
    // RFC 7797 (JWS Unencoded Payload Option) Header Parameters
    ////////////////////////////////////////////////////////////////////////////////
    /**
     * Used in [JWSHeader] with unencoded [Payload].
     *
     * @see [RFC 7797 "b64"](https://datatracker.ietf.org/doc/html/rfc7797.section-3)
     */
    const val BASE64_URL_ENCODE_PAYLOAD = "b64"
}
