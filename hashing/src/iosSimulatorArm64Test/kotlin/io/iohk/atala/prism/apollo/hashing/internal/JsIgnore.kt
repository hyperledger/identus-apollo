package io.iohk.atala.prism.apollo.hashing.internal

/**
 * Ignore a test when running the test on a JavaScript test runtime.
 */
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
actual annotation class JsIgnore actual constructor()
