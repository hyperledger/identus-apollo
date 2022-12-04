package io.iohk.atala.prism.apollo.hashing.internal

/**
 * Digests are secure one-way hash functions that take arbitrary-sized
 * data and output a fixed-length hash value.
 *
 * An interface that all implemented hashing algorithm will implement.
 */
public interface Digest {
    /**
     * the internal block length (in bytes), or `-n`
     */
    public val blockLength: Int

    /**
     * the digest output length (in bytes)
     */
    public val digestLength: Int

    /**
     * Updates the digest using the specified byte.
     *
     * @param input the byte with which to update the digest.
     */
    public fun update(input: Byte)

    /**
     * Updates the digest using the specified array of bytes.
     *
     * @param input the array of bytes.
     */
    public fun update(input: ByteArray)

    /**
     * Updates the digest using the specified array of bytes, starting
     * at the specified offset.
     *
     * @param input the array of bytes.
     * @param offset the offset to start from in the array of bytes.
     * @param length the number of bytes to use, starting at [offset]
     */
    public fun update(input: ByteArray, offset: Int, length: Int)

    /**
     * Completes the hash computation by performing final operations
     * such as padding. The digest is reset after this call is made.
     *
     * @return the array of bytes for the resulting hash value.
     */
    public fun digest(): ByteArray

    /**
     * Performs a final update on the digest using the specified array
     * of bytes, then completes the digest computation. That is, this
     * method first calls [update], passing the [input] array to the
     * [update] method, then calls [digest].
     *
     * @param input the input to be updated before the digest is
     * completed.
     * @return the array of bytes for the resulting hash value.
     */
    public fun digest(input: ByteArray): ByteArray

    /**
     * Completes the hash computation by performing final operations
     * such as padding. The digest is reset after this call is made.
     *
     * @param output output buffer for the computed digest
     * @param offset offset into the output buffer to begin storing the digest
     * @param length number of bytes within buf allotted for the digest
     * @return the number of bytes placed into [output]
     * @throws [IllegalArgumentException]
     */
    @Throws(IllegalArgumentException::class)
    public fun digest(output: ByteArray, offset: Int, length: Int): Int

    /**
     * Resets the digest for further use.
     */
    public fun reset()

    /**
     * Get the display name for this function (e.g. `"SHA-1"` for SHA-1).
     */
    public override fun toString(): String
}
