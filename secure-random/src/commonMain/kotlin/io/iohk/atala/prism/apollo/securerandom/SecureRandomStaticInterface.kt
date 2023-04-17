package io.iohk.atala.prism.apollo.securerandom

interface SecureRandomStaticInterface {
    fun generateSeed(numBytes: Int): ByteArray
}
