package io.iohk.atala.prism.apollo.utils

import io.iohk.atala.prism.apollo.utils.external.BN

/**
 * Represents a point on an elliptic curve in JavaScript.
 *
 * @property x The x-coordinate of the point.
 * @property y The y-coordinate of the point.
 * @constructor Creates a new `KMMECPointJS` object.
 *
 * @param x The x-coordinate of the point.
 * @param y The y-coordinate of the point.
 */
@OptIn(ExperimentalJsExport::class)
@JsExport
data class KMMECPointJS(val x: BN, val y: BN)
