package io.iohk.atala.prism.apollo.utils

import io.iohk.atala.prism.apollo.secp256k1.Secp256k1Lib
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport
import kotlin.js.JsName

interface KMMECSecp256k1PrivateKeyCommonStaticInterface {

    fun secp256k1FromByteArray(d: ByteArray): KMMECSecp256k1PrivateKey

    @Throws(ECPrivateKeyDecodingException::class)
    fun tweak(privateKeyData: ByteArray, derivationPrivateKeyData: ByteArray): KMMECSecp256k1PrivateKey
}

@OptIn(ExperimentalJsExport::class)
@JsExport
class KMMECSecp256k1PrivateKey : Encodable {
    val raw: ByteArray

    @JsName("fromByteArray")
    constructor(raw: ByteArray) {
        this.raw = raw
    }

    fun getPublicKey(): KMMECSecp256k1PublicKey {
        val pubKeyBytes = Secp256k1Lib().createPublicKey(raw, false)
        return KMMECSecp256k1PublicKey(pubKeyBytes)
    }

    override fun getEncoded(): ByteArray {
        return raw
    }

    /**
     * Sign the provided data with EC Secp256K1
     * @param data data that you want to sign
     * @return signature
     */
    fun sign(data: ByteArray): ByteArray {
        val secp256k1Lib = Secp256k1Lib()
        return secp256k1Lib.sign(raw, data)
    }

    /**
     * Verify provided signature with the public key that is generated using the private key
     * @param signature that we need to verify
     * @param data that was used in signature
     * @return true when valid, false when invalid
     */
    fun verify(signature: ByteArray, data: ByteArray): Boolean {
        val secp256k1Lib = Secp256k1Lib()
        return secp256k1Lib.verify(getPublicKey().raw, signature, data)
    }

    companion object : KMMECSecp256k1PrivateKeyCommonStaticInterface {
        override fun secp256k1FromByteArray(d: ByteArray): KMMECSecp256k1PrivateKey {
            return KMMECSecp256k1PrivateKey(d)
        }

        override fun tweak(
            privateKeyData: ByteArray,
            derivationPrivateKeyData: ByteArray
        ): KMMECSecp256k1PrivateKey {
            val derivedKey = Secp256k1Lib().derivePrivateKey(privateKeyData, derivationPrivateKeyData)
            return derivedKey?.let { KMMECSecp256k1PrivateKey(derivedKey) }
                ?: run { throw ECPrivateKeyDecodingException("Error while tweaking") }
        }
    }
}
