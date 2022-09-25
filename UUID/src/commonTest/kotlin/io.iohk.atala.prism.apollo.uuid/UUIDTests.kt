package io.iohk.atala.prism.apollo.uuid

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertTrue

class UUIDTests {

    @Test
    fun testVersion1Structure() {
        val id = UUID.randomUUID1()
        val str: String = id.toString()
        assertTrue(UUID.isValidUUID(str))
        assertEquals(36, str.length)
        assertEquals('-', str[8])
        assertEquals('-', str[13])
        assertEquals('1', str[14])
        assertEquals('-', str[18])
        assertEquals('-', str[23])
    }

    @Test
    fun testVersion2Structure() {
        assertFails {
            UUID.randomUUID2()
        }
    }

    @Test
    fun testVersion3Structure() {
        val id = UUID.randomUUID3("hello".encodeToByteArray())
        val str: String = id.toString()
        assertTrue(UUID.isValidUUID(str))
        assertEquals(36, str.length)
        assertEquals('-', str[8])
        assertEquals('-', str[13])
        assertEquals('3', str[14])
        assertEquals('-', str[18])
        assertEquals('-', str[23])
    }

    @Test
    fun testVersion4Structure() {
        val id = UUID.randomUUID4()
        val str: String = id.toString()
        assertTrue(UUID.isValidUUID(str))
        assertEquals(36, str.length)
        assertEquals('-', str[8])
        assertEquals('-', str[13])
        assertEquals('4', str[14])
        assertEquals('-', str[18])
        assertEquals('-', str[23])
    }

    @Test
    fun testVersion5Structure() {
        val id = UUID.randomUUID5("hello".encodeToByteArray())
        val str: String = id.toString()
        assertTrue(UUID.isValidUUID(str))
        assertEquals(36, str.length)
        assertEquals('-', str[8])
        assertEquals('-', str[13])
        assertEquals('5', str[14])
        assertEquals('-', str[18])
        assertEquals('-', str[23])
    }
}
