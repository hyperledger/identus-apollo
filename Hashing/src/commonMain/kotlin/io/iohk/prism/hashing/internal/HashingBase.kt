package io.iohk.prism.hashing.internal

/**
 * All hashing algorithms shared operations
 */
abstract class HashingBase : Digest {
    private var digestLen: Int
    private val blockLen: Int
    private var inputLen: Int
    private var outputBuf: ByteArray
    /**
     * Get the "block count": this is the number of times the
     * [.processBlock] method has been invoked for the
     * current hash operation. That counter is incremented
     * *after* the call to [.processBlock].
     *
     * @return the block count
     */
    protected var blockCount: Long
        private set
    /**
     * Get a reference to an internal buffer with the same size
     * than a block. The contents of that buffer are defined only
     * immediately after a call to [.flush]: if
     * [.flush] return the value `n`, then the
     * first `n` bytes of the array returned by this method
     * are the `n` bytes of input data which are still
     * unprocessed. The values of the remaining bytes are
     * undefined and may be altered at will.
     *
     * @return a block-sized internal buffer
     */
    protected val blockBuffer: ByteArray

    /**
     * Instantiate the engine.
     */
    init {
        doInit()
        digestLen = digestLength
        blockLen = blockLength
        blockBuffer = ByteArray(blockLen)
        outputBuf = ByteArray(digestLen)
        inputLen = 0
        blockCount = 0
    }

    /**
     * Reset the hash algorithm state.
     */
    protected abstract fun engineReset()

    /**
     * Process one block of data.
     *
     * @param data   the data block
     */
    protected abstract fun processBlock(data: ByteArray)

    /**
     * Perform the final padding and store the result in the
     * provided buffer. This method shall call [.flush]
     * and then [.update] with the appropriate padding
     * data in order to get the full input data.
     *
     * @param output   the output buffer
     * @param outputOffset   the output offset
     */
    protected abstract fun doPadding(output: ByteArray, outputOffset: Int)

    /**
     * This function is called at object creation time; the
     * implementation should use it to perform initialization tasks.
     * After this method is called, the implementation should be ready
     * to process data or meaningfully honour calls such as
     * [.getDigestLength].
     */
    protected abstract fun doInit()

    private fun adjustDigestLen() {
        if (digestLen == 0) {
            digestLen = digestLength
            outputBuf = ByteArray(digestLen)
        }
    }

    override fun digest(): ByteArray {
        adjustDigestLen()
        val result = ByteArray(digestLen)
        digest(result, 0, digestLen)
        return result
    }

    override fun digest(input: ByteArray): ByteArray {
        update(input, 0, input.size)
        return digest()
    }

    override fun digest(output: ByteArray, offset: Int, length: Int): Int {
        adjustDigestLen()
        return if (length >= digestLen) {
            doPadding(output, offset)
            reset()
            digestLen
        } else {
            doPadding(outputBuf, 0)
            outputBuf.copyInto(output, offset, 0, length)
            reset()
            length
        }
    }

    override fun reset() {
        engineReset()
        inputLen = 0
        blockCount = 0
    }

    override fun update(input: Byte) {
        blockBuffer[inputLen++] = input
        if (inputLen == blockLen) {
            processBlock(blockBuffer)
            blockCount++
            inputLen = 0
        }
    }

    override fun update(input: ByteArray) {
        update(input, 0, input.size)
    }

    override fun update(input: ByteArray, offset: Int, length: Int) {
        @Suppress("NAME_SHADOWING")
        var offset = offset
        var len = length
        while (len > 0) {
            var copyLen = blockLen - inputLen
            if (copyLen > len) copyLen = len
            input.copyInto(blockBuffer, inputLen, offset, offset + copyLen)

            offset += copyLen
            inputLen += copyLen
            len -= copyLen
            if (inputLen == blockLen) {
                processBlock(blockBuffer)
                blockCount++
                inputLen = 0
            }
        }
    }

    /**
     * Flush internal buffers, so that less than a block of data
     * may at most be upheld.
     *
     * @return the number of bytes still unprocessed after the flush
     */
    protected fun flush(): Int {
        return inputLen
    }
}
