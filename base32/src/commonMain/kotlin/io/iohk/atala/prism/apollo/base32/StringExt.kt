package io.iohk.atala.prism.apollo.base32

// Standard
/**
 * Encode a [String] to Base32 [String]
 */
val String.base32Encoded: String
    get() = Base32.encode(this.encodeToByteArray(), paddingEnabled = false)

/**
 * Decode a Base32 [String] to [ByteArray].
 */
val String.base32DecodedBytes: ByteArray
    get() = Base32.decode(this)

/**
 * Decode a Base32 [String] to [String].
 */
val String.base32Decoded: String
    get() = this.base32DecodedBytes.decodeToString()

// Standard With Padding
/**
 * Encode a [String] to Base32 [String]
 */
val String.base32PadEncoded: String
    get() = Base32.encode(this.encodeToByteArray(), Encoding.Standard)

/**
 * Decode a Base32 [String] to [ByteArray].
 */
val String.base32PadDecodedBytes: ByteArray
    get() = Base32.decode(this, Encoding.Standard)

/**
 * Decode a Base32 [String] to [String].
 */
val String.base32PadDecoded: String
    get() = this.base32PadDecodedBytes.decodeToString()

// Upper
/**
 * Encode a [String] to Base32 [String]
 */
val String.base32UpperEncoded: String
    get() = Base32.encode(this.encodeToByteArray(), Encoding.Upper, paddingEnabled = false)

/**
 * Decode a Base32 [String] to [ByteArray].
 */
val String.base32UpperDecodedBytes: ByteArray
    get() = Base32.decode(this, Encoding.Upper)

/**
 * Decode a Base32 [String] to [String].
 */
val String.base32UpperDecoded: String
    get() = this.base32UpperDecodedBytes.decodeToString()

// Upper With Padding
/**
 * Encode a [String] to Base32 [String]
 */
val String.base32UpperPadEncoded: String
    get() = Base32.encode(this.encodeToByteArray(), Encoding.Upper)

/**
 * Decode a Base32 [String] to [ByteArray].
 */
val String.base32UpperPadDecodedBytes: ByteArray
    get() = Base32.decode(this, Encoding.Upper)

/**
 * Decode a Base32 [String] to [String].
 */
val String.base32UpperPadDecoded: String
    get() = this.base32UpperPadDecodedBytes.decodeToString()

// Hex
/**
 * Encode a [String] to Base32 [String]
 */
val String.base32HexEncoded: String
    get() = Base32.encode(this.encodeToByteArray(), Encoding.Hex, paddingEnabled = false)

/**
 * Decode a Base32 [String] to [ByteArray].
 */
val String.base32HexDecodedBytes: ByteArray
    get() = Base32.decode(this, Encoding.Hex)

/**
 * Decode a Base32 [String] to [String].
 */
val String.base32HexDecoded: String
    get() = this.base32HexDecodedBytes.decodeToString()

// Hex with padding
/**
 * Encode a [String] to Base32 [String]
 */
val String.base32HexPadEncoded: String
    get() = Base32.encode(this.encodeToByteArray(), Encoding.Hex)

/**
 * Decode a Base32 [String] to [ByteArray].
 */
val String.base32HexPadDecodedBytes: ByteArray
    get() = Base32.decode(this, Encoding.Hex)

/**
 * Decode a Base32 [String] to [String].
 */
val String.base32HexPadDecoded: String
    get() = this.base32HexPadDecodedBytes.decodeToString()

// Hex Upper
/**
 * Encode a [String] to Base32 [String]
 */
val String.base32HexUpperEncoded: String
    get() = Base32.encode(this.encodeToByteArray(), Encoding.HexUpper, paddingEnabled = false)

/**
 * Decode a Base32 [String] to [ByteArray].
 */
val String.base32HexUpperDecodedBytes: ByteArray
    get() = Base32.decode(this, Encoding.HexUpper)

/**
 * Decode a Base32 [String] to [String].
 */
val String.base32HexUpperDecoded: String
    get() = this.base32HexUpperDecodedBytes.decodeToString()

// Hex Upper with padding
/**
 * Encode a [String] to Base32 [String]
 */
val String.base32HexUpperPadEncoded: String
    get() = Base32.encode(this.encodeToByteArray(), Encoding.HexUpper)

/**
 * Decode a Base32 [String] to [ByteArray].
 */
val String.base32HexUpperPadDecodedBytes: ByteArray
    get() = Base32.decode(this, Encoding.HexUpper)

/**
 * Decode a Base32 [String] to [String].
 */
val String.base32HexUpperPadDecoded: String
    get() = this.base32HexUpperPadDecodedBytes.decodeToString()
