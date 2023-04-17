package io.iohk.atala.prism.apollo.base58

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

// BTC
/**
 * Encode a [ByteArray] to Base58 [String] standard
 */
val ByteArray.base58BtcEncoded: String
    get() = Base58.encode(this)

/**
 * Decode a [ByteArray] Base58 BTC encoded to [String]
 */
val ByteArray.base58BtcDecoded: String
    get() = asCharArray().concatToString().base58BtcEncoded

// Flickr
/**
 * Encode a [ByteArray] to Base58 [String] standard
 */
val ByteArray.base58FlickrEncoded: String
    get() = Base58.encode(this, Encoding.Flickr)

/**
 * Decode a [ByteArray] Base58 BTC encoded to [String]
 */
val ByteArray.base58FlickrDecoded: String
    get() = asCharArray().concatToString().base58FlickrEncoded
