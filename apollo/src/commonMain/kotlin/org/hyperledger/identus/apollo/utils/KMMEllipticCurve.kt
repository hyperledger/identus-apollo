package org.hyperledger.identus.apollo.utils

/**
 * Represents a named elliptic curve.
 *
 * @property value The string value representing the elliptic curve.
 */
enum class KMMEllipticCurve(val value: String) {
    SECP256k1("secp256k1"),
    SECP256r1("secp256r1")
}
