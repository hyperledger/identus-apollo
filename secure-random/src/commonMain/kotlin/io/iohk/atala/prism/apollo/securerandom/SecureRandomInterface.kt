package io.iohk.atala.prism.apollo.securerandom

interface SecureRandomInterface {
    fun nextBytes(size: Int): ByteArray
}
