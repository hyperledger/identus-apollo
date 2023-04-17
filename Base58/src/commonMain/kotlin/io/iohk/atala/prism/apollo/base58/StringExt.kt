package io.iohk.atala.prism.apollo.base58

// BTC
/**
 * Encode a [String] to Base58 [String]
 */
val String.base58BtcEncoded: String
    get() = Base58.encode(this.encodeToByteArray())

/**
 * Decode a Base58 [String] to [ByteArray].
 */
val String.base58BtcDecodedBytes: ByteArray
    get() = Base58.decode(this)

/**
 * Decode a Base58 [String] to [String].
 */
val String.base58BtcDecoded: String
    get() = this.base58BtcDecodedBytes.decodeToString()

// Flickr
/**
 * Encode a [String] to Base58 [String]
 */
val String.base58FlickrEncoded: String
    get() = Base58.encode(this.encodeToByteArray(), Encoding.Flickr)

/**
 * Decode a Base58 [String] to [ByteArray].
 */
val String.base58FlickrDecodedBytes: ByteArray
    get() = Base58.decode(this, Encoding.Flickr)

/**
 * Decode a Base58 [String] to [String].
 */
val String.base58FlickrDecoded: String
    get() = this.base58FlickrDecodedBytes.decodeToString()
