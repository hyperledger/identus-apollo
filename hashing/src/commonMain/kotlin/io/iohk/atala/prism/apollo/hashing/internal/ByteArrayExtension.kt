package io.iohk.atala.prism.apollo.hashing.internal

fun ByteArray.toHexString(): String {
    return joinToString("") { (0xFF and it.toInt()).toString(16).padStart(2, '0') }
}
