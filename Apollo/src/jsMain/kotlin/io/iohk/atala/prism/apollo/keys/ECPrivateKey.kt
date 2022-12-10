package io.iohk.atala.prism.apollo.keys

import com.ionspin.kotlin.bignum.integer.BigInteger
import io.iohk.atala.prism.apollo.ECConfig
import io.iohk.atala.prism.apollo.externals.BN

@JsExport
public actual class ECPrivateKey internal constructor(internal val bigNumber: BN) :
    ECPrivateKeyCommon(BigInteger.parseString(bigNumber.toString())) {

    override fun getEncoded(): ByteArray {
        val byteList = bigNumber.toArray().map { it.toByte() }
        val padding = ByteArray(ECConfig.PRIVATE_KEY_BYTE_SIZE - byteList.size) { 0 }
        return padding + byteList
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class.js != other::class.js) return false
        if (!super.equals(other)) return false

        other as ECPrivateKey

        if (getHexEncoded() != other.getHexEncoded()) return false

        return true
    }

    override fun hashCode(): Int =
        getHexEncoded().hashCode()
}
