package io.iohk.atala.prism.apollo.uuid

fun NumberFormatException.forCharSequence(
    s: CharSequence,
    beginIndex: Int,
    endIndex: Int,
    errorIndex: Int
): NumberFormatException {
    return NumberFormatException(
        "Error at index " +
            (errorIndex - beginIndex) + " in: \"" +
            s.subSequence(beginIndex, endIndex) + "\""
    )
}
