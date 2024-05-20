package org.hyperledger.identus.apollo.securerandom

/**
 * The SecureRandomInterface interface defines the contract for generating secure random numbers.
 */
interface SecureRandomInterface {
    /**
     * Generates a specified number of secure random bytes.
     *
     * @param size The number of random bytes to generate.
     * @return A byte array containing the generated random bytes.
     */
    fun nextBytes(size: Int): ByteArray
}
