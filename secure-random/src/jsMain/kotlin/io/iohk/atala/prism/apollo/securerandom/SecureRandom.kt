package io.iohk.atala.prism.apollo.securerandom

import io.iohk.atala.prism.apollo.utils.toByteArray
import js.typedarrays.Uint8Array
import web.crypto.crypto

actual class SecureRandom actual constructor(
    actual val seed: ByteArray
) : SecureRandomInterface {

    override fun nextBytes(size: Int): ByteArray {
        val arr = Uint8Array(size)
        return crypto.getRandomValues(arr).buffer.toByteArray()
    }

    actual companion object : SecureRandomStaticInterface {
        override fun generateSeed(numBytes: Int): ByteArray {
            val arr = Uint8Array(numBytes)
            return crypto.getRandomValues(arr).buffer.toByteArray()
        }
    }
}
