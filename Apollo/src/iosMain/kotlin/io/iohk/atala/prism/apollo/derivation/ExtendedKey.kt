package io.iohk.atala.prism.apollo.derivation

import fr.acinq.bitcoin.DeterministicWallet
import fr.acinq.bitcoin.KeyPath
import io.iohk.atala.prism.apollo.EC
import io.iohk.atala.prism.apollo.keys.ECKeyPair
import io.iohk.atala.prism.apollo.keys.ECPrivateKey
import io.iohk.atala.prism.apollo.keys.ECPublicKey

private fun toDerivationAxis(keyPath: KeyPath): DerivationPath =
    DerivationPath(keyPath.path.map { DerivationAxis(it.toInt()) })

public actual class ExtendedKey(private val key: DeterministicWallet.ExtendedPrivateKey) {
    public actual fun path(): DerivationPath =
        toDerivationAxis(key.path)

    public actual fun publicKey(): ECPublicKey =
        EC.toPublicKeyFromBytes(key.publicKey.toUncompressedBin())

    public actual fun privateKey(): ECPrivateKey =
        EC.toPrivateKeyFromBytes(key.privateKey.value.toByteArray())

    public actual fun keyPair(): ECKeyPair =
        ECKeyPair(publicKey(), privateKey())

    public actual fun derive(axis: DerivationAxis): ExtendedKey {
        val index =
            if (axis.hardened)
                DeterministicWallet.hardened(axis.number.toLong())
            else
                axis.number.toLong()
        return ExtendedKey(DeterministicWallet.derivePrivateKey(key, index))
    }
}
