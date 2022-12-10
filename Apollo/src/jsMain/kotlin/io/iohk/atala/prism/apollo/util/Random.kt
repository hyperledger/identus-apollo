package io.iohk.atala.prism.apollo.util

public actual object Random {
    public actual fun bytesOfLength(l: Int): ByteArray {
        return com.soywiz.krypto.SecureRandom.nextBytes(l)
    }
}
