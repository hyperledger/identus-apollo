package io.iohk.atala.prism.apollo

public expect object Sha256 {
    /**
     * Applies SHA-256 cryptographic hash function to the given array of bytes.
     *
     * @param bytes array of bytes to be hashed
     * @return 32 bytes with the resulting hash
     */
    public fun compute(bytes: ByteArray): Sha256Digest
}
