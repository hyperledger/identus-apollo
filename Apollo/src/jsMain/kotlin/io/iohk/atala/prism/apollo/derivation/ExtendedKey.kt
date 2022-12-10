package io.iohk.atala.prism.apollo.derivation

import io.iohk.atala.prism.apollo.EC
import io.iohk.atala.prism.apollo.externals.BIP32Interface
import io.iohk.atala.prism.apollo.keys.ECKeyPair
import io.iohk.atala.prism.apollo.keys.ECPrivateKey
import io.iohk.atala.prism.apollo.keys.ECPublicKey
import io.iohk.atala.prism.apollo.util.toByteArray

@JsExport
public actual class ExtendedKey internal constructor(
    private val bip32: BIP32Interface,
    private val path: DerivationPath
) {
    public actual fun path(): DerivationPath =
        path

    public actual fun publicKey(): ECPublicKey =
        EC.toPublicKeyFromPrivateKey(privateKey())

    public actual fun privateKey(): ECPrivateKey =
        EC.toPrivateKeyFromBytes(bip32.privateKey!!.toByteArray())

    public actual fun keyPair(): ECKeyPair =
        ECKeyPair(publicKey(), privateKey())

    public actual fun derive(axis: DerivationAxis): ExtendedKey {
        val derivedBip32 =
            if (axis.hardened)
                bip32.deriveHardened(axis.number)
            else
                bip32.derive(axis.number)
        return ExtendedKey(derivedBip32, path.derive(axis))
    }
}
