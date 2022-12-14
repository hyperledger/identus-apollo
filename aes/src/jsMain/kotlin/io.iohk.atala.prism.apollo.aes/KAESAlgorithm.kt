package io.iohk.atala.prism.apollo.aes

import io.iohk.atala.prism.apollo.utils.NativeTypeInterface
import kotlinx.browser.window

actual enum class KAESAlgorithm : NativeTypeInterface<KAESAlgorithmNativeType> {
    AES_128,
    AES_192,
    AES_256;

    override fun nativeValue(): KAESAlgorithmNativeType {
        return if (jsTypeOf(window) != "undefined") { // Browser
            when (this) {
                AES_128 -> "AES"
                AES_192 -> "AES"
                AES_256 -> "AES"
            }
        } else { // NodeJS
            when (this) {
                AES_128 -> "aes-128"
                AES_192 -> "aes-192"
                AES_256 -> "aes-256"
            }
        }
    }
}
