package io.iohk.atala.prism.apollo.derivation

import io.iohk.atala.prism.apollo.utils.KMMECKeyPair
import io.iohk.atala.prism.apollo.utils.KMMECSecp256k1PrivateKey
import io.iohk.atala.prism.apollo.utils.KMMECSecp256k1PublicKey

expect class ExtendedKey {
    /**
     * Derivation path used to obtain such key
     */
    fun path(): DerivationPath

    /**
     * Public key for this extended key
     */
    fun publicKey(): KMMECSecp256k1PublicKey

    /**
     * Private key for this extended key
     */
    fun privateKey(): KMMECSecp256k1PrivateKey

    /**
     * KeyPair for this extended key
     */
    fun keyPair(): KMMECKeyPair

    /**
     * Generates child extended key for given index
     */
    fun derive(axis: DerivationAxis): ExtendedKey
}
