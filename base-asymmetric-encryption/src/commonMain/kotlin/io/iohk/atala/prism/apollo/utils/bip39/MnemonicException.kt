package io.iohk.atala.prism.apollo.utils.bip39

import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

@OptIn(ExperimentalJsExport::class)
@JsExport
sealed class MnemonicException(message: String?, cause: Throwable?) :
    Exception(message, cause)

@OptIn(ExperimentalJsExport::class)
@JsExport
class MnemonicLengthException(message: String?, cause: Throwable? = null) :
    MnemonicException(message, cause)

@OptIn(ExperimentalJsExport::class)
@JsExport
class MnemonicWordException(message: String?, cause: Throwable? = null) :
    MnemonicException(message, cause)

@OptIn(ExperimentalJsExport::class)
@JsExport
class MnemonicChecksumException2(message: String?, cause: Throwable? = null) :
    MnemonicException(message, cause)
