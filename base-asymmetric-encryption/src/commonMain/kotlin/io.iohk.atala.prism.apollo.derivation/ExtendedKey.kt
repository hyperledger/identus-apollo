package io.iohk.atala.prism.apollo.derivation

import io.iohk.atala.prism.apollo.utils.KMMECKeyPair
import io.iohk.atala.prism.apollo.utils.KMMECPrivateKey
import io.iohk.atala.prism.apollo.utils.KMMECPublicKey

expect class ExtendedKey {
    /**
     * Derivation path used to obtain such key
     */
    fun path(): DerivationPath

    /**
     * Public key for this extended key
     */
    fun publicKey(): KMMECPublicKey

    /**
     * Private key for this extended key
     */
    fun privateKey(): KMMECPrivateKey

    /**
     * KeyPair for this extended key
     */
    fun keyPair(): KMMECKeyPair

    /**
     * Generates child extended key for given index
     */
    fun derive(axis: DerivationAxis): ExtendedKey
}
