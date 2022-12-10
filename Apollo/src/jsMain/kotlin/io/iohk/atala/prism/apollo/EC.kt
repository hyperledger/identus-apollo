package io.iohk.atala.prism.apollo

import com.ionspin.kotlin.bignum.integer.BigInteger
import io.iohk.atala.prism.apollo.externals.BN
import io.iohk.atala.prism.apollo.externals.Coordinates
import io.iohk.atala.prism.apollo.externals.ec
import io.iohk.atala.prism.apollo.keys.ECKeyPair
import io.iohk.atala.prism.apollo.keys.ECPrivateKey
import io.iohk.atala.prism.apollo.keys.ECPublicKey
import io.iohk.atala.prism.apollo.signature.ECSignature
import io.iohk.atala.prism.apollo.util.BytesOps.bytesToHex
import io.iohk.atala.prism.apollo.util.BytesOps.hexToBytes
import io.iohk.atala.prism.apollo.util.asUint8Array

@JsExport
public actual object EC : ECAbstract() {
    private val ecjs = ec("secp256k1")

    override fun generateKeyPair(): ECKeyPair {
        val keyPair = ecjs.genKeyPair()
        val basePoint = keyPair.getPublic()
        val bigNumber = keyPair.getPrivate()
        return ECKeyPair(ECPublicKey(basePoint), ECPrivateKey(bigNumber))
    }

    override fun toPublicKeyFromCompressed(compressed: ByteArray): ECPublicKey {
        require(compressed.size == ECConfig.PUBLIC_KEY_COMPRESSED_BYTE_SIZE) {
            "Compressed byte array's expected length is ${ECConfig.PUBLIC_KEY_COMPRESSED_BYTE_SIZE}, but got ${compressed.size}"
        }

        val point = ecjs.curve.decodePoint(compressed.asUint8Array())
        val uncompressedEncoding = hexToBytes(point.encode("hex"))
        return toPublicKeyFromBytes(uncompressedEncoding)
    }

    override fun toPrivateKeyFromBigInteger(d: BigInteger): ECPrivateKey {
        return ECPrivateKey(BN(d.toString()))
    }

    override fun toPublicKeyFromBigIntegerCoordinates(
        x: BigInteger,
        y: BigInteger
    ): ECPublicKey {
        val xCoord = x.toByteArray()
        val yCoord = y.toByteArray()
        val keyPair = ecjs.keyFromPublic(
            object : Coordinates {
                override var x = bytesToHex(xCoord)
                override var y = bytesToHex(yCoord)
            }
        )
        return ECPublicKey(keyPair.getPublic())
    }

    override fun toPublicKeyFromPrivateKey(privateKey: ECPrivateKey): ECPublicKey {
        val keyPair = ecjs.keyFromPrivate(privateKey.bigNumber.toString("hex"))
        return ECPublicKey(keyPair.getPublic())
    }

    override fun signBytes(
        data: ByteArray,
        privateKey: ECPrivateKey
    ): ECSignature {
        val digest = Sha256.compute(data).hexValue
        val signature = ecjs.sign(digest, privateKey.getHexEncoded(), enc = "hex")
        return ECSignature(signature.toDER(enc = "hex").unsafeCast<String>())
    }

    override fun verifyBytes(
        data: ByteArray,
        publicKey: ECPublicKey,
        signature: ECSignature
    ): Boolean {
        val hexData = Sha256.compute(data).hexValue
        return ecjs.verify(hexData, signature.sig, publicKey.getHexEncoded(), enc = "hex")
    }
}
