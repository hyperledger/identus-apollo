package io.iohk.atala.prism.apollo.jose

/**
 * Enumeration of JOSE algorithm implementation requirements. Refers to the
 * requirement levels defined in RFC 2119.
 *
 */
enum class Requirement {
    /**
     * The implementation of the algorithm is required.
     */
    REQUIRED,

    /**
     * The implementation of the algorithm is recommended.
     */
    RECOMMENDED,

    /**
     * The implementation of the algorithm is optional.
     */
    OPTIONAL
}
