package io.iohk.atala.prism.apollo.base58

import kotlin.test.Test
import kotlin.test.assertEquals

class Base58Tests {
    @Test
    fun testEncodeBase58BTC() {
        assertEquals("Z8XgFYKj8dyvwswt51Y", "Welcome to IOG".base58BtcEncoded)
    }

    @Test
    fun testDecodeBase58BTC() {
        assertEquals("Welcome to IOG", "Z8XgFYKj8dyvwswt51Y".base58BtcDecoded)
    }

    @Test
    fun testEncodeBase58Flickr() {
        assertEquals("y8wFfxjJ8CYVWSWT51x", "Welcome to IOG".base58FlickrEncoded)
    }

    @Test
    fun testDecodeBase58Flickr() {
        assertEquals("Welcome to IOG", "y8wFfxjJ8CYVWSWT51x".base58FlickrDecoded)
    }
}
