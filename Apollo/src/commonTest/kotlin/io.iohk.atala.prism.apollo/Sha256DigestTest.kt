package io.iohk.atala.prism.apollo

import io.iohk.atala.prism.apollo.util.BytesOps
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class Sha256DigestTest {

    @Test
    fun testIllegalFromHex() {
        val expected1 =
            "The given hex string doesn't correspond to a valid SHA-256 hash encoded as string"

        val err1 = assertFailsWith(
            exceptionClass = IllegalArgumentException::class,
            message = "No exception found",
            block = { Sha256Digest.fromHex("AA") }
        )
        assertEquals(expected1, err1.message)

        val expected2 =
            "The given hex string doesn't correspond to a valid SHA-256 hash encoded as string"
        val err2 = assertFailsWith(
            exceptionClass = IllegalArgumentException::class,
            message = "No exception found",
            block = { Sha256Digest.fromHex(":AA") }
        )
        assertEquals(expected2, err2.message)
    }

    @Test
    fun testFromHex() {
        val text = BytesOps.bytesToHex("Hello World. It's kotlin sdk!!!!".encodeToByteArray())
        assertEquals(text.length, 64)
        val result = Sha256Digest.fromHex(text)
        assertEquals(BytesOps.hexToBytes(text).toList(), result.value.toList())
    }

    @Test
    fun testIllegalFromBytes() {
        val expected =
            "The given byte array does not correspond to a SHA256 hash. It must have exactly 32 bytes"

        fun fromBytes(i: Int): IllegalArgumentException = assertFailsWith(
            exceptionClass = IllegalArgumentException::class,
            message = "No exception found",
            block = { Sha256Digest.fromBytes(ByteArray(i)) }
        )

        for (i in 1..31) {
            val err = fromBytes(i)
            assertEquals(expected, err.message)
        }

        val err = fromBytes(33)
        assertEquals(expected, err.message)
    }

    @Test
    fun testFromBytes() {
        val bytes = ByteArray(32)
        val result = Sha256Digest.fromBytes(bytes)
        assertEquals(bytes, result.value)
    }
}
