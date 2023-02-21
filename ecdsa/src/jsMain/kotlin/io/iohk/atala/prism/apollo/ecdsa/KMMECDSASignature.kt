package io.iohk.atala.prism.apollo.ecdsa

import io.iohk.atala.prism.apollo.utils.decodeHex
import io.iohk.atala.prism.apollo.utils.toHex

@JsExport
public actual class KMMECDSASignature internal constructor(internal val sig: String) : KMMECDSASignatureCommon(sig.decodeHex()) {
    @JsName("fromBytes")
    public actual constructor(data: ByteArray) : this(data.toHex())

    override fun getEncoded(): ByteArray =
        sig.decodeHex()

    actual companion object : KMMECDSASignatureCommonStaticInterface
}
