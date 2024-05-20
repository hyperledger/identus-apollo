package org.hyperledger.identus.apollo.derivation

import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

/**
 * The MnemonicException class is a sealed class that represents an exception related to mnemonic codes.
 * It extends the built-in [Exception] class.
 *
 * @param message The detail message of the exception.
 * @param cause The cause of the exception.
 */
@OptIn(ExperimentalJsExport::class)
@JsExport
sealed class MnemonicException(message: String?, cause: Throwable?) :
    Exception(message, cause)

/**
 * Exception thrown when the length of a mnemonic is not valid.
 *
 * @param message The detail message explaining the exception.
 * @param cause The cause of the exception.
 */
@OptIn(ExperimentalJsExport::class)
@JsExport
class MnemonicLengthException(message: String?, cause: Throwable? = null) :
    MnemonicException(message, cause)

/**
 * Exception that is thrown when an error related to mnemonic words occurs.
 *
 * @param message The detail message for the exception.
 * @param cause The cause of the exception.
 */
@OptIn(ExperimentalJsExport::class)
@JsExport
class MnemonicWordException(message: String?, cause: Throwable? = null) :
    MnemonicException(message, cause)

/**
 * Exception thrown when a mnemonic checksum does not match the expected value.
 *
 * @param message The detail message for the exception.
 * @param cause The cause of the exception.
 */
@OptIn(ExperimentalJsExport::class)
@JsExport
class MnemonicChecksumException(message: String?, cause: Throwable? = null) :
    MnemonicException(message, cause)
