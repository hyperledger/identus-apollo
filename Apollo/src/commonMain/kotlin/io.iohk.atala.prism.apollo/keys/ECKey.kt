package io.iohk.atala.prism.apollo.keys

import io.iohk.atala.prism.apollo.Encodable
import kotlin.js.JsExport

@JsExport
public abstract class ECKey : Encodable {
    override fun hashCode(): Int {
        return getEncoded().hashCode()
    }

    override fun equals(other: Any?): Boolean {
        return when (other) {
            is ECKey -> getEncoded().contentEquals(other.getEncoded())
            else -> false
        }
    }
}
