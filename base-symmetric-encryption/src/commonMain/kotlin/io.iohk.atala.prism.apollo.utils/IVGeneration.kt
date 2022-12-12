package io.iohk.atala.prism.apollo.utils

interface IVGeneration {
    /**
     * Generate random data with specified size
     *
     * @param size the size of the random generated data
     */
    fun createRandomIV(size: Int): ByteArray
}
