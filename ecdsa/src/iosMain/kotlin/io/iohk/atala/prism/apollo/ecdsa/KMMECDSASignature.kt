package io.iohk.atala.prism.apollo.ecdsa

actual class KMMECDSASignature actual constructor(private val data: ByteArray) : KMMECDSASignatureCommon(data) {
    override fun getEncoded(): ByteArray =
        data
    actual companion object : KMMECDSASignatureCommonStaticInterface
}
