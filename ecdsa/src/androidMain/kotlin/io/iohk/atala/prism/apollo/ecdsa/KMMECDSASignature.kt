package io.iohk.atala.prism.apollo.ecdsa

actual class KMMECDSASignature actual constructor(public val data: ByteArray) : KMMECDSASignatureCommon(data) {
    override fun getEncoded(): ByteArray =
        data
}
