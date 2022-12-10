package io.iohk.atala.prism.apollo.keys

import com.ionspin.kotlin.bignum.integer.BigInteger
import com.ionspin.kotlin.bignum.integer.Sign
import io.iohk.atala.prism.apollo.ECConfig
import io.iohk.atala.prism.apollo.util.*
import kotlinx.cinterop.*
import platform.posix.size_tVar
import secp256k1.*

private fun toSecpPubkey(memScope: MemScope, key: UByteArray): secp256k1_pubkey {
    val pubkey = memScope.alloc<secp256k1_pubkey>()
    for (i in 0 until ECConfig.PUBLIC_KEY_BYTE_SIZE) {
        pubkey.data[i] = key[i]
    }

    return pubkey
}

private fun convertRepresentation(key: UByteArray): ByteArray {
    return memScoped {
        val context = secp256k1_context_create((SECP256K1_CONTEXT_SIGN or SECP256K1_CONTEXT_VERIFY).convert())
        val pubkey = toSecpPubkey(this, key)
        val output = memScope.allocArray<UByteVar>(ECConfig.PUBLIC_KEY_BYTE_SIZE)
        val outputLen = alloc<size_tVar>()
        outputLen.value = ECConfig.PUBLIC_KEY_BYTE_SIZE.convert()
        val result =
            secp256k1_ec_pubkey_serialize(context, output, outputLen.ptr, pubkey.ptr, SECP256K1_EC_UNCOMPRESSED)
        if (result != 1) {
            error("Could not serialize public key")
        }
        output.toUByteArray(outputLen.value.convert()).toByteArray()
    }
}

private fun computeCurvePoint(key: UByteArray): ECPoint {
    val encoded = convertRepresentation(key)
    val xBytes = encoded.slice(1..32)
    val x = BigInteger.fromByteArray(xBytes.toByteArray(), Sign.POSITIVE)
    val yBytes = encoded.slice(33..64)
    val y = BigInteger.fromByteArray(yBytes.toByteArray(), Sign.POSITIVE)

    return ECPoint(x, y)
}

public actual class ECPublicKey(internal val key: UByteArray) :
    ECPublicKeyCommon(computeCurvePoint(key))
