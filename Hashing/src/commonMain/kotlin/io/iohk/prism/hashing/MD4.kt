package io.iohk.prism.hashing

import io.iohk.prism.hashing.internal.HMACInterface
import io.iohk.prism.hashing.internal.MathHelper
import io.iohk.prism.hashing.internal.MDHelper

/**
 * This class implements the MD4 digest algorithm under the [MDHelper] API.
 * MD4 is described in RFC 1320.
 */
final class MD4: MDHelper(true, 8), HMACInterface {
    private lateinit var currentVal: IntArray
    override val digestLength: Int
        get() = 16
    override val blockLength: Int
        get() = 64

    override fun doInit() {
        currentVal = IntArray(4)
        engineReset()
    }

    override fun engineReset() {
        currentVal[0] = 0x67452301
        currentVal[1] = -0x10325477
        currentVal[2] = -0x67452302
        currentVal[3] = 0x10325476
    }

    override fun doPadding(output: ByteArray, outputOffset: Int) {
        makeMDPadding()
        for (i in 0..3) MathHelper.encodeLEInt(
            currentVal[i], output,
            outputOffset + 4 * i
        )
    }

    override fun processBlock(data: ByteArray) {
        /*
		 * This method could have been made simpler by using
		 * external methods for 32-bit decoding, or the round
		 * functions F, G and H. However, it seems that the JIT
		 * compiler from Sun's JDK decides not to inline those
		 * methods, although it could (they are private final,
		 * hence cannot be overridden) and it would yield better
		 * performance.
		 */
        var a = currentVal[0]
        var b = currentVal[1]
        var c = currentVal[2]
        var d = currentVal[3]
        val x00: Int = (data[0].toInt() and 0xFF
                or (data[0 + 1].toInt() and 0xFF shl 8)
                or (data[0 + 2].toInt() and 0xFF shl 16)
                or (data[0 + 3].toInt() and 0xFF shl 24))
        val x01: Int = (data[4].toInt() and 0xFF
                or (data[4 + 1].toInt() and 0xFF shl 8)
                or (data[4 + 2].toInt() and 0xFF shl 16)
                or (data[4 + 3].toInt() and 0xFF shl 24))
        val x02: Int = (data[8].toInt() and 0xFF
                or (data[8 + 1].toInt() and 0xFF shl 8)
                or (data[8 + 2].toInt() and 0xFF shl 16)
                or (data[8 + 3].toInt() and 0xFF shl 24))
        val x03: Int = (data[12].toInt() and 0xFF
                or (data[12 + 1].toInt() and 0xFF shl 8)
                or (data[12 + 2].toInt() and 0xFF shl 16)
                or (data[12 + 3].toInt() and 0xFF shl 24))
        val x04: Int = (data[16].toInt() and 0xFF
                or (data[16 + 1].toInt() and 0xFF shl 8)
                or (data[16 + 2].toInt() and 0xFF shl 16)
                or (data[16 + 3].toInt() and 0xFF shl 24))
        val x05: Int = (data[20].toInt() and 0xFF
                or (data[20 + 1].toInt() and 0xFF shl 8)
                or (data[20 + 2].toInt() and 0xFF shl 16)
                or (data[20 + 3].toInt() and 0xFF shl 24))
        val x06: Int = (data[24].toInt() and 0xFF
                or (data[24 + 1].toInt() and 0xFF shl 8)
                or (data[24 + 2].toInt() and 0xFF shl 16)
                or (data[24 + 3].toInt() and 0xFF shl 24))
        val x07: Int = (data[28].toInt() and 0xFF
                or (data[28 + 1].toInt() and 0xFF shl 8)
                or (data[28 + 2].toInt() and 0xFF shl 16)
                or (data[28 + 3].toInt() and 0xFF shl 24))
        val x08: Int = (data[32].toInt() and 0xFF
                or (data[32 + 1].toInt() and 0xFF shl 8)
                or (data[32 + 2].toInt() and 0xFF shl 16)
                or (data[32 + 3].toInt() and 0xFF shl 24))
        val x09: Int = (data[36].toInt() and 0xFF
                or (data[36 + 1].toInt() and 0xFF shl 8)
                or (data[36 + 2].toInt() and 0xFF shl 16)
                or (data[36 + 3].toInt() and 0xFF shl 24))
        val x10: Int = (data[40].toInt() and 0xFF
                or (data[40 + 1].toInt() and 0xFF shl 8)
                or (data[40 + 2].toInt() and 0xFF shl 16)
                or (data[40 + 3].toInt() and 0xFF shl 24))
        val x11: Int = (data[44].toInt() and 0xFF
                or (data[44 + 1].toInt() and 0xFF shl 8)
                or (data[44 + 2].toInt() and 0xFF shl 16)
                or (data[44 + 3].toInt() and 0xFF shl 24))
        val x12: Int = (data[48].toInt() and 0xFF
                or (data[48 + 1].toInt() and 0xFF shl 8)
                or (data[48 + 2].toInt() and 0xFF shl 16)
                or (data[48 + 3].toInt() and 0xFF shl 24))
        val x13: Int = (data[52].toInt() and 0xFF
                or (data[52 + 1].toInt() and 0xFF shl 8)
                or (data[52 + 2].toInt() and 0xFF shl 16)
                or (data[52 + 3].toInt() and 0xFF shl 24))
        val x14: Int = (data[56].toInt() and 0xFF
                or (data[56 + 1].toInt() and 0xFF shl 8)
                or (data[56 + 2].toInt() and 0xFF shl 16)
                or (data[56 + 3].toInt() and 0xFF shl 24))
        val x15: Int = (data[60].toInt() and 0xFF
                or (data[60 + 1].toInt() and 0xFF shl 8)
                or (data[60 + 2].toInt() and 0xFF shl 16)
                or (data[60 + 3].toInt() and 0xFF shl 24))
        var t: Int
        t = a + (c xor d and b xor d) + x00
        a = t shl 3 or (t ushr 32 - 3)
        t = d + (b xor c and a xor c) + x01
        d = t shl 7 or (t ushr 32 - 7)
        t = c + (a xor b and d xor b) + x02
        c = t shl 11 or (t ushr 32 - 11)
        t = b + (d xor a and c xor a) + x03
        b = t shl 19 or (t ushr 32 - 19)
        t = a + (c xor d and b xor d) + x04
        a = t shl 3 or (t ushr 32 - 3)
        t = d + (b xor c and a xor c) + x05
        d = t shl 7 or (t ushr 32 - 7)
        t = c + (a xor b and d xor b) + x06
        c = t shl 11 or (t ushr 32 - 11)
        t = b + (d xor a and c xor a) + x07
        b = t shl 19 or (t ushr 32 - 19)
        t = a + (c xor d and b xor d) + x08
        a = t shl 3 or (t ushr 32 - 3)
        t = d + (b xor c and a xor c) + x09
        d = t shl 7 or (t ushr 32 - 7)
        t = c + (a xor b and d xor b) + x10
        c = t shl 11 or (t ushr 32 - 11)
        t = b + (d xor a and c xor a) + x11
        b = t shl 19 or (t ushr 32 - 19)
        t = a + (c xor d and b xor d) + x12
        a = t shl 3 or (t ushr 32 - 3)
        t = d + (b xor c and a xor c) + x13
        d = t shl 7 or (t ushr 32 - 7)
        t = c + (a xor b and d xor b) + x14
        c = t shl 11 or (t ushr 32 - 11)
        t = b + (d xor a and c xor a) + x15
        b = t shl 19 or (t ushr 32 - 19)
        t = a + (d and c or (d or c and b)) + x00 + 0x5A827999
        a = t shl 3 or (t ushr 32 - 3)
        t = d + (c and b or (c or b and a)) + x04 + 0x5A827999
        d = t shl 5 or (t ushr 32 - 5)
        t = c + (b and a or (b or a and d)) + x08 + 0x5A827999
        c = t shl 9 or (t ushr 32 - 9)
        t = b + (a and d or (a or d and c)) + x12 + 0x5A827999
        b = t shl 13 or (t ushr 32 - 13)
        t = a + (d and c or (d or c and b)) + x01 + 0x5A827999
        a = t shl 3 or (t ushr 32 - 3)
        t = d + (c and b or (c or b and a)) + x05 + 0x5A827999
        d = t shl 5 or (t ushr 32 - 5)
        t = c + (b and a or (b or a and d)) + x09 + 0x5A827999
        c = t shl 9 or (t ushr 32 - 9)
        t = b + (a and d or (a or d and c)) + x13 + 0x5A827999
        b = t shl 13 or (t ushr 32 - 13)
        t = a + (d and c or (d or c and b)) + x02 + 0x5A827999
        a = t shl 3 or (t ushr 32 - 3)
        t = d + (c and b or (c or b and a)) + x06 + 0x5A827999
        d = t shl 5 or (t ushr 32 - 5)
        t = c + (b and a or (b or a and d)) + x10 + 0x5A827999
        c = t shl 9 or (t ushr 32 - 9)
        t = b + (a and d or (a or d and c)) + x14 + 0x5A827999
        b = t shl 13 or (t ushr 32 - 13)
        t = a + (d and c or (d or c and b)) + x03 + 0x5A827999
        a = t shl 3 or (t ushr 32 - 3)
        t = d + (c and b or (c or b and a)) + x07 + 0x5A827999
        d = t shl 5 or (t ushr 32 - 5)
        t = c + (b and a or (b or a and d)) + x11 + 0x5A827999
        c = t shl 9 or (t ushr 32 - 9)
        t = b + (a and d or (a or d and c)) + x15 + 0x5A827999
        b = t shl 13 or (t ushr 32 - 13)
        t = a + (b xor c xor d) + x00 + 0x6ED9EBA1
        a = t shl 3 or (t ushr 32 - 3)
        t = d + (a xor b xor c) + x08 + 0x6ED9EBA1
        d = t shl 9 or (t ushr 32 - 9)
        t = c + (d xor a xor b) + x04 + 0x6ED9EBA1
        c = t shl 11 or (t ushr 32 - 11)
        t = b + (c xor d xor a) + x12 + 0x6ED9EBA1
        b = t shl 15 or (t ushr 32 - 15)
        t = a + (b xor c xor d) + x02 + 0x6ED9EBA1
        a = t shl 3 or (t ushr 32 - 3)
        t = d + (a xor b xor c) + x10 + 0x6ED9EBA1
        d = t shl 9 or (t ushr 32 - 9)
        t = c + (d xor a xor b) + x06 + 0x6ED9EBA1
        c = t shl 11 or (t ushr 32 - 11)
        t = b + (c xor d xor a) + x14 + 0x6ED9EBA1
        b = t shl 15 or (t ushr 32 - 15)
        t = a + (b xor c xor d) + x01 + 0x6ED9EBA1
        a = t shl 3 or (t ushr 32 - 3)
        t = d + (a xor b xor c) + x09 + 0x6ED9EBA1
        d = t shl 9 or (t ushr 32 - 9)
        t = c + (d xor a xor b) + x05 + 0x6ED9EBA1
        c = t shl 11 or (t ushr 32 - 11)
        t = b + (c xor d xor a) + x13 + 0x6ED9EBA1
        b = t shl 15 or (t ushr 32 - 15)
        t = a + (b xor c xor d) + x03 + 0x6ED9EBA1
        a = t shl 3 or (t ushr 32 - 3)
        t = d + (a xor b xor c) + x11 + 0x6ED9EBA1
        d = t shl 9 or (t ushr 32 - 9)
        t = c + (d xor a xor b) + x07 + 0x6ED9EBA1
        c = t shl 11 or (t ushr 32 - 11)
        t = b + (c xor d xor a) + x15 + 0x6ED9EBA1
        b = t shl 15 or (t ushr 32 - 15)
        currentVal[0] += a
        currentVal[1] += b
        currentVal[2] += c
        currentVal[3] += d
    }

    override fun toString() = "MD4"
}
