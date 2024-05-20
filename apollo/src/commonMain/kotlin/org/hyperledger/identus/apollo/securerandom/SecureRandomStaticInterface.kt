package org.hyperledger.identus.apollo.securerandom

/**
 * The SecureRandomStaticInterface interface defines the contract for generating secure random numbers.
 */
interface SecureRandomStaticInterface {
    /**
     * Generates a random seed of specified length in bytes.
     *
     * @param numBytes The length of the seed in bytes.
     * @return The generated seed as a ByteArray.
     */
    fun generateSeed(numBytes: Int): ByteArray
}
