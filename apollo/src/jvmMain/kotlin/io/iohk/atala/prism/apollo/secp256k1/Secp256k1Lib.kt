package io.iohk.atala.prism.apollo.secp256k1

import fr.acinq.secp256k1.Secp256k1
import org.kotlincrypto.hash.sha2.SHA256
import java.math.BigInteger

/**
 * This class provides various Secp256k1 cryptographic functionalities such as creating public keys, signing data,
 * verifying signatures, and compressing or decompressing public keys.
 */
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

    /**
     * Derives a new private key from an existing private key and derived bytes.
     *
     * @param privateKeyBytes The original private key in byte array format.
     * @param derivedPrivateKeyBytes The byte array used for deriving the new private key.
     * @return A byte array representing the derived private key, or null if derivation fails.
     */
    actual fun derivePrivateKey(
        privateKeyBytes: ByteArray,
        derivedPrivateKeyBytes: ByteArray
    ): ByteArray? {
        return Secp256k1.privKeyTweakAdd(privateKeyBytes, derivedPrivateKeyBytes)
    }

    /**
     * Signs data using a given private key.
     *
     * @param privateKey The private key used for signing, in byte array format.
     * @param data The data to be signed, in byte array format.
     * @return A byte array representing the signature.
     */
    actual fun sign(privateKey: ByteArray, data: ByteArray): ByteArray {
        val sha = SHA256().digest(data)
        val compactSignature = Secp256k1.sign(sha, privateKey)
        return Secp256k1.compact2der(compactSignature)
    }

    /**
     * Verifies a signature against a public key and data.
     *
     * @param publicKey The public key in byte array format.
     * @param signature The signature to be verified, in byte array format.
     * @param data The data against which the signature will be verified, in byte array format.
     * @return A boolean indicating whether the signature is valid.
     */
    actual fun verify(
        publicKey: ByteArray,
        signature: ByteArray,
        data: ByteArray
    ): Boolean {
        val sha = SHA256().digest(data)
        if (Secp256k1.verify(signature, sha, publicKey)) {
            return true
        }
        val normalisedSignature = Secp256k1.signatureNormalize(signature).first
        val derSignature = transcodeSignatureToDERBitcoin(normalisedSignature)
        return Secp256k1.verify(derSignature, sha, publicKey)
    }

    private fun reverseB32(inputBytes: ByteArray): ByteArray {
        val reversedBytes = ByteArray(inputBytes.size)
        for (i in inputBytes.indices) {
            reversedBytes[inputBytes.size - i - 1] = inputBytes[i]
        }
        return reversedBytes
    }

    private fun transcodeSignatureToDERBitcoin(signature: ByteArray): ByteArray {
        val rawLen = signature.size / 2
        val bigR = BigInteger(1, signature.copyOfRange(0, rawLen))
        val bigS = BigInteger(1, signature.copyOfRange(rawLen, signature.size))
        var r = bigR.toByteArray()
        var s = bigS.toByteArray()
        r = reverseB32(r)
        s = reverseB32(s)
        val lenR = r.size
        val lenS = s.size
        val derLength = 6 + lenR + lenS
        val derSignature = ByteArray(derLength)
        derSignature[0] = 0x30
        derSignature[1] = (4 + lenR + lenS).toByte()
        derSignature[2] = 0x02
        derSignature[3] = lenR.toByte()
        System.arraycopy(r, 0, derSignature, 4, lenR)
        derSignature[4 + lenR] = 0x02
        derSignature[5 + lenR] = lenS.toByte()
        System.arraycopy(s, 0, derSignature, 6 + lenR, lenS)
        return derSignature
    }

    /**
     * Decompresses a compressed public key.
     *
     * @param compressed The compressed public key in byte array format.
     * @return A byte array representing the uncompressed public key.
     */
    actual fun uncompressPublicKey(compressed: ByteArray): ByteArray {
        return Secp256k1.pubkeyParse(compressed)
    }

    /**
     * Compresses an uncompressed public key.
     *
     * @param uncompressed The uncompressed public key in byte array format.
     * @return A byte array representing the compressed public key.
     */
    actual fun compressPublicKey(uncompressed: ByteArray): ByteArray {
        return Secp256k1.pubKeyCompress(uncompressed)
    }
}
