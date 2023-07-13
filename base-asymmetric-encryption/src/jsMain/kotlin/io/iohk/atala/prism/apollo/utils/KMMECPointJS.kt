package io.iohk.atala.prism.apollo.utils // ktlint-disable filename

import io.iohk.atala.prism.apollo.utils.external.BN

@OptIn(ExperimentalJsExport::class)
@JsExport
data class KMMECPointJS(val x: BN, val y: BN)
