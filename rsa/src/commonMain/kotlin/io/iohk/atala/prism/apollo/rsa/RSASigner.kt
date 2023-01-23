package io.iohk.atala.prism.apollo.rsa

import io.iohk.atala.prism.apollo.utils.KMMPrivateKey

interface RSASigner {
    /**
     * Returns the signature bytes of all the data updated. The format of the signature depends on the underlying
     * signature scheme.
     *
     * @param privateKey private key in PKCS#8 pem format
     * @param data the data to sign
     * @return the signature bytes of the signing operation's result.
     */
    suspend fun sign(privateKey: KMMPrivateKey, data: ByteArray, type: RSASignatureType): ByteArray
}
