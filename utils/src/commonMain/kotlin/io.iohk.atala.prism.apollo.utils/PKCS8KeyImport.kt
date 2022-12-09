package io.iohk.atala.prism.apollo.utils

public interface PKCS8KeyImport {
    /**
     * Import PKCS8 Private Key from pem
     *
     * @param pem PKCS8 Private Key as pem
     */
    fun getPKCS8PrivateKey(pem: String): KMMPrivateKey

    /**
     * Import PKCS8 Public Key from pem
     *
     * @param pem PKCS8 Public Key as pem
     */
    fun getPKCS8PublicKey(pem: String): KMMPublicKey
}
