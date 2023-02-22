package io.iohk.atala.prism.apollo.rsa

import io.iohk.atala.prism.apollo.utils.KMMRSAPublicKey

interface RSAVerifier {
    /**
     * Verifies the passed-in signature.
     *
     * @param publicKey public key in PKCS#8 pem format
     * @param data data that we need to sign
     * @param signedData data that has already been signed
     * @return boolean value representing if the verifying was correct or not
     */
    suspend fun verify(publicKey: KMMRSAPublicKey, data: ByteArray, signedData: ByteArray, type: RSASignatureType): Boolean
}
