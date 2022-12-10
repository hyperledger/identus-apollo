package io.iohk.atala.prism.apollo.derivation

import io.iohk.atala.prism.apollo.keys.ECKeyPair
import io.iohk.atala.prism.apollo.keys.ECPrivateKey
import io.iohk.atala.prism.apollo.keys.ECPublicKey

public expect class ExtendedKey {
    /** Derivation path used to obtain such key */
    public fun path(): DerivationPath

    /** Public key for this extended key */
    public fun publicKey(): ECPublicKey

    /** Private key for this extended key */
    public fun privateKey(): ECPrivateKey

    /** KeyPair for this extended key */
    public fun keyPair(): ECKeyPair

    /** Generates child extended key for given index */
    public fun derive(axis: DerivationAxis): ExtendedKey
}
