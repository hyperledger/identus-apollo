package io.iohk.atala.prism.apollo.multibase

import io.iohk.atala.prism.apollo.base16.base16DecodedBytes
import io.iohk.atala.prism.apollo.base16.base16Encoded
import io.iohk.atala.prism.apollo.base16.base16UpperDecodedBytes
import io.iohk.atala.prism.apollo.base16.base16UpperEncoded
import io.iohk.atala.prism.apollo.base32.base32DecodedBytes
import io.iohk.atala.prism.apollo.base32.base32Encoded
import io.iohk.atala.prism.apollo.base32.base32HexDecodedBytes
import io.iohk.atala.prism.apollo.base32.base32HexEncoded
import io.iohk.atala.prism.apollo.base32.base32HexPadDecodedBytes
import io.iohk.atala.prism.apollo.base32.base32HexPadEncoded
import io.iohk.atala.prism.apollo.base32.base32HexUpperDecodedBytes
import io.iohk.atala.prism.apollo.base32.base32HexUpperEncoded
import io.iohk.atala.prism.apollo.base32.base32HexUpperPadDecodedBytes
import io.iohk.atala.prism.apollo.base32.base32HexUpperPadEncoded
import io.iohk.atala.prism.apollo.base32.base32PadDecodedBytes
import io.iohk.atala.prism.apollo.base32.base32PadEncoded
import io.iohk.atala.prism.apollo.base32.base32UpperDecodedBytes
import io.iohk.atala.prism.apollo.base32.base32UpperEncoded
import io.iohk.atala.prism.apollo.base32.base32UpperPadDecodedBytes
import io.iohk.atala.prism.apollo.base32.base32UpperPadEncoded
import io.iohk.atala.prism.apollo.base58.base58BtcDecodedBytes
import io.iohk.atala.prism.apollo.base58.base58BtcEncoded
import io.iohk.atala.prism.apollo.base58.base58FlickrDecodedBytes
import io.iohk.atala.prism.apollo.base58.base58FlickrEncoded
import io.iohk.atala.prism.apollo.base64.base64DecodedBytes
import io.iohk.atala.prism.apollo.base64.base64Encoded
import io.iohk.atala.prism.apollo.base64.base64PadDecodedBytes
import io.iohk.atala.prism.apollo.base64.base64PadEncoded
import io.iohk.atala.prism.apollo.base64.base64UrlDecodedBytes
import io.iohk.atala.prism.apollo.base64.base64UrlEncoded
import io.iohk.atala.prism.apollo.base64.base64UrlPadDecodedBytes
import io.iohk.atala.prism.apollo.base64.base64UrlPadEncoded

/**
 * MultiBase Implementation of [multibase](https://github.com/multiformats/multibase) -self identifying base encodings- in KMM
 */
final object MultiBase {

    fun encode(base: Base, data: String): String {
        return encode(base, data.encodeToByteArray())
    }

    fun encode(base: Base, data: ByteArray): String {
        return when (base) {
            Base.BASE16 -> "${base.prefix}${data.base16Encoded}"
            Base.BASE16_UPPER -> "${base.prefix}${data.base16UpperEncoded}"

            Base.BASE32 -> "${base.prefix}${data.base32Encoded}"
            Base.BASE32_UPPER -> "${base.prefix}${data.base32UpperEncoded}"
            Base.BASE32_PAD -> "${base.prefix}${data.base32PadEncoded}"
            Base.BASE32_UPPER_PAD -> "${base.prefix}${data.base32UpperPadEncoded}"
            Base.BASE32_HEX -> "${base.prefix}${data.base32HexEncoded}"
            Base.BASE32_HEX_UPPER -> "${base.prefix}${data.base32HexUpperEncoded}"
            Base.BASE32_HEX_PAD -> "${base.prefix}${data.base32HexPadEncoded}"
            Base.BASE32_HEX_UPPER_PAD -> "${base.prefix}${data.base32HexUpperPadEncoded}"

            Base.BASE58_FLICKR -> "${base.prefix}${data.base58FlickrEncoded}"
            Base.BASE58_BTC -> "${base.prefix}${data.base58BtcEncoded}"

            Base.BASE64 -> "${base.prefix}${data.base64Encoded}"
            Base.BASE64_URL -> "${base.prefix}${data.base64UrlEncoded}"
            Base.BASE64_PAD -> "${base.prefix}${data.base64PadEncoded}"
            Base.BASE64_URL_PAD -> "${base.prefix}${data.base64UrlPadEncoded}"
        }
    }

    @Throws(IllegalStateException::class)
    fun decode(data: String): ByteArray {
        val prefix = data[0]
        val rest = data.substring(1)
        return when (Base.lookup(prefix)) {
            Base.BASE16 -> rest.base16DecodedBytes
            Base.BASE16_UPPER -> rest.base16UpperDecodedBytes

            Base.BASE32 -> rest.base32DecodedBytes
            Base.BASE32_UPPER -> rest.base32UpperDecodedBytes
            Base.BASE32_PAD -> rest.base32PadDecodedBytes
            Base.BASE32_UPPER_PAD -> rest.base32UpperPadDecodedBytes
            Base.BASE32_HEX -> rest.base32HexDecodedBytes
            Base.BASE32_HEX_UPPER -> rest.base32HexUpperDecodedBytes
            Base.BASE32_HEX_PAD -> rest.base32HexPadDecodedBytes
            Base.BASE32_HEX_UPPER_PAD -> rest.base32HexUpperPadDecodedBytes

            Base.BASE58_FLICKR -> rest.base58FlickrDecodedBytes
            Base.BASE58_BTC -> rest.base58BtcDecodedBytes

            Base.BASE64 -> rest.base64DecodedBytes
            Base.BASE64_URL -> rest.base64UrlDecodedBytes
            Base.BASE64_PAD -> rest.base64PadDecodedBytes
            Base.BASE64_URL_PAD -> rest.base64UrlPadDecodedBytes
        }
    }

    enum class Base(val prefix: Char) {
        BASE16('f'),
        BASE16_UPPER('F'),

        BASE32('b'),
        BASE32_UPPER('B'),
        BASE32_PAD('c'),
        BASE32_UPPER_PAD('C'),
        BASE32_HEX('v'),
        BASE32_HEX_UPPER('V'),
        BASE32_HEX_PAD('t'),
        BASE32_HEX_UPPER_PAD('T'),

        BASE58_FLICKR('Z'),
        BASE58_BTC('z'),

        BASE64('m'),
        BASE64_URL('u'),
        BASE64_PAD('M'),
        BASE64_URL_PAD('U');

        companion object {
            private val baseMap: MutableMap<Char, Base> = mutableMapOf()

            init {
                for (base in values()) {
                    baseMap[base.prefix] = base
                }
            }

            fun lookup(prefix: Char): Base {
                return baseMap[prefix]
                    ?: throw IllegalStateException("Unknown Multibase type: $prefix")
            }
        }
    }
}
