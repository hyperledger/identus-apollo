package io.iohk.atala.prism.apollo.utils

import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

@OptIn(ExperimentalJsExport::class)
@JsExport
sealed class ECPrivateKeyException(message: String?, cause: Throwable?) : Exception(message, cause)

@OptIn(ExperimentalJsExport::class)
@JsExport
class ECPrivateKeyInitializationException(message: String) : ECPrivateKeyException(message, null)

@OptIn(ExperimentalJsExport::class)
@JsExport
class ECPrivateKeyDecodingException(message: String) : ECPrivateKeyException(message, null)

@OptIn(ExperimentalJsExport::class)
@JsExport
sealed class ECPublicKeyException(message: String?, cause: Throwable?) : Exception(message, cause)

@OptIn(ExperimentalJsExport::class)
@JsExport
class ECPublicKeyInitializationException(message: String) : ECPublicKeyException(message, null)
