package io.iohk.atala.prism.apollo.jwt

/**
 * All JOSE Exceptions
 */
sealed class JOSEException(message: String?) : Exception(message)

/**
 * Signals that an error has been reached unexpectedly
 */
class ParseException(message: String?) : JOSEException(message)
