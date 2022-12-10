package io.iohk.atala.prism.apollo.util

// @Suppress("NO_ACTUAL_FOR_EXPECT")
public expect object Random {
    public fun bytesOfLength(l: Int): ByteArray
}
