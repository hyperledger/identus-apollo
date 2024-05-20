package io.iohk.atala.prism.apollo.utils

import io.iohk.atala.prism.apollo.base64.base64UrlDecodedBytes
import node.buffer.Buffer

/**
 * The Curve25519Parser object provides methods for parsing byte arrays into raw key values.
 * It supports parsing both encoded and raw data.
 *
 * @property encodedLength The length of the encoded key value.
 * @property rawLength The length of the raw key value.
 */
@OptIn(ExperimentalJsExport::class)
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
