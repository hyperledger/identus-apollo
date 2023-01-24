package io.iohk.atala.prism.apollo.utils

actual final enum class RSAAsymmetricAlgorithm : NativeTypeInterface<String> {
    RSA,
    RSAPSS; // ktlint-disable no-semi

    override fun nativeValue(): String {
        return when (this) {
            RSA -> "RSA"
            RSAPSS -> "RSASSA-PSS"
        }
    }
}
