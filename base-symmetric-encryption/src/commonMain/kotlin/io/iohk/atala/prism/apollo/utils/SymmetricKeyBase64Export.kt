package io.iohk.atala.prism.apollo.utils

public interface SymmetricKeyBase64Export {
    /**
     * export [KMMSymmetricKey] to Base64 standard with padding
     */
    fun exportToBase64(): String
}
