package io.iohk.atala.prism.apollo.utils

import kotlinx.cinterop.CArrayPointer
import kotlinx.cinterop.MemScope
import kotlinx.cinterop.UByteVar
import kotlinx.cinterop.allocArray
import kotlinx.cinterop.get
import kotlinx.cinterop.set

@OptIn(ExperimentalUnsignedTypes::class)
fun CArrayPointer<UByteVar>.toUByteArray(length: Int): UByteArray = UByteArray(length) {
    this[it]
}

@OptIn(ExperimentalUnsignedTypes::class)
fun UByteArray.toCArrayPointer(memScope: MemScope): CArrayPointer<UByteVar> {
    val array = memScope.allocArray<UByteVar>(this.size)
    val arrayPtr = array.getPointer(memScope)
    for (i in this.indices) arrayPtr[i] = this[i]
    return array
}
