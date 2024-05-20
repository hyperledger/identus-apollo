package org.hyperledger.identus.apollo.utils

import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

/**
 * Representation of an EC curve point
 */
@OptIn(ExperimentalJsExport::class)
@JsExport
data class KMMECPoint(val x: ByteArray, val y: ByteArray) {
    /**
     * Checks if the given object is equal to this KMMECPoint.
     *
     * @param other The object to compare with this KMMECPoint.
     * @return true if the given object is equal to this KMMECPoint, false otherwise.
     */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as KMMECPoint

        if (!x.contentEquals(other.x)) return false
        if (!y.contentEquals(other.y)) return false

        return true
    }

    /**
     * Calculates the hash code for this KMMECPoint object.
     *
     * The hash code is calculated based on the content of the `x` and `y` properties.
     * If two KMMECPoint objects have the same `x` and `y` values, their hash codes will be equal.
     * The algorithm used for calculating the hash code is as follows:
     * - The hash code of the `x` property is computed using `contentHashCode()`.
     * - The hash code of the `y` property is computed using `contentHashCode()`.
     * - The final hash code is calculated by multiplying the hash code of `x` by 31 and adding the hash code of `y` to it.
     *
     * @return The hash code for this KMMECPoint object.
     */
    override fun hashCode(): Int {
        var result = x.contentHashCode()
        result = 31 * result + y.contentHashCode()
        return result
    }
}
