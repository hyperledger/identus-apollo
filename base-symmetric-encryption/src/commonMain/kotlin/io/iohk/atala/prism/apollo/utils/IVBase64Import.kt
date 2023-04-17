package io.iohk.atala.prism.apollo.utils

import io.iohk.atala.prism.apollo.base64.base64PadDecodedBytes

interface IVBase64Import {
    /**
     * Create an IV from Base64 standard with padding
     *
     * @param base64Encoded Base64 standard with padding value to convert to IV
     */
    fun createIVFromBase64(base64Encoded: String): ByteArray {
        return base64Encoded.base64PadDecodedBytes
    }
}
