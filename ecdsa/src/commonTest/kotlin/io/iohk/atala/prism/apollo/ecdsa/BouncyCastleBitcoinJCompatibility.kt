package io.iohk.atala.prism.apollo.ecdsa

import io.iohk.atala.prism.apollo.utils.KMMECSecp256k1KeyPair
import io.iohk.atala.prism.apollo.utils.KMMECSecp256k1PublicKey
import org.bitcoinj.core.ECKey
import org.bitcoinj.core.Sha256Hash
import org.bitcoinj.crypto.TransactionSignature
import kotlin.test.Test
import kotlin.test.assertTrue

class BouncyCastleBitcoinJCompatibility {
    @Test
    fun shouldSignBouncyCastleThenVerifyByBitcoinJ() {
        val textToSign = "Hello IOG!"
        val keyPair = KMMECSecp256k1KeyPair.generateSecp256k1KeyPair()

        val signature = KMMECDSA.sign(
            ECDSAType.ECDSA_SHA256,
            textToSign.encodeToByteArray(),
            keyPair.privateKey
        )

        val bouncyCastlePublicKey = keyPair.publicKey
        val publicKey = ECKey.fromPublicOnly(bouncyCastlePublicKey.getEncoded())
        val hash = Sha256Hash.wrap(Sha256Hash.hash(textToSign.toByteArray()))
        val libsecp256k1Signature = TransactionSignature.decodeFromDER(signature)

        assertTrue(publicKey.verify(hash, libsecp256k1Signature))
    }

    @Test
    fun shouldSignBitcoinJThenVerifyByBouncyCastle() {
        val textToSign = "Hello IOG!"
        val ecKey = ECKey().decompress()
        val publicKeyBytes = ecKey.pubKey

        // Sign the message using libsecp256k1 (bitcoinj)
        val hash = Sha256Hash.wrap(Sha256Hash.hash(textToSign.toByteArray()))
        val libsecp256k1Signature = ecKey.sign(hash)
        val signatureBytes = libsecp256k1Signature.encodeToDER()

        val pk = KMMECSecp256k1PublicKey.secp256k1FromBytes(publicKeyBytes)

        // Verify the signature using BouncyCastle (KMMECDSA)
        val isSignatureValid = KMMECDSA.verify(
            ECDSAType.ECDSA_SHA256,
            textToSign.encodeToByteArray(),
            pk,
            signatureBytes
        )

        assertTrue(isSignatureValid)
    }
}
