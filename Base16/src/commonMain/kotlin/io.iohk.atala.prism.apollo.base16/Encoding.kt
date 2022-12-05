package io.iohk.atala.prism.apollo.base16

/**
 * Base16 encoding scheme
 */
sealed interface Encoding {
    val alphabet: String

    /**
     * Base16 Standard
     */
    object Standard : Encoding {
        override val alphabet: String = "0123456789abcdef"
    }

    /**
     * Base16 Upper
     */
    object Upper : Encoding {
        override val alphabet: String = "0123456789ABCDEF"
    }
}
