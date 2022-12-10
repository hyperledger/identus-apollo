package io.iohk.atala.prism.apollo

import com.ionspin.kotlin.bignum.integer.BigInteger
import com.ionspin.kotlin.bignum.integer.Sign
import fr.acinq.bitcoin.*
import fr.acinq.secp256k1.Secp256k1
import io.iohk.atala.prism.c.secp256k1.*
import io.iohk.atala.prism.apollo.keys.ECCoordinate
import io.iohk.atala.prism.apollo.keys.ECKeyPair
import io.iohk.atala.prism.apollo.keys.ECPrivateKey
import io.iohk.atala.prism.apollo.keys.ECPublicKey
import io.iohk.atala.prism.apollo.signature.ECSignature
import io.iohk.atala.prism.apollo.util.padStart
import io.iohk.atala.prism.apollo.util.toCArrayPointer
import io.iohk.atala.prism.apollo.util.toUByteArray
import kotlinx.cinterop.*
import platform.posix.fclose
import platform.posix.fopen
import platform.posix.fread


public actual object EC : ECAbstract() {
    // secp256k1 private key is just a sequence of random bytes, hence
    // we ask /dev/urandom to produce the necessary amount of random bytes
    private fun generatePrivateKey(memScope: MemScope): CArrayPointer<UByteVar> {
        val privateKey = memScope.allocArray<UByteVar>(ECConfig.PRIVATE_KEY_BYTE_SIZE)
        val privateKeyPtr = privateKey.getPointer(memScope)
        val urandom = fopen("/dev/urandom", "rb") ?: error("No /dev/urandom on this device")
        try {
            do {
                fread(privateKeyPtr, 1.convert(), ECConfig.PRIVATE_KEY_BYTE_SIZE.convert(), urandom)
                for (n in 0 until ECConfig.PRIVATE_KEY_BYTE_SIZE) privateKey[n] = privateKeyPtr[n]
                val privateKeyInt = BigInteger.fromUByteArray(privateKey.toUByteArray(ECConfig.PRIVATE_KEY_BYTE_SIZE), Sign.POSITIVE)
            } while (privateKeyInt >= ECConfig.n) /* n is the order of the secp256k1 curve  */
        } finally {
            fclose(urandom)
        }
        return privateKey
    }

    private fun createContext(memScope: MemScope, options: Int): CPointer<secp256k1_context>? {
        val context = secp256k1_context_create(options.convert())

        // Clean-up context by destroying it on scope closure
        memScope.defer {
            secp256k1_context_destroy(context)
        }

        return context
    }

    private fun parsePublicKey(encoded: ByteArray): ECPublicKey =
        memScoped {
            val context = createContext(this, SECP256K1_CONTEXT_SIGN or SECP256K1_CONTEXT_VERIFY)
            val pubkey = alloc<secp256k1_pubkey>()
            val input = encoded.toUByteArray().toCArrayPointer(this)
            val result = secp256k1_ec_pubkey_parse(context, pubkey.ptr, input, encoded.size.convert())
            if (result != 1) {
                error("Could not parse public key")
            }

            val publicKeyBytes = pubkey.data.toUByteArray(ECConfig.PUBLIC_KEY_BYTE_SIZE)
            ECPublicKey(publicKeyBytes)
        }

    override fun generateKeyPair(): ECKeyPair {
        return memScoped {
            val context = createContext(this, SECP256K1_CONTEXT_SIGN or SECP256K1_CONTEXT_VERIFY)
            val privateKey = generatePrivateKey(this)
            val publicKey = alloc<secp256k1_pubkey>()
            if (secp256k1_ec_pubkey_create(context, publicKey.ptr, privateKey) != 1) {
                error("Invalid private key")
            }

            val publicKeyBytes = publicKey.data.toUByteArray(ECConfig.PUBLIC_KEY_BYTE_SIZE)
            val privateKeyBytes = privateKey.toUByteArray(ECConfig.PRIVATE_KEY_BYTE_SIZE)

            ECKeyPair(ECPublicKey(publicKeyBytes), ECPrivateKey(privateKeyBytes))
        }
    }

    override fun toPrivateKeyFromBigInteger(d: BigInteger): ECPrivateKey {
        return ECPrivateKey(d.toByteArray().padStart(ECConfig.PRIVATE_KEY_BYTE_SIZE, 0).toUByteArray())
    }

    override fun toPublicKeyFromCompressed(compressed: ByteArray): ECPublicKey {
        require(compressed.size == ECConfig.PUBLIC_KEY_COMPRESSED_BYTE_SIZE) {
            "Compressed byte array's expected length is ${ECConfig.PUBLIC_KEY_COMPRESSED_BYTE_SIZE}, but got ${compressed.size}"
        }

        return parsePublicKey(compressed)
    }

    override fun toPublicKeyFromBigIntegerCoordinates(x: BigInteger, y: BigInteger): ECPublicKey {
        return parsePublicKey(byteArrayOf(0x04) + ECCoordinate(x).bytes() + ECCoordinate(y).bytes())
    }

    override fun toPublicKeyFromPrivateKey(privateKey: ECPrivateKey): ECPublicKey {
        return memScoped {
            val context = createContext(this, SECP256K1_CONTEXT_SIGN or SECP256K1_CONTEXT_VERIFY)
            val privkey = privateKey.getEncoded().toUByteArray().toCArrayPointer(this)
            val publicKey = alloc<secp256k1_pubkey>()
            if (secp256k1_ec_pubkey_create(context, publicKey.ptr, privkey) != 1) {
                error("Invalid private key")
            }

            val publicKeyBytes = publicKey.data.toUByteArray(ECConfig.PUBLIC_KEY_BYTE_SIZE)
            ECPublicKey(publicKeyBytes)
        }
    }

    override fun signBytes(data: ByteArray, privateKey: ECPrivateKey): ECSignature {
        val data32 = Crypto.sha256(data)
        val compressedBytes = Secp256k1.sign(data32, privateKey.getEncoded())
        val signatureBytes = Secp256k1.compact2der(compressedBytes)

        return ECSignature(signatureBytes)
    }

    override fun verifyBytes(data: ByteArray, publicKey: ECPublicKey, signature: ECSignature): Boolean {
        val data32 = Crypto.sha256(data)
        return Secp256k1.verify(
            signature.getEncoded(),
            data32,
            publicKey.getEncoded()
        )
    }
}
