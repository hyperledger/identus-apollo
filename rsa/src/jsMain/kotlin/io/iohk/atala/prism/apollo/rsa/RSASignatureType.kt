package io.iohk.atala.prism.apollo.rsa

import io.iohk.atala.prism.apollo.utils.NativeTypeInterface

actual typealias RSASignatureTypeNativeType = String

actual final enum class RSASignatureType : NativeTypeInterface<RSASignatureTypeNativeType> {
    RSASHA256,
    RSASHA384,
    RSASHA512,
    RSAPSSSHA256,
    RSAPSSSHA384,
    RSAPSSSHA512;

    override fun nativeValue(): RSASignatureTypeNativeType {
        return when (this) {
            RSASHA256 -> "RSA-PSS"
            RSASHA384 -> "RSA-PSS"
            RSASHA512 -> "RSA-PSS"
            RSAPSSSHA256 -> "RSASSA-PKCS1-v1_5"
            RSAPSSSHA384 -> "RSASSA-PKCS1-v1_5"
            RSAPSSSHA512 -> "RSASSA-PKCS1-v1_5"
        }
    }
}
