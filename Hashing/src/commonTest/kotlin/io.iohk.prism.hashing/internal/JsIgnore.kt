package io.iohk.prism.hashing.internal

/**
 * Ignore a test when running the test on a JavaScript test runtime.
 */
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
expect annotation class JsIgnore()
