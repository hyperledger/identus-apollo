package io.iohk.atala.prism.apollo.utils

public interface SymmetricKeyBase64Import {
    /**
     * Create an instance of [KMMSymmetricKey] from a Base64 standard with padding encoded key
     *
     * @param base64Encoded a Base64 standard with padding encoded key value
     * @param algorithm algorithm type supported
     */
    fun createKeyFromBase64(base64Encoded: String, algorithm: SymmetricKeyType): KMMSymmetricKey
}
