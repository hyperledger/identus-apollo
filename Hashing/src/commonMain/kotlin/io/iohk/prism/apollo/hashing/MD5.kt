package io.iohk.prism.apollo.hashing

import io.iohk.prism.apollo.hashing.internal.HMACInterface
import io.iohk.prism.apollo.hashing.internal.MDHelper
import io.iohk.prism.apollo.hashing.internal.MathHelper

/**
 * This class implements the MD5 digest algorithm under the [MDHelper] API.
 * MD5 is described in RFC 1321.
 */
final class MD5 : MDHelper(true, 8), HMACInterface {
    private lateinit var currentVal: IntArray
    private lateinit var x: IntArray
    override val digestLength: Int
        get() = 16
    override val blockLength: Int
        get() = 64

    override fun doInit() {
        currentVal = IntArray(4)
        x = IntArray(16)
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
            currentVal[i],
            output, outputOffset + 4 * i
        )
    }

    override fun processBlock(data: ByteArray) {
        var a = currentVal[0]
        var b = currentVal[1]
        var c = currentVal[2]
        var d = currentVal[3]
        for (i in 0..15) x[i] = MathHelper.decodeLEInt(data, 4 * i)
        a = b + MathHelper.circularLeftInt(a + f(b, c, d) + x[0] + -0x28955b88, 7)
        d = a + MathHelper.circularLeftInt(d + f(a, b, c) + x[1] + -0x173848aa, 12)
        c = d + MathHelper.circularLeftInt(c + f(d, a, b) + x[2] + 0x242070DB, 17)
        b = c + MathHelper.circularLeftInt(b + f(c, d, a) + x[3] + -0x3e423112, 22)
        a = b + MathHelper.circularLeftInt(a + f(b, c, d) + x[4] + -0xa83f051, 7)
        d = a + MathHelper.circularLeftInt(d + f(a, b, c) + x[5] + 0x4787C62A, 12)
        c = d + MathHelper.circularLeftInt(c + f(d, a, b) + x[6] + -0x57cfb9ed, 17)
        b = c + MathHelper.circularLeftInt(b + f(c, d, a) + x[7] + -0x2b96aff, 22)
        a = b + MathHelper.circularLeftInt(a + f(b, c, d) + x[8] + 0x698098D8, 7)
        d = a + MathHelper.circularLeftInt(d + f(a, b, c) + x[9] + -0x74bb0851, 12)
        c = d + MathHelper.circularLeftInt(c + f(d, a, b) + x[10] + -0xa44f, 17)
        b = c + MathHelper.circularLeftInt(b + f(c, d, a) + x[11] + -0x76a32842, 22)
        a = b + MathHelper.circularLeftInt(a + f(b, c, d) + x[12] + 0x6B901122, 7)
        d = a + MathHelper.circularLeftInt(d + f(a, b, c) + x[13] + -0x2678e6d, 12)
        c = d + MathHelper.circularLeftInt(c + f(d, a, b) + x[14] + -0x5986bc72, 17)
        b = c + MathHelper.circularLeftInt(b + f(c, d, a) + x[15] + 0x49B40821, 22)
        a = b + MathHelper.circularLeftInt(a + g(b, c, d) + x[1] + -0x9e1da9e, 5)
        d = a + MathHelper.circularLeftInt(d + g(a, b, c) + x[6] + -0x3fbf4cc0, 9)
        c = d + MathHelper.circularLeftInt(c + g(d, a, b) + x[11] + 0x265E5A51, 14)
        b = c + MathHelper.circularLeftInt(b + g(c, d, a) + x[0] + -0x16493856, 20)
        a = b + MathHelper.circularLeftInt(a + g(b, c, d) + x[5] + -0x29d0efa3, 5)
        d = a + MathHelper.circularLeftInt(d + g(a, b, c) + x[10] + 0x02441453, 9)
        c = d + MathHelper.circularLeftInt(c + g(d, a, b) + x[15] + -0x275e197f, 14)
        b = c + MathHelper.circularLeftInt(b + g(c, d, a) + x[4] + -0x182c0438, 20)
        a = b + MathHelper.circularLeftInt(a + g(b, c, d) + x[9] + 0x21E1CDE6, 5)
        d = a + MathHelper.circularLeftInt(d + g(a, b, c) + x[14] + -0x3cc8f82a, 9)
        c = d + MathHelper.circularLeftInt(c + g(d, a, b) + x[3] + -0xb2af279, 14)
        b = c + MathHelper.circularLeftInt(b + g(c, d, a) + x[8] + 0x455A14ED, 20)
        a = b + MathHelper.circularLeftInt(a + g(b, c, d) + x[13] + -0x561c16fb, 5)
        d = a + MathHelper.circularLeftInt(d + g(a, b, c) + x[2] + -0x3105c08, 9)
        c = d + MathHelper.circularLeftInt(c + g(d, a, b) + x[7] + 0x676F02D9, 14)
        b = c + MathHelper.circularLeftInt(b + g(c, d, a) + x[12] + -0x72d5b376, 20)
        a = b + MathHelper.circularLeftInt(a + h(b, c, d) + x[5] + -0x5c6be, 4)
        d = a + MathHelper.circularLeftInt(d + h(a, b, c) + x[8] + -0x788e097f, 11)
        c = d + MathHelper.circularLeftInt(c + h(d, a, b) + x[11] + 0x6D9D6122, 16)
        b = c + MathHelper.circularLeftInt(b + h(c, d, a) + x[14] + -0x21ac7f4, 23)
        a = b + MathHelper.circularLeftInt(a + h(b, c, d) + x[1] + -0x5b4115bc, 4)
        d = a + MathHelper.circularLeftInt(d + h(a, b, c) + x[4] + 0x4BDECFA9, 11)
        c = d + MathHelper.circularLeftInt(c + h(d, a, b) + x[7] + -0x944b4a0, 16)
        b = c + MathHelper.circularLeftInt(b + h(c, d, a) + x[10] + -0x41404390, 23)
        a = b + MathHelper.circularLeftInt(a + h(b, c, d) + x[13] + 0x289B7EC6, 4)
        d = a + MathHelper.circularLeftInt(d + h(a, b, c) + x[0] + -0x155ed806, 11)
        c = d + MathHelper.circularLeftInt(c + h(d, a, b) + x[3] + -0x2b10cf7b, 16)
        b = c + MathHelper.circularLeftInt(b + h(c, d, a) + x[6] + 0x04881D05, 23)
        a = b + MathHelper.circularLeftInt(a + h(b, c, d) + x[9] + -0x262b2fc7, 4)
        d = a + MathHelper.circularLeftInt(d + h(a, b, c) + x[12] + -0x1924661b, 11)
        c = d + MathHelper.circularLeftInt(c + h(d, a, b) + x[15] + 0x1FA27CF8, 16)
        b = c + MathHelper.circularLeftInt(b + h(c, d, a) + x[2] + -0x3b53a99b, 23)
        a = b + MathHelper.circularLeftInt(a + i(b, c, d) + x[0] + -0xbd6ddbc, 6)
        d = a + MathHelper.circularLeftInt(d + i(a, b, c) + x[7] + 0x432AFF97, 10)
        c = d + MathHelper.circularLeftInt(c + i(d, a, b) + x[14] + -0x546bdc59, 15)
        b = c + MathHelper.circularLeftInt(b + i(c, d, a) + x[5] + -0x36c5fc7, 21)
        a = b + MathHelper.circularLeftInt(a + i(b, c, d) + x[12] + 0x655B59C3, 6)
        d = a + MathHelper.circularLeftInt(d + i(a, b, c) + x[3] + -0x70f3336e, 10)
        c = d + MathHelper.circularLeftInt(c + i(d, a, b) + x[10] + -0x100b83, 15)
        b = c + MathHelper.circularLeftInt(b + i(c, d, a) + x[1] + -0x7a7ba22f, 21)
        a = b + MathHelper.circularLeftInt(a + i(b, c, d) + x[8] + 0x6FA87E4F, 6)
        d = a + MathHelper.circularLeftInt(d + i(a, b, c) + x[15] + -0x1d31920, 10)
        c = d + MathHelper.circularLeftInt(c + i(d, a, b) + x[6] + -0x5cfebcec, 15)
        b = c + MathHelper.circularLeftInt(b + i(c, d, a) + x[13] + 0x4E0811A1, 21)
        a = b + MathHelper.circularLeftInt(a + i(b, c, d) + x[4] + -0x8ac817e, 6)
        d = a + MathHelper.circularLeftInt(d + i(a, b, c) + x[11] + -0x42c50dcb, 10)
        c = d + MathHelper.circularLeftInt(c + i(d, a, b) + x[2] + 0x2AD7D2BB, 15)
        b = c + MathHelper.circularLeftInt(b + i(c, d, a) + x[9] + -0x14792c6f, 21)
        currentVal[0] += a
        currentVal[1] += b
        currentVal[2] += c
        currentVal[3] += d
    }

    override fun toString() = "MD5"

    companion object {

        private fun f(x: Int, y: Int, z: Int): Int {
            return y and x or (z and x.inv())
        }

        private fun g(x: Int, y: Int, z: Int): Int {
            return x and z or (y and z.inv())
        }

        private fun h(x: Int, y: Int, z: Int): Int {
            return x xor y xor z
        }

        private fun i(x: Int, y: Int, z: Int): Int {
            return y xor (x or z.inv())
        }
    }
}
