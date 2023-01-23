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
            RSASHA256 -> TODO()
            RSASHA384 -> TODO()
            RSASHA512 -> TODO()
            RSAPSSSHA256 -> TODO()
            RSAPSSSHA384 -> TODO()
            RSAPSSSHA512 -> TODO()
        }
    }
}
