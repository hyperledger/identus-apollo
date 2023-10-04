package io.iohk.atala.prism.apollo.base32

/**
 * Convert [ByteArray] to [CharArray]
 * @return [CharArray]
 */
fun ByteArray.asCharArray(): CharArray {
    val chars = CharArray(size)
    for (i in chars.indices) {
        chars[i] = get(i).toInt().toChar()
    }
    return chars
}

// Standard

/**
 * Encode a [ByteArray] to Base32 [String] standard
 */
val ByteArray.base32Encoded: String
    get() = Base32.encode(this)

/**
 * Decode a [ByteArray] Base32 standard encoded to [String]
 */
val ByteArray.base32Decoded: String
    get() = asCharArray().concatToString().base32Encoded

// Standard with padding

/**
 * Encode a [ByteArray] to Base32 [String] standard with padding
 */
val ByteArray.base32PadEncoded: String
    get() = Base32.encode(this, Encoding.StandardPad)

/**
 * Decode a [ByteArray] Base32 standard with padding encoded to [String]
 */
val ByteArray.base32PadDecoded: String
    get() = asCharArray().concatToString().base32PadEncoded

// Upper

/**
 * Encode a [ByteArray] to Base32 [String] upper
 */
val ByteArray.base32UpperEncoded: String
    get() = Base32.encode(this, Encoding.Upper)

/**
 * Decode a [ByteArray] Base32 Upper encoded to [String]
 */
val ByteArray.base32UpperDecoded: String
    get() = asCharArray().concatToString().base32UpperEncoded

// Upper with padding

/**
 * Encode a [ByteArray] to Base32 [String] Upper with padding
 */
val ByteArray.base32UpperPadEncoded: String
    get() = Base32.encode(this, Encoding.UpperPad)

/**
 * Decode a [ByteArray] Base32 Upper with padding encoded to [String]
 */
val ByteArray.base32UpperPadDecoded: String
    get() = asCharArray().concatToString().base32UpperPadEncoded

// Hex

/**
 * Encode a [ByteArray] to Base32 [String] hex
 */
val ByteArray.base32HexEncoded: String
    get() = Base32.encode(this, Encoding.Hex)

/**
 * Decode a [ByteArray] Base32 Hex encoded to [String]
 */
val ByteArray.base32HexDecoded: String
    get() = asCharArray().concatToString().base32HexEncoded

// Hex with padding

/**
 * Encode a [ByteArray] to Base32 [String] Hex with padding
 */
val ByteArray.base32HexPadEncoded: String
    get() = Base32.encode(this, Encoding.HexPad)

/**
 * Decode a [ByteArray] Base32 Hex with padding encoded to [String]
 */
val ByteArray.base32HexPadDecoded: String
    get() = asCharArray().concatToString().base32HexPadEncoded

// Hex Upper

/**
 * Encode a [ByteArray] to Base32 [String] Hex Upper
 */
val ByteArray.base32HexUpperEncoded: String
    get() = Base32.encode(this, Encoding.HexUpper)

/**
 * Decode a [ByteArray] Base32 Hex Upper encoded to [String]
 */
val ByteArray.base32HexUpperDecoded: String
    get() = asCharArray().concatToString().base32HexUpperEncoded

// Hex Upper with padding

/**
 * Encode a [ByteArray] to Base32 [String] Hex Upper with padding
 */
val ByteArray.base32HexUpperPadEncoded: String
    get() = Base32.encode(this, Encoding.HexUpperPad)

/**
 * Decode a [ByteArray] Base32 HexUpper with padding encoded to [String]
 */
val ByteArray.base32HexUpperPadDecoded: String
    get() = asCharArray().concatToString().base32HexUpperPadEncoded
