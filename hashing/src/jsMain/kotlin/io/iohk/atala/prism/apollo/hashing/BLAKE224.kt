package io.iohk.atala.prism.apollo.hashing

import io.iohk.atala.prism.apollo.hashing.internal.HashingBase

actual class BLAKE224 actual constructor() : HashingBase() {
    override val digestLength: Int
        get() = 28
    override val blockLength: Int
        get() = 64

    override fun toString() = "BLAKE-224"

    override fun engineReset() {
        return
    }

    override fun processBlock(data: ByteArray) {
        return
    }

    override fun doPadding(output: ByteArray, outputOffset: Int) {
        return
    }

    override fun doInit() {
        return
    }

    override fun update(input: ByteArray) {
        throw NotImplementedError("Not implemented")
    }

    override fun digest(): ByteArray {
        throw NotImplementedError("Not implemented")
    }

    override fun digest(input: ByteArray): ByteArray {
        throw NotImplementedError("Not implemented")
    }
}
