package io.iohk.atala.prism.apollo.base58

/**
 * Base58 encoding scheme
 *
 * TODO: Figure out a way to put both type with padded version in on scheme
 */
sealed interface Encoding {
    val alphabet: String

    /**
     * Base58 BTC => Standard
     */
    object BTC : Encoding {
        override val alphabet: String = "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz"
    }

    /**
     * Base58 Flickr
     */
    object Flickr : Encoding {
        override val alphabet: String = "123456789abcdefghijkmnopqrstuvwxyzABCDEFGHJKLMNPQRSTUVWXYZ"
    }
}
