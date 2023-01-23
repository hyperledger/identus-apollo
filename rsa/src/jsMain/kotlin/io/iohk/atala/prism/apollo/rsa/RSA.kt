package io.iohk.atala.prism.apollo.rsa

import io.iohk.atala.prism.apollo.utils.KMMPrivateKey
import io.iohk.atala.prism.apollo.utils.KMMPublicKey
import io.iohk.atala.prism.apollo.utils.toArrayBuffer
import io.iohk.atala.prism.apollo.utils.toByteArray
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.await
import kotlinx.coroutines.promise
import web.crypto.RsaPssParams
import web.crypto.crypto

actual final class RSA : RSASigner, RSAVerifier {

    private fun getRsaPssParams(): RsaPssParams {
        val algorithm = "RSA-PSS"
        return js("{name: algorithm, saltLength: 32}") as RsaPssParams
    }

    override suspend fun sign(privateKey: KMMPrivateKey, data: ByteArray, type: RSASignatureType): ByteArray {
        return when (type) {
            RSASignatureType.RSASHA256, RSASignatureType.RSASHA384, RSASignatureType.RSASHA512 -> {
                MainScope().promise {
                    crypto.subtle.sign(
                        privateKey.nativeType.algorithm.name,
                        privateKey.nativeType,
                        data.toArrayBuffer()
                    ).await().toByteArray()
                }.await()
            }
            RSASignatureType.RSAPSSSHA256, RSASignatureType.RSAPSSSHA384, RSASignatureType.RSAPSSSHA512 -> {
                MainScope().promise {
                    crypto.subtle.sign(
                        getRsaPssParams(),
                        privateKey.nativeType,
                        data.toArrayBuffer()
                    ).await().toByteArray()
                }.await()
            }
        }
    }

    override suspend fun verify(publicKey: KMMPublicKey, data: ByteArray, signedData: ByteArray, type: RSASignatureType): Boolean {
        return when (type) {
            RSASignatureType.RSASHA256, RSASignatureType.RSASHA384, RSASignatureType.RSASHA512 -> {
                MainScope().promise {
                    crypto.subtle.verify(
                        publicKey.nativeType.algorithm.name,
                        publicKey.nativeType,
                        signedData.toArrayBuffer(),
                        data.toArrayBuffer()
                    ).await()
                }.await()
            }
            RSASignatureType.RSAPSSSHA256, RSASignatureType.RSAPSSSHA384, RSASignatureType.RSAPSSSHA512 -> {
                MainScope().promise {
                    crypto.subtle.verify(
                        getRsaPssParams(),
                        publicKey.nativeType,
                        signedData.toArrayBuffer(),
                        data.toArrayBuffer()
                    ).await()
                }.await()
            }
        }
    }
}
