package io.iohk.atala.prism.apollo.utils

import io.iohk.atala.prism.apollo.base64.base64PadDecodedBytes
import io.iohk.atala.prism.apollo.base64.base64PadEncoded
import kotlinx.browser.window

actual open class KMMSymmetricKey(val nativeValue: ByteArray) : SymmetricKeyBase64Export {
    override fun exportToBase64(): String {
        return nativeValue.base64PadEncoded
    }

    actual companion object : SymmetricKeyBase64Import, IVBase64Import, IVBase64Export, IVGeneration {
        override fun createKeyFromBase64(base64Encoded: String, algorithm: SymmetricKeyType): KMMSymmetricKey {
            return KMMSymmetricKey(base64Encoded.base64PadDecodedBytes)
        }

        override fun createRandomIV(size: Int): ByteArray {
            return if (jsTypeOf(window) != "undefined") { // Browser
                js(
                    """
                        var crypto = window.crypto;
                        var arr = new Uint32Array(size);
                        crypto.getRandomValues(arr);
                    """
                ) as ByteArray
            } else { // NodeJS
                js(
                    """
                        var crypto = require('crypto');
                        var buf = crypto.randomBytes(size);
                        var arr = new Uint8Array(size);
                        for (var i = 0; i < count; ++i) { 
                            arr[i] = buf.readUInt8(i) 
                        }
                        arr
                    """
                ) as ByteArray
            }
        }
    }
}
