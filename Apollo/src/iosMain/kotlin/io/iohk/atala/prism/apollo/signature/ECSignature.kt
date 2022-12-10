package io.iohk.atala.prism.apollo.signature

public actual class ECSignature actual constructor(private val data: ByteArray) : ECSignatureCommon(data) {
    override fun getEncoded(): ByteArray =
        data
}
