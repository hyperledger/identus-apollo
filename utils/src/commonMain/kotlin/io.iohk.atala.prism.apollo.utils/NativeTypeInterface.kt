package io.iohk.atala.prism.apollo.utils

import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

@OptIn(ExperimentalJsExport::class)
@JsExport
interface NativeTypeInterface<NativeType> {
    fun nativeValue(): NativeType
}
