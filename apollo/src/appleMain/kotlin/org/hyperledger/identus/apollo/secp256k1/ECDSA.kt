package io.iohk.atala.prism.apollo.secp256k1

import fr.acinq.secp256k1.Secp256k1Native

/**
 * ECDSA class for creating and manipulating ECDSA signatures.
 */
class ECDSA {
    /**
     * Create a normalized ECDSA signature.
     *
     * @param message message to sign.
     * @param privateKey signer's private key.
     */
    fun sign(message: ByteArray, privateKey: ByteArray): ByteArray {
        return Secp256k1Native.sign(message, privateKey)
    }

    /**
     * Recover a public key from an ECDSA signature.
     *
     * @param sig ecdsa compact signature (64 bytes).
     * @param message message signed.
     * @param recid recoveryId (should have been provided with the signature to allow recovery).
     */
    fun ecdsaRecover(sig: ByteArray, message: ByteArray, recid: Int): ByteArray {
        return Secp256k1Native.ecdsaRecover(sig, message, recid)
    }

    /**
     * Convert a compact ECDSA signature (64 bytes) to a der-encoded ECDSA signature.
     */
    fun compact2der(sig: ByteArray): ByteArray {
        return Secp256k1Native.compact2der(sig)
    }
}
