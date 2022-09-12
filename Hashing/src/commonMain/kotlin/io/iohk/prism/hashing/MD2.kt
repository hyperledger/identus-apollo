package io.iohk.prism.hashing

import io.iohk.prism.hashing.internal.HMACInterface
import io.iohk.prism.hashing.internal.HashingBase

/**
 * This class implements the MD2 digest algorithm under the [HashingBase] API.
 * MD2 is defined by RFC 1319.
 */
final class MD2: HashingBase(), HMACInterface {
    private lateinit var x: IntArray
    private lateinit var c: IntArray
    private lateinit var d: ByteArray
    private var l = 0

    override val digestLength: Int
        get() = 16

    override val blockLength: Int
        get() = 16

    override fun doInit() {
        x = IntArray(48)
        c = IntArray(16)
        d = ByteArray(16)
        engineReset()
    }

    override fun engineReset() {
        for (i in 0..15) {
            x[i] = 0
            c[i] = 0
        }
        l = 0
    }

    override fun doPadding(output: ByteArray, outputOffset: Int) {
        val pending = flush()
        for (i in 0 until 16 - pending) update((16 - pending).toByte())
        flush()
        for (i in 0..15) d[i] = c[i].toByte()
        processBlock(d)
        for (i in 0..15) output[outputOffset + i] = x[i].toByte()
    }

    override fun processBlock(data: ByteArray) {
        var tL = l
        for (i in 0..15) {
            val u: Int = data[i].toInt() and 0xFF
            x[16 + i] = u
            x[32 + i] = x[i] xor u
            tL = S[u xor tL].let { c[i] = c[i] xor it; c[i] }
        }
        l = tL
        var t = 0
        for (j in 0..17) {
            var k = 0
            while (k < 48) {
                t = S[t].let { x[k + 0] = x[k + 0] xor it; x[k + 0] }
                t = S[t].let { x[k + 1] = x[k + 1] xor it; x[k + 1] }
                t = S[t].let { x[k + 2] = x[k + 2] xor it; x[k + 2] }
                t = S[t].let { x[k + 3] = x[k + 3] xor it; x[k + 3] }
                t = S[t].let { x[k + 4] = x[k + 4] xor it; x[k + 4] }
                t = S[t].let { x[k + 5] = x[k + 5] xor it; x[k + 5] }
                t = S[t].let { x[k + 6] = x[k + 6] xor it; x[k + 6] }
                t = S[t].let { x[k + 7] = x[k + 7] xor it; x[k + 7] }
                k += 8
            }
            t = t + j and 0xFF
        }
    }

    override fun toString() = "MD2"

    companion object {
        private val S = intArrayOf(
            41, 46, 67, 201, 162, 216, 124, 1,
            61, 54, 84, 161, 236, 240, 6, 19,
            98, 167, 5, 243, 192, 199, 115, 140,
            152, 147, 43, 217, 188, 76, 130, 202,
            30, 155, 87, 60, 253, 212, 224, 22,
            103, 66, 111, 24, 138, 23, 229, 18,
            190, 78, 196, 214, 218, 158, 222, 73,
            160, 251, 245, 142, 187, 47, 238, 122,
            169, 104, 121, 145, 21, 178, 7, 63,
            148, 194, 16, 137, 11, 34, 95, 33,
            128, 127, 93, 154, 90, 144, 50, 39,
            53, 62, 204, 231, 191, 247, 151, 3,
            255, 25, 48, 179, 72, 165, 181, 209,
            215, 94, 146, 42, 172, 86, 170, 198,
            79, 184, 56, 210, 150, 164, 125, 182,
            118, 252, 107, 226, 156, 116, 4, 241,
            69, 157, 112, 89, 100, 113, 135, 32,
            134, 91, 207, 101, 230, 45, 168, 2,
            27, 96, 37, 173, 174, 176, 185, 246,
            28, 70, 97, 105, 52, 64, 126, 15,
            85, 71, 163, 35, 221, 81, 175, 58,
            195, 92, 249, 206, 186, 197, 234, 38,
            44, 83, 13, 110, 133, 40, 132, 9,
            211, 223, 205, 244, 65, 129, 77, 82,
            106, 220, 55, 200, 108, 193, 171, 250,
            36, 225, 123, 8, 12, 189, 177, 74,
            120, 136, 149, 139, 227, 99, 232, 109,
            233, 203, 213, 254, 59, 0, 29, 57,
            242, 239, 183, 14, 102, 88, 208, 228,
            166, 119, 114, 248, 235, 117, 75, 10,
            49, 68, 80, 180, 143, 237, 31, 26,
            219, 153, 141, 51, 159, 17, 131, 20
        )
    }
}
