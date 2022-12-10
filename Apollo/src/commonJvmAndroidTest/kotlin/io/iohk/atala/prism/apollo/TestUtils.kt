package io.iohk.atala.prism.apollo

// Annotations for writing common tests, excluding individual platforms

actual typealias IgnoreJvmAndroid = org.junit.Ignore

@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
actual annotation class IgnoreIos

@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
actual annotation class IgnoreJs
