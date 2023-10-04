package io.iohk.atala.prism.apollo.base16

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

/**
 * Encode a [ByteArray] to Base16 [String] standard
 */
val ByteArray.base16Encoded: String
    get() = Base16.encode(this)

/**
 * Decode a [ByteArray] Base16 standard encoded to [String]
 */
val ByteArray.base16Decoded: String
    get() = asCharArray().concatToString().base16Encoded

/**
 * Encode a [ByteArray] to Base16 [String] Upper
 */
val ByteArray.base16UpperEncoded: String
    get() = Base16.encode(this, Encoding.Upper)

/**
 * Decode a [ByteArray] Base16 Upper encoded to [String]
 */
val ByteArray.base16UpperDecoded: String
    get() = asCharArray().concatToString().base16UpperDecoded
