package io.iohk.atala.prism.apollo.derivation

import io.iohk.atala.prism.apollo.utils.KMMECKeyPair
import io.iohk.atala.prism.apollo.utils.KMMECPrivateKey
import io.iohk.atala.prism.apollo.utils.KMMECPublicKey
import io.iohk.atala.prism.apollo.utils.toKotlinBigInteger
import org.bitcoinj.crypto.ChildNumber
import org.bitcoinj.crypto.DeterministicKey
import org.bitcoinj.crypto.HDKeyDerivation

actual class ExtendedKey(private val key: DeterministicKey) {
    /**
     * Derivation path used to obtain such key
     */
    actual fun path(): DerivationPath {
        return DerivationPath(key.path.map { axis -> DerivationAxis(axis.i) })
    }

    /**
     * Public key for this extended key
     */
    actual fun publicKey(): KMMECPublicKey {
        val ecPoint = key.pubKeyPoint
        ecPoint.xCoord.toBigInteger()
        return KMMECPublicKey.secp256k1FromBigIntegerCoordinates(
            ecPoint.xCoord.toBigInteger().toKotlinBigInteger(),
            ecPoint.yCoord.toBigInteger().toKotlinBigInteger()
        )
    }

    /**
     * Private key for this extended key
     */
    actual fun privateKey(): KMMECPrivateKey {
        return KMMECPrivateKey.secp256k1FromBigInteger(key.privKey.toKotlinBigInteger())
    }

    /**
     * KeyPair for this extended key
     */
    actual fun keyPair(): KMMECKeyPair {
        return KMMECKeyPair(privateKey(), publicKey())
    }

    /**
     * Generates child extended key for given index
     */
    actual fun derive(axis: DerivationAxis): ExtendedKey {
        return ExtendedKey(HDKeyDerivation.deriveChildKey(key, ChildNumber(axis.i)))
    }
}
