package io.iohk.atala.prism.apollo.ecdsa

import io.iohk.atala.prism.apollo.hashing.SHA256
import io.iohk.atala.prism.apollo.hashing.SHA384
import io.iohk.atala.prism.apollo.hashing.SHA512
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
        val hashedData = when (type) {
            ECDSAType.ECDSA_SHA256 -> SHA256().digest(data)
            ECDSAType.ECDSA_SHA384 -> SHA384().digest(data)
            ECDSAType.ECDSA_SHA512 -> SHA512().digest(data)
        }.toHexString()

        val byteList = privateKey.nativeValue.toArray().map { it.toByte() }
        val padding = ByteArray(ECConfig.PRIVATE_KEY_BYTE_SIZE - byteList.size) { 0 }
        val privateKeyBytes = (padding + byteList).toHexString()

        val ecjs = ec("secp256k1")
        val signature = ecjs.sign(hashedData, privateKeyBytes, enc = "hex")
        val value = signature.toDER(enc = "hex").unsafeCast<String>()
        return value.decodeHex()
    }

    actual fun verify(
        type: ECDSAType,
        data: ByteArray,
        publicKey: KMMECPublicKey,
        signature: ByteArray
    ): Boolean {
        val hashedData = when (type) {
            ECDSAType.ECDSA_SHA256 -> SHA256().digest(data)
            ECDSAType.ECDSA_SHA384 -> SHA384().digest(data)
            ECDSAType.ECDSA_SHA512 -> SHA512().digest(data)
        }.toHexString()

        val ecjs = ec("secp256k1")
        return ecjs.verify(hashedData, signature.toHexString(), publicKey.getEncoded().toHexString())
    }
}
