package io.iohk.atala.prism.apollo.derivation

import io.iohk.atala.prism.apollo.utils.KMMECKeyPair
import io.iohk.atala.prism.apollo.utils.KMMECPrivateKey
import io.iohk.atala.prism.apollo.utils.KMMECPublicKey
import io.iohk.atala.prism.apollo.utils.external.BIP32Interface
import io.iohk.atala.prism.apollo.utils.toByteArray

actual class ExtendedKey internal constructor(private val bip32: BIP32Interface, private val path: DerivationPath) {
    /**
     * Derivation path used to obtain such key
     */
    actual fun path(): DerivationPath {
        return path
    }

    /**
     * Public key for this extended key
     */
    actual fun publicKey(): KMMECPublicKey {
        return privateKey().getPublicKey()
    }

    /**
     * Private key for this extended key
     */
    actual fun privateKey(): KMMECPrivateKey {
        return KMMECPrivateKey.secp256k1FromBytes(bip32.privateKey!!.toByteArray())
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
        val derivedBip32 = if (axis.hardened) {
            bip32.deriveHardened(axis.number)
        } else {
            bip32.derive(axis.number)
        }
        return ExtendedKey(derivedBip32, path.derive(axis))
    }
}
