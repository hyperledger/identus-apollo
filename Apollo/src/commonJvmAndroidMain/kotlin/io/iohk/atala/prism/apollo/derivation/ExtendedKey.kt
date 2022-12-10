package io.iohk.atala.prism.apollo.derivation

import io.iohk.atala.prism.apollo.EC
import io.iohk.atala.prism.apollo.keys.ECKeyPair
import io.iohk.atala.prism.apollo.keys.ECPrivateKey
import io.iohk.atala.prism.apollo.keys.ECPublicKey
import io.iohk.atala.prism.apollo.util.toKotlinBigInteger
import org.bitcoinj.crypto.ChildNumber
import org.bitcoinj.crypto.DeterministicKey
import org.bitcoinj.crypto.HDKeyDerivation

public actual class ExtendedKey(private val key: DeterministicKey) {
    /** Derivation path used to obtain such key */
    public actual fun path(): DerivationPath =
        DerivationPath(key.path.map { axis -> DerivationAxis(axis.i) })

    /** Public key for this extended key */
    public actual fun publicKey(): ECPublicKey {
        val ecPoint = key.pubKeyPoint
        return EC.toPublicKeyFromBigIntegerCoordinates(
            ecPoint.xCoord.toBigInteger().toKotlinBigInteger(),
            ecPoint.yCoord.toBigInteger().toKotlinBigInteger()
        )
    }

    /** Private key for this extended key */
    public actual fun privateKey(): ECPrivateKey =
        EC.toPrivateKeyFromBigInteger(key.privKey.toKotlinBigInteger())

    /** KeyPair for this extended key */
    public actual fun keyPair(): ECKeyPair =
        ECKeyPair(publicKey(), privateKey())

    /** Generates child extended key for given index */
    public actual fun derive(axis: DerivationAxis): ExtendedKey =
        ExtendedKey(HDKeyDerivation.deriveChildKey(key, ChildNumber(axis.i)))
}
