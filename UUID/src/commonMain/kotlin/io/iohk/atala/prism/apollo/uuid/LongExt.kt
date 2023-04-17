package io.iohk.atala.prism.apollo.uuid

@Throws(NumberFormatException::class)
fun Long.Companion.parseLong(s: String, beginIndex: Int, endIndex: Int, radix: Int): Long {
    if ((beginIndex < 0) || beginIndex > endIndex || endIndex > s.length) {
        throw IndexOutOfBoundsException()
    }
    if (radix < Char.MIN_RADIX) {
        throw NumberFormatException("radix $radix less than Character.MIN_RADIX")
    }
    if (radix > Char.MAX_RADIX) {
        throw NumberFormatException("radix $radix greater than Character.MAX_RADIX")
    }
    var negative = false
    var i = beginIndex
    var limit = -Long.MAX_VALUE
    if (i < endIndex) {
        val firstChar = s[i]
        if (firstChar < '0') { // Possible leading "+" or "-"
            if (firstChar == '-') {
                negative = true
                limit = Long.MIN_VALUE
            } else if (firstChar != '+') {
                throw NumberFormatException().forCharSequence(s, beginIndex, endIndex, i)
            }
            i++
        }
        if (i >= endIndex) { // Cannot have lone "+", "-" or ""
            throw NumberFormatException().forCharSequence(
                s, beginIndex, endIndex, i
            )
        }
        val multmin = limit / radix
        var result: Long = 0
        while (i < endIndex) {
            // Accumulating negatively avoids surprises near MAX_VALUE
            val digit = s[i].digitToIntOrNull(radix) ?: -1
            if (digit < 0 || result < multmin) {
                throw NumberFormatException().forCharSequence(s, beginIndex, endIndex, i)
            }
            result *= radix.toLong()
            if (result < limit + digit) {
                throw NumberFormatException().forCharSequence(s, beginIndex, endIndex, i)
            }
            i++
            result -= digit.toLong()
        }
        return if (negative) result else -result
    } else {
        throw NumberFormatException("")
    }
}

fun Long.toHexString(): String {
    return this.toUInt().toString(16)
}
