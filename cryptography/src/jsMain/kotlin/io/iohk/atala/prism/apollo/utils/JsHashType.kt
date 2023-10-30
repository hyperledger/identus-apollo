package io.iohk.atala.prism.apollo.utils

enum class JsHashType : NativeTypeInterface<String> {
    SHA256,
    SHA384,
    SHA512;

    override fun nativeValue(): String {
        return when (this) {
            SHA256 -> "SHA-256"
            SHA384 -> "SHA-384"
            SHA512 -> "SHA-512"
        }
    }
}
