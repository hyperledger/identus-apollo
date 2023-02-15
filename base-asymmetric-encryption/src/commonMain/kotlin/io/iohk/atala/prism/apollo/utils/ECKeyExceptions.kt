package io.iohk.atala.prism.apollo.utils

import kotlin.js.JsExport

@JsExport
sealed class ECPrivateKeyException(message: String?, cause: Throwable?) : Exception(message, cause)

@JsExport
class ECPrivateKeyInitializationException(message: String) : ECPrivateKeyException(message, null)

@JsExport
class ECPrivateKeyDecodingException(message: String) : ECPrivateKeyException(message, null)

@JsExport
sealed class ECPublicKeyException(message: String?, cause: Throwable?) : Exception(message, cause)

@JsExport
class ECPublicKeyInitializationException(message: String) : ECPublicKeyException(message, null)
