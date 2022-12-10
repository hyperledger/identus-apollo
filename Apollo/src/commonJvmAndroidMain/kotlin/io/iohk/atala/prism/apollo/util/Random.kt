package io.iohk.atala.prism.apollo.util

import java.security.SecureRandom

public actual object Random {
    public actual fun bytesOfLength(l: Int): ByteArray {
        val arr = ByteArray(l)
        SecureRandom().nextBytes(arr)
        return arr
    }
}
