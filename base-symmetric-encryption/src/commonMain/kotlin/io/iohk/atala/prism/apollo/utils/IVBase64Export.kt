package io.iohk.atala.prism.apollo.utils

import io.iohk.atala.prism.apollo.base64.base64PadEncoded

interface IVBase64Export {
    /**
     * Export IV to Base64 standard with padding
     *
     * @param iv iv value to convert to Base64 standard with padding
     */
    fun exportToBase64(iv: ByteArray): String {
        return iv.base64PadEncoded
    }
}
