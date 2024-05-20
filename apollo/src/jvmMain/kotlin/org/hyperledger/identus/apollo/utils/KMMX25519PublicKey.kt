package org.hyperledger.identus.apollo.utils

/**
 * Represents a public key for the X25519 elliptic curve encryption algorithm.
 *
 * @property raw The binary representation of the public key.
 * @constructor Creates a new instance of [KMMX25519PublicKey] with the given [raw] value.
 */
actual class KMMX25519PublicKey(val raw: ByteArray)
