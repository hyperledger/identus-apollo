package io.iohk.atala.prism.apollo.rsa

import io.iohk.atala.prism.apollo.utils.NativeTypeInterface

expect class RSASignatureTypeNativeType

expect final enum class RSASignatureType : NativeTypeInterface<RSASignatureTypeNativeType> {
    RSASHA256,
    RSASHA384,
    RSASHA512,

    RSAPSSSHA256,
    RSAPSSSHA384,
    RSAPSSSHA512;
}
