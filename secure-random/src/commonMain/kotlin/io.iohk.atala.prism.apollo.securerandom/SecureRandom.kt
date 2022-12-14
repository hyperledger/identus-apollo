package io.iohk.atala.prism.apollo.securerandom

expect class SecureRandom
/**
 * @param seed the seed.
 */
constructor(
    seed: ByteArray = ByteArray(0)
) : SecureRandomInterface {
    val seed: ByteArray

    companion object : SecureRandomStaticInterface
}
