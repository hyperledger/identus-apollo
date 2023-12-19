package io.iohk.atala.prism.apollo.utils

/**
 * Represents a public key for the X25519 elliptic curve encryption algorithm.
 *
 * @property raw The binary representation of the public key.
 */
actual class KMMX25519PublicKey(val raw: ByteArray)
