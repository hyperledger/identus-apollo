package io.iohk.atala.prism.apollo.secp256k1

import fr.acinq.secp256k1.Secp256k1
import io.iohk.atala.prism.apollo.hashing.SHA256

actual class Secp256k1Lib {
    actual fun createPublicKey(privateKey: ByteArray, compressed: Boolean): ByteArray {
        val pubKey = Secp256k1.pubkeyCreate(privateKey)
        if (Secp256k1Helper.validatePublicKey(pubKey)) {
            if (compressed) {
                return Secp256k1.pubKeyCompress(pubKey)
            }
            return pubKey
        } else {
            throw Secp256k1Exception("invalid public key")
        }
    }

    actual fun derivePrivateKey(
        privateKeyBytes: ByteArray,
        derivedPrivateKeyBytes: ByteArray
    ): ByteArray? {
        return Secp256k1.privKeyTweakAdd(privateKeyBytes, derivedPrivateKeyBytes)
    }

    actual fun sign(privateKey: ByteArray, data: ByteArray): ByteArray {
        val sha = SHA256().digest(data)
        val compactSignature = Secp256k1.sign(sha, privateKey)
        return Secp256k1.compact2der(compactSignature)
    }

    actual fun verify(
        publicKey: ByteArray,
        signature: ByteArray,
        data: ByteArray
    ): Boolean {
        val sha = SHA256().digest(data)
        return Secp256k1.verify(signature, sha, publicKey)
    }

    actual fun uncompressPublicKey(compressed: ByteArray): ByteArray {
        return Secp256k1.pubkeyParse(compressed)
    }

    actual fun compressPublicKey(uncompressed: ByteArray): ByteArray {
        return Secp256k1.pubKeyCompress(uncompressed)
    }
}
