package io.iohk.atala.prism.apollo.derivation

import kotlin.js.JsExport

@JsExport
public sealed class MnemonicException(message: String?, cause: Throwable?) :
    Exception(message, cause)

@JsExport
public class MnemonicLengthException(message: String?, cause: Throwable? = null) :
    MnemonicException(message, cause)

@JsExport
public class MnemonicWordException(message: String?, cause: Throwable? = null) :
    MnemonicException(message, cause)

@JsExport
public class MnemonicChecksumException(message: String?, cause: Throwable? = null) :
    MnemonicException(message, cause)
