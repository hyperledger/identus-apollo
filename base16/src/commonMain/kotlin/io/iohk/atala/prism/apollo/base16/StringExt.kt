package io.iohk.atala.prism.apollo.base16

/**
 * Encode a [String] to Base16 [String]
 */
val String.base16Encoded: String
    get() = Base16.encode(this.encodeToByteArray())

/**
 * Decode a Base16 [String] to [ByteArray].
 */
val String.base16DecodedBytes: ByteArray
    get() = Base16.decode(this)

/**
 * Decode a Base16 [String] to [String].
 */
val String.base16Decoded: String
    get() = this.base16DecodedBytes.decodeToString()

/**
 * Encode a [String] to Base16 [String] Upper
 */
val String.base16UpperEncoded: String
    get() = Base16.encode(this.encodeToByteArray(), Encoding.Upper)

/**
 * Decode a Base16 [String] Upper to [ByteArray].
 */
val String.base16UpperDecodedBytes: ByteArray
    get() = Base16.decode(this, Encoding.Upper)

/**
 * Decode a Base16 [String] Upper to [String].
 */
val String.base16UpperDecoded: String
    get() = this.base16UpperDecodedBytes.decodeToString()
