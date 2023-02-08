package io.iohk.atala.prism.apollo.aes

import io.iohk.atala.prism.apollo.utils.KMMSymmetricKey

interface AESKeyGeneration {
    /**
     * Generate random AES key
     *
     * @param algorithm AES Key algorithm
     */
    suspend fun createRandomAESKey(algorithm: KAESAlgorithm, blockMode: KAESBlockMode = KAESBlockMode.GCM): KMMSymmetricKey
}
