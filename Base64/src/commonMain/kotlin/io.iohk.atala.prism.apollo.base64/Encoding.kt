package io.iohk.atala.prism.apollo.base64

/**
 * Base64 encoding scheme
 */
sealed interface Encoding {
    val alphabet: String
    val requiresPadding: Boolean

    /**
     * Base64 Standard
     */
    object Standard : Encoding {
        override val alphabet: String = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"
        override val requiresPadding: Boolean = true
    }

    /**
     * Base64 URL
     */
    object UrlSafe : Encoding {
        override val alphabet: String = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_"
        override val requiresPadding: Boolean = false // Padding is optional
    }
}
