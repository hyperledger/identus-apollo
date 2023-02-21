package io.iohk.atala.prism.apollo.ecdsa

import io.iohk.atala.prism.apollo.utils.ECConfig
import io.iohk.atala.prism.apollo.utils.KMMECPrivateKey
import io.iohk.atala.prism.apollo.utils.KMMECPublicKey
import org.bouncycastle.jce.provider.BouncyCastleProvider
import java.security.Signature

actual object KMMECDSA {
    private val provider = BouncyCastleProvider()

    actual fun sign(
        type: ECDSAType,
        data: ByteArray,
        privateKey: KMMECPrivateKey
    ): ByteArray {
        val signatureAlgorithm = when (type) {
            ECDSAType.ECDSA_SHA256 -> "SHA256withECDSA"
            ECDSAType.ECDSA_SHA384 -> "SHA384withECDSA"
            ECDSAType.ECDSA_SHA512 -> "SHA512withECDSA"
        }
        val signer = Signature.getInstance(signatureAlgorithm, provider)
        signer.initSign(privateKey.nativeValue)
        signer.update(data)
        return signer.sign()
    }

    actual fun verify(
        type: ECDSAType,
        data: ByteArray,
        publicKey: KMMECPublicKey,
        signature: ByteArray
    ): Boolean {
        val signatureAlgorithm = when (type) {
            ECDSAType.ECDSA_SHA256 -> "SHA256withECDSA"
            ECDSAType.ECDSA_SHA384 -> "SHA384withECDSA"
            ECDSAType.ECDSA_SHA512 -> "SHA512withECDSA"
        }
        val verifier = Signature.getInstance(signatureAlgorithm, provider)
        verifier.initVerify(publicKey.nativeValue)
        verifier.update(data)
        return verifier.verify(signature)
    }
}
