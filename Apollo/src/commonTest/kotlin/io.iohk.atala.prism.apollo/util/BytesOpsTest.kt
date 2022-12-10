package io.iohk.atala.prism.apollo.util

import kotlin.test.Test
import kotlin.test.assertContentEquals

class BytesOpsTest {

    @Test
    fun testPadding() {
        assertContentEquals(
            byteArrayOf(1, 2, 3, 4).padStart(6, 0),
            byteArrayOf(0, 0, 1, 2, 3, 4)
        )

        assertContentEquals(
            byteArrayOf().padStart(3, 0),
            byteArrayOf(0, 0, 0)
        )

        assertContentEquals(
            byteArrayOf(1, 2, 3).padStart(3, 0),
            byteArrayOf(1, 2, 3)
        )
    }
}
