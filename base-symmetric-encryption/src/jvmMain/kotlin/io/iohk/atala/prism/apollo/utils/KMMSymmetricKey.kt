package io.iohk.atala.prism.apollo.utils

import io.iohk.atala.prism.apollo.base64.base64PadDecodedBytes
import io.iohk.atala.prism.apollo.base64.base64PadEncoded
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

actual final class KMMSymmetricKey(val nativeType: SecretKey) : SymmetricKeyBase64Export {
    override fun exportToBase64(): String {
        return nativeType.encoded.base64PadEncoded
    }

    actual companion object : SymmetricKeyBase64Import, IVBase64Import, IVBase64Export, IVGeneration {
        override fun createKeyFromBase64(base64Encoded: String, algorithm: SymmetricKeyType): KMMSymmetricKey {
            val decodedKey = base64Encoded.base64PadDecodedBytes
            val originalKey: SecretKey = SecretKeySpec(decodedKey, 0, decodedKey.size, algorithm.value)
            return KMMSymmetricKey(originalKey)
        }
    }
}
