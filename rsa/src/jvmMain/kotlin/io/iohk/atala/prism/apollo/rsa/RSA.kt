package io.iohk.atala.prism.apollo.rsa

import io.iohk.atala.prism.apollo.utils.KMMRSAPrivateKey
import io.iohk.atala.prism.apollo.utils.KMMRSAPublicKey
import org.bouncycastle.jce.provider.BouncyCastleProvider
import java.security.Security
import java.security.Signature

actual final class RSA : RSASigner, RSAVerifier {

    init {
        Security.removeProvider("BC")
        Security.addProvider(BouncyCastleProvider())
    }

    override suspend fun sign(privateKey: KMMRSAPrivateKey, data: ByteArray, type: RSASignatureType): ByteArray {
        val signature = Signature.getInstance(type.nativeValue())
        signature.initSign(privateKey.nativeType)
        signature.update(data)
        return signature.sign()
    }

    override suspend fun verify(publicKey: KMMRSAPublicKey, data: ByteArray, signedData: ByteArray, type: RSASignatureType): Boolean {
        val signature = Signature.getInstance(type.nativeValue())
        signature.initVerify(publicKey.nativeType)
        signature.update(data)
        return signature.verify(signedData)
    }
}
