package io.iohk.atala.prism.apollo.derivation

/**
 * Represents and HDKey with its derive methods
 */
expect class EdHDKey constructor(
    privateKey: ByteArray,
    chainCode: ByteArray,
    publicKey: ByteArray? = null,
    depth: Int = 0,
    index: BigIntegerWrapper = BigIntegerWrapper(0)
)  {
    val privateKey: ByteArray
    val chainCode: ByteArray
    val publicKey: ByteArray?
    val depth: Int
    val index: BigIntegerWrapper

    /**
     * Method to derive an HDKey by a path
     *
     * @param path value used to derive a key
     */
    fun derive(path: String): EdHDKey

    /**
     * Method to derive an HDKey child by index
     *
     * @param wrappedIndex value used to derive a key
     */
    fun deriveChild(wrappedIndex: BigIntegerWrapper): EdHDKey

    companion object {
        /**
         * Constructs a new EdHDKey object from a seed
         *
         * @param seed The seed used to derive the private key and chain code.
         * @throws IllegalArgumentException if the seed length is not equal to 64.
         */
        fun initFromSeed(seed: ByteArray): EdHDKey
    }
}
