package io.iohk.atala.prism.apollo.hashing

import io.iohk.atala.prism.apollo.hashing.internal.HashingBase

actual final class BLAKE512 actual constructor() : HashingBase() {
    override val digestLength: Int
        get() = 64
    override val blockLength: Int
        get() = 128

    override fun toString() = "BLAKE-512"

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
