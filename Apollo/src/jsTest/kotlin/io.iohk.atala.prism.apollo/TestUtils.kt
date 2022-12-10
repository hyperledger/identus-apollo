package io.iohk.atala.prism.apollo

// Annotations for writing common tests, excluding individual platforms

@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
actual annotation class IgnoreJvmAndroid

@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
actual annotation class IgnoreIos

actual typealias IgnoreJs = kotlin.test.Ignore
