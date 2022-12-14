package io.iohk.atala.prism.apollo.securerandom

import kotlinx.browser.window

actual class SecureRandom actual constructor(
    actual val seed: ByteArray
) : SecureRandomInterface {

    override fun nextBytes(size: Int): ByteArray {
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
                    for (var i = 0; i < size; ++i) { 
                        arr[i] = buf.readUInt8(i) 
                    }
                    arr
                """
            ) as ByteArray
        }
    }

    actual companion object : SecureRandomStaticInterface {
        override fun generateSeed(numBytes: Int): ByteArray {
            return if (jsTypeOf(window) != "undefined") { // Browser
                js(
                    """
                        var crypto = window.crypto;
                        var arr = new Uint32Array(numBytes);
                        crypto.getRandomValues(arr);
                    """
                ) as ByteArray
            } else { // NodeJS
                js(
                    """
                        var crypto = require('crypto');
                        var buf = crypto.randomBytes(numBytes);
                        var arr = new Uint8Array(numBytes);
                        for (var i = 0; i < numBytes; ++i) { 
                            arr[i] = buf.readUInt8(i) 
                        }
                        arr
                    """
                ) as ByteArray
            }
        }
    }
}
