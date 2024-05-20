package org.hyperledger.identus.apollo.utils

/**
 * Definition of the KMMEdPrivateKey functionality
 */
public expect class KMMEdPrivateKey {

    /**
     * Method to sign a message using this KMMEdPrivateKey
     *
     * @param message ByteArray representing the message to be signed
     * @return ByteArray representing the signed message
     */
    fun sign(message: ByteArray): ByteArray
}
