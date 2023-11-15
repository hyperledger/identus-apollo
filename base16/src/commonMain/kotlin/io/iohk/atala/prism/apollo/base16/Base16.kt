package io.iohk.atala.prism.apollo.base16

import com.ionspin.kotlin.bignum.integer.BigInteger
import com.ionspin.kotlin.bignum.integer.Sign

/**
 * Base16 implementation
 */
internal final object Base16 {
    private val base: BigInteger = BigInteger.parseString("16")

    /**
     * Encode string to Base16
     */
    fun encode(input: ByteArray, encoding: Encoding = Encoding.Standard): String {
        if (input.contentEquals("".encodeToByteArray())) {
            return ""
        }
        var bi = BigInteger.fromByteArray(input, Sign.POSITIVE)
        val sb = StringBuilder()
        while (bi >= base) {
            val mod = bi.mod(base)
            sb.insert(0, encoding.alphabet[mod.intValue()])
            bi = bi.subtract(mod).divide(base)
        }
        sb.insert(0, encoding.alphabet[bi.intValue()])
        // convert leading zeros.
        for (b in input) {
            if (b.compareTo(0) == 0) {
                sb.insert(0, encoding.alphabet[0])
            } else {
                break
            }
        }
        return sb.toString()
    }

    /**
     * Decode string to Base16
     */
    fun decode(input: String, encoding: Encoding = Encoding.Standard): ByteArray {
        val bytes = decodeToBigInteger(encoding.alphabet, base, input).toByteArray()
        val stripSignByte = bytes.size > 1 && bytes[0].compareTo(0) == 0 && bytes[1] < 0
        var leadingZeros = 0
        var i = 0
        while (input[i] == encoding.alphabet[0]) {
            leadingZeros++
            i++
        }
        val tmp = ByteArray(bytes.size - (if (stripSignByte) 1 else 0) + leadingZeros)
        bytes.copyInto(
            tmp, // dest
            0, // dest offset
            if (stripSignByte) 1 else 0,
            tmp.size - leadingZeros // can be added -1 not sure
        )
        return tmp
    }

    private fun decodeToBigInteger(alphabet: String, base: BigInteger, input: String): BigInteger {
        var bi = BigInteger(0)
        for (i in input.length - 1 downTo 0) {
            val alphaIndex = alphabet.indexOf(input[i])
            if (alphaIndex == -1) {
                throw IllegalStateException("Illegal character " + input[i] + " at " + i)
            }
            bi = bi.add(BigInteger(alphaIndex.toLong()).multiply(base.pow(input.length - 1 - i)))
        }
        return bi
    }
}
