package io.iohk.atala.prism.apollo.rsa

import cocoapods.IOHKRSA.RSASignatureMessageType
import cocoapods.IOHKRSA.RSASignatureMessageTypeRsaPSSSHA256
import cocoapods.IOHKRSA.RSASignatureMessageTypeRsaPSSSHA384
import cocoapods.IOHKRSA.RSASignatureMessageTypeRsaPSSSHA512
import cocoapods.IOHKRSA.RSASignatureMessageTypeRsaSHA256
import cocoapods.IOHKRSA.RSASignatureMessageTypeRsaSHA384
import cocoapods.IOHKRSA.RSASignatureMessageTypeRsaSHA512
import io.iohk.atala.prism.apollo.utils.NativeTypeInterface

actual typealias RSASignatureTypeNativeType = Long

actual final enum class RSASignatureType : NativeTypeInterface<RSASignatureTypeNativeType> {
    RSASHA256,
    RSASHA384,
    RSASHA512,
    RSAPSSSHA256,
    RSAPSSSHA384,
    RSAPSSSHA512;

    override fun nativeValue(): RSASignatureMessageType {
        return when (this) {
            RSASHA256 -> RSASignatureMessageTypeRsaSHA256
            RSASHA384 -> RSASignatureMessageTypeRsaSHA384
            RSASHA512 -> RSASignatureMessageTypeRsaSHA512
            RSAPSSSHA256 -> RSASignatureMessageTypeRsaPSSSHA256
            RSAPSSSHA384 -> RSASignatureMessageTypeRsaPSSSHA384
            RSAPSSSHA512 -> RSASignatureMessageTypeRsaPSSSHA512
        }
    }
}
