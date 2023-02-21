package io.iohk.atala.prism.apollo.jwt

/**
 * JSON Web Token (JWT) claim names. The claim names defined in
 * <a href="https://datatracker.ietf.org/doc/html/rfc7519">RFC 7519</a> (JWT)
 * and other standards, such as OpenID Connect, are tracked in a
 * <a href="https://www.iana.org/assignments/jwt/jwt.xhtml#claims">JWT claims
 * registry</a> administered by IANA.
 */
object JWTClaimNames {
    /**
     * @see [RFC 7519 "iss"](https://datatracker.ietf.org/doc/html/rfc7519.section-4.1.1)
     */
    const val ISSUER = "iss"

    /**
     * @see [RFC 7519 "sub"](https://datatracker.ietf.org/doc/html/rfc7519.section-4.1.2)
     */
    const val SUBJECT = "sub"

    /**
     * @see [RFC 7519 "aud"](https://datatracker.ietf.org/doc/html/rfc7519.section-4.1.3)
     */
    const val AUDIENCE = "aud"

    /**
     * @see [RFC 7519 "exp"](https://datatracker.ietf.org/doc/html/rfc7519.section-4.1.4)
     */
    const val EXPIRATION_TIME = "exp"

    /**
     * @see [RFC 7519 "nbf"](https://datatracker.ietf.org/doc/html/rfc7519.section-4.1.5)
     */
    const val NOT_BEFORE = "nbf"

    /**
     * @see [RFC 7519 "iat"](https://datatracker.ietf.org/doc/html/rfc7519.section-4.1.6)
     */
    const val ISSUED_AT = "iat"

    /**
     * @see [RFC 7519 "jti"](https://datatracker.ietf.org/doc/html/rfc7519.section-4.1.7)
     */
    const val JWT_ID = "jti"
}
