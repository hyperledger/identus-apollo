package io.iohk.atala.prism.apollo.keys

import io.iohk.atala.prism.apollo.ECConfig
import io.iohk.atala.prism.apollo.GenericJavaCryptography
import io.iohk.atala.prism.apollo.util.toJavaBigInteger
import io.iohk.atala.prism.apollo.util.toKotlinBigInteger
import io.iohk.atala.prism.apollo.util.toUnsignedByteArray
import java.security.PrivateKey

public actual class ECPrivateKey(internal val key: PrivateKey) :
    ECPrivateKeyCommon(GenericJavaCryptography.privateKeyD(key).toKotlinBigInteger()) {
    override fun getEncoded(): ByteArray {
        val byteList = getD().toJavaBigInteger().toUnsignedByteArray()
        val padding = ByteArray(ECConfig.PRIVATE_KEY_BYTE_SIZE - byteList.size) { 0 }
        return padding + byteList
    }
}
