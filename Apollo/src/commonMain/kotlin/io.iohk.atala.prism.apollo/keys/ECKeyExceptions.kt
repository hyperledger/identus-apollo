package io.iohk.atala.prism.apollo.keys

import kotlin.js.JsExport

@JsExport
public sealed class ECPrivateKeyException(message: String?, cause: Throwable?) :
    Exception(message, cause)

@JsExport
public class ECPrivateKeyInitializationException(
    override val message: String
) :
    ECPrivateKeyException(message, null)

@JsExport
public class ECPrivateKeyDecodingException(
    override val message: String
) :
    ECPrivateKeyException(message, null)

@JsExport
public sealed class ECPublicKeyException(message: String?, cause: Throwable?) :
    Exception(message, cause)

@JsExport
public class ECPublicKeyInitializationException(
    override val message: String
) :
    ECPublicKeyException(message, null)
