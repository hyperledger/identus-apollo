package io.iohk.atala.prism.apollo.aes

import io.iohk.atala.prism.apollo.utils.NativeTypeInterface

expect enum class KAESAlgorithm : NativeTypeInterface<KAESAlgorithmNativeType> {
    AES_128,
    AES_192,
    AES_256;
}

fun KAESAlgorithm.keySize(): Int {
    return when (this) {
        KAESAlgorithm.AES_128 -> 128
        KAESAlgorithm.AES_192 -> 192
        KAESAlgorithm.AES_256 -> 256
        else -> throw Exception("This line should never be reached")
    }
}
