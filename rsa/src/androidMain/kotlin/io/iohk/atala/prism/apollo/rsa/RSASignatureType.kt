package io.iohk.atala.prism.apollo.rsa

import io.iohk.atala.prism.apollo.utils.NativeTypeInterface

actual typealias RSASignatureTypeNativeType = String

actual final enum class RSASignatureType : NativeTypeInterface<RSASignatureTypeNativeType> {
    RSASHA256,
    RSASHA384,
    RSASHA512,
    RSAPSSSHA256,
    RSAPSSSHA384,
    RSAPSSSHA512,;

    override fun nativeValue(): String {
        return when (this) {
            RSASHA256 -> "SHA256withRSA"
            RSASHA384 -> "SHA384withRSA"
            RSASHA512 -> "SHA512withRSA"
            RSAPSSSHA256 -> "SHA256withRSA/PSS"
            RSAPSSSHA384 -> "SHA384withRSA/PSS"
            RSAPSSSHA512 -> "SHA512withRSA/PSS"
        }
    }
}
