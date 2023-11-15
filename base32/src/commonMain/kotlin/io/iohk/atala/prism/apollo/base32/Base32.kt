package io.iohk.atala.prism.apollo.base32

import com.ionspin.kotlin.bignum.integer.BigInteger
import com.ionspin.kotlin.bignum.integer.Sign

/**
 * Base32 implementation
 */
internal final object Base32 {
    private val base: BigInteger = BigInteger.parseString("32")

    /**
     * Encode string to Base32
     */
    fun encode(input: ByteArray, encoding: Encoding = Encoding.Standard, paddingEnabled: Boolean = true): String {
        if (input.contentEquals("".encodeToByteArray())) {
            return ""
        }
        val output = StringBuilder()
        var buffer = 0
        var bits = 0

        for (byte in input) {
            buffer = (buffer shl 8) or (byte.toInt() and 0xFF)
            bits += 8

            while (bits >= 5) {
                val index = (buffer shr (bits - 5)) and 0x1F
                output.append(encoding.alphabet[index])
                bits -= 5
            }
        }

        if (bits > 0) {
            buffer = (buffer shl (5 - bits))
            val index = (buffer and 0x1F)
            output.append(encoding.alphabet[index])
        }

        val padding = (8 - output.length % 8) % 8
        repeat(padding) {
            output.append('=')
        }

        return output.toString()
    }

    /**
     * Decode string to Base32
     */
    fun decode(input: String, encoding: Encoding = Encoding.Standard): ByteArray {
        val bytes = decodeToBigInteger(encoding.alphabet, base, input).toByteArray().dropLastWhile {
            it.toInt() == 0
        }.toByteArray()

        // Determine if the first byte is a sign byte (0x00) and needs to be stripped
        val stripSignByte = bytes.size > 1 && bytes[0].compareTo(0) == 0 && bytes[1] < 0

        // Count the number of leading zeros in the input string
        var leadingZeros = 0
        var i = 0
        while (input[i] == encoding.alphabet[0]) {
            leadingZeros++
            i++
        }

        // Allocate a temporary byte array with the correct size, accounting for the sign byte and leading zeros
        val tmp = ByteArray(bytes.size - (if (stripSignByte) 1 else 0) + leadingZeros)

        // Copy the decoded bytes into the temporary array, skipping the sign byte if necessary
        bytes.copyInto(
            tmp, // destination array
            0, // destination offset
            if (stripSignByte) 1 else 0, // source offset
            tmp.size - leadingZeros // number of bytes to copy
        )

        return tmp
    }

    private fun decodeToBigInteger(alphabet: String, base: BigInteger, input: String): BigInteger {
        var bi = BigInteger(0)
        for (i in input.length - 1 downTo 0) {
            val alphaIndex = alphabet.indexOf(input[i])
            if (alphaIndex == -1 && input[i] != '=') {
                throw IllegalStateException("Illegal character " + input[i] + " at " + i)
            }
            if (input[i] != '=') {
                bi = bi.add(BigInteger(alphaIndex.toLong()).multiply(base.pow(input.length - 1 - i)))
            }
        }
        return bi
    }
}
