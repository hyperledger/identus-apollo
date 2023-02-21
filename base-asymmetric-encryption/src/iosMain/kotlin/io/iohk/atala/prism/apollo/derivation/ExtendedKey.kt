package io.iohk.atala.prism.apollo.derivation

import fr.acinq.bitcoin.DeterministicWallet
import fr.acinq.bitcoin.DeterministicWallet.ExtendedPrivateKey
import io.iohk.atala.prism.apollo.utils.KMMECKeyPair
import io.iohk.atala.prism.apollo.utils.KMMECSecp256k1PrivateKey
import io.iohk.atala.prism.apollo.utils.KMMECSecp256k1PublicKey

actual class ExtendedKey(private val key: ExtendedPrivateKey) {
    /**
     * Derivation path used to obtain such key
     */
    actual fun path(): DerivationPath {
        return DerivationPath(key.path.path.map { DerivationAxis(it.toInt()) })
    }

    /**
     * Public key for this extended key
     */
    actual fun publicKey(): KMMECSecp256k1PublicKey {
        return KMMECSecp256k1PublicKey.secp256k1FromBytes(key.publicKey.toUncompressedBin())
    }

    /**
     * Private key for this extended key
     */
    actual fun privateKey(): KMMECSecp256k1PrivateKey {
        return KMMECSecp256k1PrivateKey.secp256k1FromBytes(key.privateKey.value.toByteArray())
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
        val index = if (axis.hardened) {
            DeterministicWallet.hardened(axis.number.toLong())
        } else {
            axis.number.toLong()
        }
        return ExtendedKey(DeterministicWallet.derivePrivateKey(key, index))
    }
}
