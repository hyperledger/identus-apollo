package io.iohk.atala.prism.apollo.secp256k1

import fr.acinq.secp256k1.Secp256k1Native
import org.kotlincrypto.hash.sha2.SHA256

actual class Secp256k1Lib {
    actual fun createPublicKey(privateKey: ByteArray, compressed: Boolean): ByteArray {
        val pubKey = Secp256k1Native.pubkeyCreate(privateKey)
        if (Secp256k1Helper.validatePublicKey(pubKey)) {
            if (compressed) {
                return Secp256k1Native.pubKeyCompress(pubKey)
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
        return Secp256k1Native.privKeyTweakAdd(privateKeyBytes, derivedPrivateKeyBytes)
    }

    actual fun sign(privateKey: ByteArray, data: ByteArray): ByteArray {
        val sha = SHA256().digest(data)
        val compactSign = Secp256k1Native.sign(sha, privateKey)
        return Secp256k1Native.compact2der(compactSign)
    }

    actual fun verify(
        publicKey: ByteArray,
        signature: ByteArray,
        data: ByteArray
    ): Boolean {
        val sha = SHA256().digest(data)
        return Secp256k1Native.verify(signature, sha, publicKey)
    }

    actual fun uncompressPublicKey(compressed: ByteArray): ByteArray {
        return Secp256k1Native.pubkeyParse(compressed)
    }

    actual fun compressPublicKey(uncompressed: ByteArray): ByteArray {
        return Secp256k1Native.pubKeyCompress(uncompressed)
    }
}
