package io.iohk.atala.prism.apollo.rsa

import cocoapods.IOHKRSA.IOHKRSA
import io.iohk.atala.prism.apollo.utils.KMMRSAPrivateKey
import io.iohk.atala.prism.apollo.utils.KMMRSAPublicKey
import io.iohk.atala.prism.apollo.utils.toByteArray
import io.iohk.atala.prism.apollo.utils.toNSData

actual final class RSA : RSASigner, RSAVerifier {
    override suspend fun sign(privateKey: KMMRSAPrivateKey, data: ByteArray, type: RSASignatureType): ByteArray {
        return IOHKRSA.signRSAWithKey(
            privateKey.nativeType,
            type.nativeValue(),
            data.toNSData()
        )!!.toByteArray()
    }

    override suspend fun verify(publicKey: KMMRSAPublicKey, data: ByteArray, signedData: ByteArray, type: RSASignatureType): Boolean {
        return IOHKRSA.verifyRSAWithKey(
            publicKey.nativeType,
            type.nativeValue(),
            data.toNSData(),
            signedData.toNSData()
        )
    }
}
