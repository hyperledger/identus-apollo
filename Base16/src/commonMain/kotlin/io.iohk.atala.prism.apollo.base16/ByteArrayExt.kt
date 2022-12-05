package io.iohk.atala.prism.apollo.base16

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

/**
 * Encode a [ByteArray] to Base16 [String]
 */
val ByteArray.base16Encoded: String
    get() = Base16.encode(this)

/**
 * Decode a [ByteArray] Base16 standard encoded to [String]
 */
val ByteArray.base16Decoded: String
    get() = asCharArray().concatToString().base16Encoded
