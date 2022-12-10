package io.iohk.atala.prism.apollo

import java.security.MessageDigest

public actual object Sha256 {
    @JvmStatic
    public actual fun compute(bytes: ByteArray): Sha256Digest {
        val messageDigest = MessageDigest.getInstance("SHA-256")
        return Sha256Digest(messageDigest.digest(bytes))
    }
}
