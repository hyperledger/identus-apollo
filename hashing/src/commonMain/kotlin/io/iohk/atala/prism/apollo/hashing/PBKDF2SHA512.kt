package io.iohk.atala.prism.apollo.hashing

expect class PBKDF2SHA512 {
    companion object {
        fun derive(p: String, s: String, c: Int, dkLen: Int): ByteArray
    }
}
