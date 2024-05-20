package org.hyperledger.identus.apollo.base64

/**
 * Base64 encoding scheme
 */
sealed interface Encoding {
    val alphabet: String

    /**
     * Base64 Standard
     */
    object Standard : Encoding {
        override val alphabet: String = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"
    }

    /**
     * Base64 Standard
     */
    object StandardPad : Encoding {
        override val alphabet: String = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/="
    }

    /**
     * Base64 URL
     */
    object UrlSafe : Encoding {
        override val alphabet: String = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_"
    }

    /**
     * Base64 URL
     */
    object UrlSafePad : Encoding {
        override val alphabet: String = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_="
    }
}
