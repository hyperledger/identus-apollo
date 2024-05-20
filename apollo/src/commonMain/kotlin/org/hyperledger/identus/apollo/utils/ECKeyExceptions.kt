package org.hyperledger.identus.apollo.utils

import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

/**
 * Represents an exception related to EC private key operations.
 *
 * This is a sealed class and cannot be directly instantiated. Subclasses can be used to represent specific types of exceptions.
 *
 * @param message Optional message describing the exception.
 * @param cause Optional cause of the exception.
 */
@OptIn(ExperimentalJsExport::class)
@JsExport
sealed class ECPrivateKeyException(message: String?, cause: Throwable?) : Exception(message, cause)

/**
 * Exception thrown to indicate an error during the initialization of an EC private key.
 *
 * @param message The detail message.
 */
@OptIn(ExperimentalJsExport::class)
@JsExport
class ECPrivateKeyInitializationException(message: String) : ECPrivateKeyException(message, null)

/**
 * Exception thrown when there is an error decoding an EC private key.
 *
 * @param message The error message associated with the exception.
 */
@OptIn(ExperimentalJsExport::class)
@JsExport
class ECPrivateKeyDecodingException(message: String) : ECPrivateKeyException(message, null)

/**
 * A sealed class representing an exception related to the initialization or usage of an EC public key.
 *
 * @param message A description of the exception
 * @param cause The cause of the exception
 */
@OptIn(ExperimentalJsExport::class)
@JsExport
sealed class ECPublicKeyException(message: String?, cause: Throwable?) : Exception(message, cause)

/**
 * Exception thrown to indicate an error during the initialization of an EC public key.
 *
 * This exception is a subclass of [ECPublicKeyException].
 *
 * @param message The detail message explaining the reason for the exception.
 */
@OptIn(ExperimentalJsExport::class)
@JsExport
class ECPublicKeyInitializationException(message: String) : ECPublicKeyException(message, null)
