package io.iohk.atala.prism.apollo.ecdsa

import io.iohk.atala.prism.apollo.hashing.internal.toHexString
import io.iohk.atala.prism.apollo.utils.ECConfig
import io.iohk.atala.prism.apollo.utils.KMMECPrivateKey
import io.iohk.atala.prism.apollo.utils.KMMECPublicKey
import io.iohk.atala.prism.apollo.utils.decodeHex
import io.iohk.atala.prism.apollo.utils.external.ec

actual object KMMECDSA {
    actual fun sign(
        type: ECDSAType,
        data: ByteArray,
        privateKey: KMMECPrivateKey
    ): ByteArray {
        when (type) {
            ECDSAType.ECDSA_SHA256 -> {}
            ECDSAType.ECDSA_SHA384, ECDSAType.ECDSA_SHA512 -> {
                throw NotImplementedError("Only ECDSA with SHA256 is supported")
            }
        }
        val byteList = privateKey.nativeValue.toArray().map { it.toByte() }
        val padding = ByteArray(ECConfig.PRIVATE_KEY_BYTE_SIZE - byteList.size) { 0 }
        val privateKeyBytes = (padding + byteList).toHexString()

        val ecjs = ec("secp256k1")
        val signature = ecjs.sign(data.toHexString(), privateKeyBytes, enc = "hex")
        val value = signature.toDER(enc = "hex").unsafeCast<String>()
        return value.decodeHex()
    }

    actual fun verify(
        type: ECDSAType,
        data: ByteArray,
        publicKey: KMMECPublicKey,
        signature: ByteArray
    ): Boolean {
        when (type) {
            ECDSAType.ECDSA_SHA256 -> {}
            ECDSAType.ECDSA_SHA384, ECDSAType.ECDSA_SHA512 -> {
                throw NotImplementedError("Only ECDSA with SHA256 is supported")
            }
        }
        val ecjs = ec("secp256k1")
        return ecjs.verify(data.toHexString(), signature.toHexString(), publicKey.getEncoded().toHexString(), enc = "hex")
    }
}
