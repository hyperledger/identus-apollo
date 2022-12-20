package io.iohk.atala.prism.apollo.securerandom

import io.iohk.atala.prism.apollo.utils.toByteArray
import js.typedarrays.Uint8Array
import web.crypto.crypto

actual class SecureRandom actual constructor(
    actual val seed: ByteArray
) : SecureRandomInterface {

    override fun nextBytes(size: Int): ByteArray {
        val arr = Uint8Array(size)
        return crypto.getRandomValues(arr).buffer.toByteArray()
//        return if (jsTypeOf(window) != "undefined") { // Browser
//            (js(
//                """
//                    var arr = new Uint32Array(size);
//                    window.crypto.getRandomValues(arr);
//                """
//            ) as Uint32Array).buffer.toByteArray()
//        } else { // NodeJS
//            (js(
//                """
//                    var crypto = require('crypto');
//                    var buf = crypto.randomBytes(size);
//                    var arr = new Uint8Array(size);
//                    for (var i = 0; i < size; ++i) {
//                        arr[i] = buf.readUInt8(i)
//                    }
//                    arr
//                """
//            ) as Uint32Array).buffer.toByteArray()
//        }
    }

    actual companion object : SecureRandomStaticInterface {
        override fun generateSeed(numBytes: Int): ByteArray {
            val arr = Uint8Array(numBytes)
            return crypto.getRandomValues(arr).buffer.toByteArray()
//            return if (jsTypeOf(window) != "undefined") { // Browser
//                (js(
//                    """
//                        var arr = new Uint32Array(numBytes);
//                        window.crypto.getRandomValues(arr);
//                    """
//                ) as Uint32Array).buffer.toByteArray()
//            } else { // NodeJS
//                (js(
//                    """
//                        var crypto = require('crypto');
//                        var buf = crypto.randomBytes(numBytes);
//                        var arr = new Uint8Array(numBytes);
//                        for (var i = 0; i < numBytes; ++i) {
//                            arr[i] = buf.readUInt8(i)
//                        }
//                        arr
//                    """
//                ) as Uint32Array).buffer.toByteArray()
//            }
        }
    }
}
