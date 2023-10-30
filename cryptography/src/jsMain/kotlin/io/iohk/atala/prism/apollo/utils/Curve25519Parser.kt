package io.iohk.atala.prism.apollo.utils

import io.iohk.atala.prism.apollo.base64.base64UrlDecodedBytes
import node.buffer.Buffer

@ExperimentalJsExport
@JsExport
object Curve25519Parser {
    val encodedLength = 43
    val rawLength = 32

    /**
     * Resolve the given ByteArray into the raw key value
     * @param bytes - ByteArray to be parsed, either Encoded or Raw data
     * @throws Error - if [bytes] is neither Encoded nor Raw
     * @return Buffer - raw key value
     */
    fun parseRaw(bytes: ByteArray): Buffer {
        val buffer = Buffer.from(bytes)

        if (buffer.length == encodedLength) {
            return Buffer.from(buffer.toByteArray().decodeToString().base64UrlDecodedBytes)
        }

        if (buffer.length == rawLength) {
            return buffer
        }

        throw Error("invalid raw key")
    }
}
