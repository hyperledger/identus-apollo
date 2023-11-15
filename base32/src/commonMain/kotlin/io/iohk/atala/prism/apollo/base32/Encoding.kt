package io.iohk.atala.prism.apollo.base32

/**
 * Base32 encoding scheme
 *
 * TODO: Figure out a way to put both type with padded version in on scheme
 */
sealed interface Encoding {
    val alphabet: String

    /**
     * Base32 Standard
     */
    object Standard : Encoding {
        override val alphabet: String = "abcdefghijklmnopqrstuvwxyz234567"
    }

    /**
     * Base32 Standard with padding
     */
    object StandardPad : Encoding {
        override val alphabet: String = "abcdefghijklmnopqrstuvwxyz234567"
    }

    /**
     * Base32 Upper
     */
    object Upper : Encoding {
        override val alphabet: String = "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567"
    }

    /**
     * Base32 Upper with padding
     */
    object UpperPad : Encoding {
        override val alphabet: String = "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567"
    }

    /**
     * Base32 Hex
     */
    object Hex : Encoding {
        override val alphabet: String = "0123456789abcdefghijklmnopqrstuvw"
    }

    /**
     * Base32 Hex with padding
     */
    object HexPad : Encoding {
        override val alphabet: String = "0123456789abcdefghijklmnopqrstuvw="
    }

    /**
     * Base32 Hex Upper
     */
    object HexUpper : Encoding {
        override val alphabet: String = "0123456789ABCDEFGHIJKLMNOPQRSTUVW"
    }

    /**
     * Base32 Hex Upper with padding
     */
    object HexUpperPad : Encoding {
        override val alphabet: String = "0123456789ABCDEFGHIJKLMNOPQRSTUVW="
    }
}
