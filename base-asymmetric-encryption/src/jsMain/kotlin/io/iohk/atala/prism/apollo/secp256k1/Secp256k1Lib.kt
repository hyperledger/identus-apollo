package io.iohk.atala.prism.apollo.secp256k1

import com.ionspin.kotlin.bignum.integer.BigInteger
import io.iohk.atala.prism.apollo.hashing.SHA256
import io.iohk.atala.prism.apollo.hashing.internal.toHexString
import io.iohk.atala.prism.apollo.utils.ECConfig
import io.iohk.atala.prism.apollo.utils.asByteArray
import io.iohk.atala.prism.apollo.utils.asUint8Array
import io.iohk.atala.prism.apollo.utils.decodeHex
import io.iohk.atala.prism.apollo.utils.external.BN
import io.iohk.atala.prism.apollo.utils.external.ec
import io.iohk.atala.prism.apollo.utils.external.secp256k1.getPublicKey

actual class Secp256k1Lib actual constructor() {
    actual fun createPublicKey(privateKey: ByteArray, compressed: Boolean): ByteArray {
        return getPublicKey(privateKey.asUint8Array(), isCompressed = compressed).asByteArray()
    }

    actual fun derivePrivateKey(privateKeyBytes: ByteArray, derivedPrivateKeyBytes: ByteArray): ByteArray? {
        val privKeyString = privateKeyBytes.toHexString()
        val derivedPrivKeyString = derivedPrivateKeyBytes.toHexString()
        val privKey = BigInteger.parseString(privKeyString, 16)
        val derivedPrivKey = BigInteger.parseString(derivedPrivKeyString, 16)

        val added = (privKey + derivedPrivKey) % ECConfig.n

        return added.toByteArray()
    }

    actual fun sign(privateKey: ByteArray, data: ByteArray): ByteArray {
        // TODO: "Using noble/secp256k1 would be problematic since it requires a configuration so it can use Sync methods to sign, also it doesnt have DER signatures")
        val sha = SHA256().digest(data)
        val ecInstance = ec("secp256k1")
        val key = ecInstance.keyFromPrivate(privateKey.asUint8Array())
        val signature = key.sign(sha.asUint8Array())
        val derSignature = signature.toDER(enc = "hex").unsafeCast<String>()
        return derSignature.decodeHex()
    }

    actual fun verify(
        publicKey: ByteArray,
        signature: ByteArray,
        data: ByteArray
    ): Boolean {
        val ecjs = ec("secp256k1")
        val sha = SHA256().digest(data)
        return ecjs.verify(sha.toHexString(), signature.toHexString(), publicKey.toHexString(), enc = "hex")
    }

    @OptIn(ExperimentalUnsignedTypes::class)
    actual fun uncompressPublicKey(compressed: ByteArray): ByteArray {
        val ecjs = ec("secp256k1")

        val decoded = ecjs.curve.decodePoint(compressed.asUint8Array())

        val x = ByteArray(decoded.getX().toArray().size) { index -> decoded.getX().toArray()[index].asDynamic() as Byte }
        val y = ByteArray(decoded.getX().toArray().size) { index -> decoded.getY().toArray()[index].asDynamic() as Byte }

        val header: Byte = 0x04
        return byteArrayOf(header) + x + y
    }

    actual fun compressPublicKey(uncompressed: ByteArray): ByteArray {
        val ecjs = ec("secp256k1")
        val pubKeyBN = BN(ecjs.keyFromPublic(uncompressed.asUint8Array()).getPublic().encodeCompressed())
        return ByteArray(pubKeyBN.toArray().size) { index -> pubKeyBN.toArray()[index].asDynamic() as Byte }
    }
}
