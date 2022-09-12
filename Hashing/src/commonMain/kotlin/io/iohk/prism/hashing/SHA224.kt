package io.iohk.prism.hashing

import io.iohk.prism.hashing.internal.HMACInterface
import io.iohk.prism.hashing.internal.MDHelper
import io.iohk.prism.hashing.internal.MathHelper

/**
 * This class implements the SHA-224 digest algorithm under the [MDHelper] API.
 * SHA-224 is specified by FIPS 180-2.
 */
final class SHA224: MDHelper(false, 8), HMACInterface {
    private lateinit var currentVal: IntArray
    private lateinit var w: IntArray
    override val digestLength: Int
        get() = 28
    override val blockLength: Int
        get() = 64

    override fun doInit() {
        currentVal = IntArray(8)
        w = IntArray(64)
        engineReset()
    }

    override fun engineReset() {
        currentVal = IntArray(8)
        currentVal[0] = -0x3efa6128
        currentVal[1] = 0x367CD507
        currentVal[2] = 0x3070DD17
        currentVal[3] = -0x8f1a6c7
        currentVal[4] = -0x3ff4cf
        currentVal[5] = 0x68581511
        currentVal[6] = 0x64F98FA7
        currentVal[7] = -0x4105b05c
    }

    override fun doPadding(output: ByteArray, outputOffset: Int) {
        makeMDPadding()
        val olen = digestLength
        var i = 0
        var j = 0
        while (j < olen) {
            MathHelper.encodeBEInt(currentVal[i], output, outputOffset + j)
            i++
            j += 4
        }
    }

    override fun processBlock(data: ByteArray) {
        var a = currentVal[0]
        var b = currentVal[1]
        var c = currentVal[2]
        var d = currentVal[3]
        var e = currentVal[4]
        var f = currentVal[5]
        var g = currentVal[6]
        var h = currentVal[7]
        for (i in 0..15) w[i] = MathHelper.decodeBEInt(data, 4 * i)
        for (i in 16..63) {
            w[i] = ((MathHelper.circularLeftInt(w[i - 2], 15)
                    xor MathHelper.circularLeftInt(w[i - 2], 13)
                    xor (w[i - 2] ushr 10)) +
                    w[i - 7] +
                    (MathHelper.circularLeftInt(w[i - 15], 25)
                            xor MathHelper.circularLeftInt(w[i - 15], 14)
                            xor (w[i - 15] ushr 3)) +
                    w[i - 16])
        }
        for (i in 0..63) {
            val t1 = (h + (MathHelper.circularLeftInt(e, 26) xor MathHelper.circularLeftInt(e, 21)
                    xor MathHelper.circularLeftInt(e, 7)) + (f and e xor (g and e.inv())) +
                    K[i] + w[i])
            val t2 = ((MathHelper.circularLeftInt(a, 30) xor MathHelper.circularLeftInt(a, 19)
                    xor MathHelper.circularLeftInt(a, 10)) +
                    (a and b xor (a and c) xor (b and c)))
            h = g
            g = f
            f = e
            e = d + t1
            d = c
            c = b
            b = a
            a = t1 + t2
        }
        currentVal[0] += a
        currentVal[1] += b
        currentVal[2] += c
        currentVal[3] += d
        currentVal[4] += e
        currentVal[5] += f
        currentVal[6] += g
        currentVal[7] += h
    }

    override fun toString() = "SHA-224"

    companion object {
        /** The initial value for SHA-224.  */
        private val K = intArrayOf(
            0x428A2F98, 0x71374491, -0x4a3f0431, -0x164a245b,
            0x3956C25B, 0x59F111F1, -0x6dc07d5c, -0x54e3a12b,
            -0x27f85568, 0x12835B01, 0x243185BE, 0x550C7DC3,
            0x72BE5D74, -0x7f214e02, -0x6423f959, -0x3e640e8c,
            -0x1b64963f, -0x1041b87a, 0x0FC19DC6, 0x240CA1CC,
            0x2DE92C6F, 0x4A7484AA, 0x5CB0A9DC, 0x76F988DA,
            -0x67c1aeae, -0x57ce3993, -0x4ffcd838, -0x40a68039,
            -0x391ff40d, -0x2a586eb9, 0x06CA6351, 0x14292967,
            0x27B70A85, 0x2E1B2138, 0x4D2C6DFC, 0x53380D13,
            0x650A7354, 0x766A0ABB, -0x7e3d36d2, -0x6d8dd37b,
            -0x5d40175f, -0x57e599b5, -0x3db47490, -0x3893ae5d,
            -0x2e6d17e7, -0x2966f9dc, -0xbf1ca7b, 0x106AA070,
            0x19A4C116, 0x1E376C08, 0x2748774C, 0x34B0BCB5,
            0x391C0CB3, 0x4ED8AA4A, 0x5B9CCA4F, 0x682E6FF3,
            0x748F82EE, 0x78A5636F, -0x7b3787ec, -0x7338fdf8,
            -0x6f410006, -0x5baf9315, -0x41065c09, -0x398e870e
        )
    }
}
