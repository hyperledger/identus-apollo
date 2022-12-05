package io.iohk.atala.prism.apollo.base64

/**
 * Convert [ByteArray] to [CharArray]
 * @return [CharArray]
 */
internal fun ByteArray.asCharArray(): CharArray {
    val chars = CharArray(size)
    for (i in chars.indices) {
        chars[i] = get(i).toInt().toChar()
    }
    return chars
}

// Base64Standard
/**
 * Encode a [ByteArray] to Base64 [String] standard encoding
 * RFC 4648 Section 4
 */
val ByteArray.base64Encoded: String
    get() = asCharArray().concatToString().base64Encoded

/**
 * Decode a [ByteArray] Base64 standard encoded to [String]
 * RFC 4648 Section 4
 */
val ByteArray.base64Decoded: String
    get() = asCharArray().concatToString().base64Decoded

// Base64Standard with padding
/**
 * Encode a [ByteArray] to Base64 [String] standard encoding
 * RFC 4648 Section 4
 */
val ByteArray.base64PadEncoded: String
    get() = asCharArray().concatToString().base64PadEncoded

/**
 * Decode a [ByteArray] Base64 standard encoded to [String]
 * RFC 4648 Section 4
 */
val ByteArray.base64PadDecoded: String
    get() = asCharArray().concatToString().base64PadDecoded

// Base64URL
/**
 * Decode a [ByteArray] Base64 URL-safe encoded to [String].
 * RFC 4648 Section 5
 */
val ByteArray.base64UrlDecoded: String
    get() = asCharArray().concatToString().base64UrlDecoded

/**
 * Encode a [ByteArray] to Base64 URL-safe encoded [String].
 * RFC 4648 Section 5
 */
val ByteArray.base64UrlEncoded: String
    get() = asCharArray().concatToString().base64UrlEncoded

// Base64URL with padding
/**
 * Decode a [ByteArray] Base64 URL-safe encoded to [String].
 * RFC 4648 Section 5
 */
val ByteArray.base64UrlPadDecoded: String
    get() = asCharArray().concatToString().base64UrlPadDecoded

/**
 * Encode a [ByteArray] to Base64 URL-safe encoded [String].
 * RFC 4648 Section 5
 */
val ByteArray.base64UrlPadEncoded: String
    get() = asCharArray().concatToString().base64UrlPadEncoded
