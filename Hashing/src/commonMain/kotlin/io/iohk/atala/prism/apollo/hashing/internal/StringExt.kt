package io.iohk.atala.prism.apollo.hashing.internal

fun String.toBinary(): ByteArray {
    val blen = this.length / 2
    val buf = ByteArray(blen)
    for (i in 0 until blen) {
        val bs = this.substring(i * 2, i * 2 + 2)
        buf[i] = bs.toInt(16).toByte()
    }
    return buf
}
