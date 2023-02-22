package io.iohk.atala.prism.apollo.varint

import okio.Buffer
import kotlin.test.Test
import kotlin.test.assertEquals

class VarIntTests {
    @Test
    fun testVarInt() {
        val origin = 1234774
        val byteBuffer = Buffer()
        VarInt.write(origin, byteBuffer)
        val value: Int = VarInt.read(byteBuffer)
        assertEquals(origin, value)
    }
}
