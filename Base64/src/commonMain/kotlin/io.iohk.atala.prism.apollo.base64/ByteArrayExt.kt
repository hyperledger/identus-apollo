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
    get() = Base64.encodeToString(this)

/**
 * Decode a [ByteArray] Base64 standard encoded to [String]
 * RFC 4648 Section 4
 */
val ByteArray.base64Decoded: String
    get() = Base64.decode(this.decodeToString()).decodeToString()

// Base64Standard with padding
/**
 * Encode a [ByteArray] to Base64 [String] standard encoding
 * RFC 4648 Section 4
 */
val ByteArray.base64PadEncoded: String
    get() = Base64.encodeToString(this, Encoding.StandardPad)

/**
 * Decode a [ByteArray] Base64 standard encoded to [String]
 * RFC 4648 Section 4
 */
val ByteArray.base64PadDecoded: String
    get() = Base64.decode(this.decodeToString(), Encoding.StandardPad).decodeToString()

// Base64URL
/**
 * Decode a [ByteArray] Base64 URL-safe encoded to [String].
 * RFC 4648 Section 5
 */
val ByteArray.base64UrlDecoded: String
    get() = Base64.decode(this.decodeToString(), Encoding.UrlSafe).decodeToString()

/**
 * Encode a [ByteArray] to Base64 URL-safe encoded [String].
 * RFC 4648 Section 5
 */
val ByteArray.base64UrlEncoded: String
    get() = Base64.encodeToString(this, Encoding.UrlSafe)

// Base64URL with padding
/**
 * Decode a [ByteArray] Base64 URL-safe encoded to [String].
 * RFC 4648 Section 5
 */
val ByteArray.base64UrlPadDecoded: String
    get() = Base64.encodeToString(this, Encoding.UrlSafePad)

/**
 * Encode a [ByteArray] to Base64 URL-safe encoded [String].
 * RFC 4648 Section 5
 */
val ByteArray.base64UrlPadEncoded: String
    get() = Base64.encodeToString(this, Encoding.UrlSafePad)
