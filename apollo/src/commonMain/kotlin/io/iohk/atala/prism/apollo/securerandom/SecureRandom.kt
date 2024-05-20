package io.iohk.atala.prism.apollo.securerandom

/**
 * The SecureRandom class provides a platform-specific implementation for generating secure random numbers.
 */
expect class SecureRandom
/**
 * The SecureRandom class provides a platform-specific implementation for generating secure random numbers.
 *
 * @constructor Creates an instance of SecureRandom with an optional seed value.
 * @param seed The seed value used for initializing the random number generator.
 */
constructor(
    seed: ByteArray = ByteArray(0)
) : SecureRandomInterface {
    val seed: ByteArray

    companion object : SecureRandomStaticInterface
}
