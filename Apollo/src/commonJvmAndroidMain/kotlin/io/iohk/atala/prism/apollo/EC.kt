package io.iohk.atala.prism.apollo

import com.ionspin.kotlin.bignum.integer.BigInteger
import io.iohk.atala.prism.apollo.ECConfig.CURVE_NAME
import io.iohk.atala.prism.apollo.keys.ECKeyPair
import io.iohk.atala.prism.apollo.keys.ECPrivateKey
import io.iohk.atala.prism.apollo.keys.ECPublicKey
import io.iohk.atala.prism.apollo.keys.ECPublicKeyInitializationException
import io.iohk.atala.prism.apollo.signature.ECSignature
import io.iohk.atala.prism.apollo.util.toJavaBigInteger
import io.iohk.atala.prism.apollo.util.toKotlinBigInteger
import java.security.*
import java.security.spec.*

public actual object EC : ECAbstract() {
    private val signatureAlgorithm = "SHA256withECDSA"
    private val provider = GenericJavaCryptography.provider
    private val ecNamedCurveSpec = GenericJavaCryptography.ecNamedCurveSpec
    private val keyFactory = KeyFactory.getInstance("EC", provider)

    init {
        Security.addProvider(provider)
    }

    override fun generateKeyPair(): ECKeyPair {
        val keyGen = KeyPairGenerator.getInstance("ECDSA", provider)
        val ecSpec = ECGenParameterSpec(CURVE_NAME)
        keyGen.initialize(ecSpec, SecureRandom())
        val keyPair = keyGen.generateKeyPair()
        return ECKeyPair(ECPublicKey(keyPair.public), ECPrivateKey(keyPair.private))
    }

    override fun toPrivateKeyFromBigInteger(d: BigInteger): ECPrivateKey {
        val spec = ECPrivateKeySpec(d.toJavaBigInteger(), ecNamedCurveSpec)
        return ECPrivateKey(keyFactory.generatePrivate(spec))
    }

    override fun toPublicKeyFromCompressed(compressed: ByteArray): ECPublicKey {
        require(compressed.size == ECConfig.PUBLIC_KEY_COMPRESSED_BYTE_SIZE) {
            "Compressed byte array's expected length is ${ECConfig.PUBLIC_KEY_COMPRESSED_BYTE_SIZE}, but got ${compressed.size}"
        }

        val point = GenericJavaCryptography.decodePoint(compressed)
        return toPublicKeyFromBigIntegerCoordinates(point.affineX.toKotlinBigInteger(), point.affineY.toKotlinBigInteger())
    }

    override fun toPublicKeyFromBigIntegerCoordinates(x: BigInteger, y: BigInteger): ECPublicKey {
        val ecPoint = ECPoint(x.toJavaBigInteger(), y.toJavaBigInteger())
        if (!EC.isSecp256k1(io.iohk.atala.prism.apollo.keys.ECPoint(x, y))) { // This is also checked by the keyFactory.generatePublic method nad ECPublicKey.init
            throw ECPublicKeyInitializationException("ECPoint corresponding to a public key doesn't belong to Secp256k1 curve")
        }
        val spec = ECPublicKeySpec(ecPoint, ecNamedCurveSpec)
        return ECPublicKey(keyFactory.generatePublic(spec))
    }

    override fun toPublicKeyFromPrivateKey(privateKey: ECPrivateKey): ECPublicKey {
        val pubSpec = GenericJavaCryptography.keySpec(privateKey.getD().toJavaBigInteger())
        return ECPublicKey(keyFactory.generatePublic(pubSpec))
    }

    override fun signBytes(data: ByteArray, privateKey: ECPrivateKey): ECSignature {
        val signer = Signature.getInstance(signatureAlgorithm, provider)
        signer.initSign(privateKey.key)
        signer.update(data)
        return ECSignature(signer.sign())
    }

    override fun verifyBytes(data: ByteArray, publicKey: ECPublicKey, signature: ECSignature): Boolean {
        val verifier = Signature.getInstance(signatureAlgorithm, provider)
        verifier.initVerify(publicKey.key)
        verifier.update(data)
        return verifier.verify(signature.data)
    }
}
