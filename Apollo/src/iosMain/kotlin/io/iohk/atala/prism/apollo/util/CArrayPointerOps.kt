package io.iohk.atala.prism.apollo.util

import kotlinx.cinterop.*

internal fun CArrayPointer<UByteVar>.toUByteArray(length: Int): UByteArray = UByteArray(length) {
    this[it]
}

internal fun UByteArray.toCArrayPointer(memScope: MemScope): CArrayPointer<UByteVar> {
    val array = memScope.allocArray<UByteVar>(this.size)
    val arrayPtr = array.getPointer(memScope)
    for (i in this.indices) arrayPtr[i] = this[i]
    return array
}
