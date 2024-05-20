package org.hyperledger.identus.apollo.secp256k1

import com.ionspin.kotlin.bignum.integer.BigInteger
import com.ionspin.kotlin.bignum.integer.Sign

/**
 * Helper class for working with the Secp256k1 elliptic curve cryptography algorithm.
 */
class Secp256k1Helper {
    companion object {
        /**
         * Validates a public key using the Secp256k1 elliptic curve cryptography algorithm.
         *
         * @param pubKey The public key to validate as a byte array.
         * @return True if the public key is valid, false otherwise.
         */
        fun validatePublicKey(pubKey: ByteArray): Boolean {
            val x = BigInteger.fromByteArray(pubKey.sliceArray(1..32), Sign.POSITIVE)
            val y = BigInteger.fromByteArray(pubKey.sliceArray(33..64), Sign.POSITIVE)
            val b = BigInteger(7)
            val p = BigInteger.parseString("115792089237316195423570985008687907853269984665640564039457584007908834671663", 10)
            return ((y * y - x * x * x - b) mod p) == BigInteger.ZERO
        }
    }
}

/**
 * Exception class for Secp256k1 operations.
 */
public class Secp256k1Exception : RuntimeException {
    public constructor() : super()
    public constructor(message: String?) : super(message)
}
