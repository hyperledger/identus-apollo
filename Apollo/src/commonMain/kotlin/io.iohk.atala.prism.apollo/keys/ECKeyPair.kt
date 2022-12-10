package io.iohk.atala.prism.apollo.keys

import kotlin.js.JsExport

@JsExport
public data class ECKeyPair(val publicKey: ECPublicKey, val privateKey: ECPrivateKey)
