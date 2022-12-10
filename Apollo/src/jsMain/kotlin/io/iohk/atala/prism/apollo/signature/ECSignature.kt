package io.iohk.atala.prism.apollo.signature

import io.iohk.atala.prism.apollo.util.BytesOps.bytesToHex
import io.iohk.atala.prism.apollo.util.BytesOps.hexToBytes

@JsExport
public actual class ECSignature internal constructor(internal val sig: String) : ECSignatureCommon(hexToBytes(sig)) {
    @JsName("fromBytes")
    public actual constructor(data: ByteArray) : this(bytesToHex(data))

    override fun getEncoded(): ByteArray =
        hexToBytes(sig)
}
