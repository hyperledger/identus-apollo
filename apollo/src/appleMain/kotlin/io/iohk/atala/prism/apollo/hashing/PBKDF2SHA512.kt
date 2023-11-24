package io.iohk.atala.prism.apollo.hashing

import kotlinx.cinterop.addressOf
import kotlinx.cinterop.convert
import kotlinx.cinterop.usePinned
import platform.CoreCrypto.CCKeyDerivationPBKDF
import platform.CoreCrypto.kCCPBKDF2
import platform.CoreCrypto.kCCPRFHmacAlgSHA512

// TODO: Finish implementation of pbkdf2 function on each platform probably easiest path
actual class PBKDF2SHA512 {
    actual companion object {
        @OptIn(ExperimentalUnsignedTypes::class)
        actual fun derive(
            p: String,
            s: String,
            c: Int,
            dkLen: Int
        ): ByteArray {
            val rounds = c.toUInt()
            val salt = s.encodeToByteArray().toUByteArray()
            val derivedKey = UByteArray(dkLen) // initialize buffer for derived key

            derivedKey.usePinned { derivedKeyPin ->
                salt.usePinned { saltPin ->

                    val result = CCKeyDerivationPBKDF(
                        algorithm = kCCPBKDF2,
                        password = p,
                        passwordLen = p.length.convert(),
                        salt = saltPin.addressOf(0),
                        saltLen = saltPin.get().size.convert(),
                        prf = kCCPRFHmacAlgSHA512,
                        rounds = rounds,
                        derivedKey = derivedKeyPin.addressOf(0),
                        derivedKeyLen = derivedKeyPin.get().size.convert()
                    )

                    if (result != 0) {
                        throw Error("Key derivation failed with error: $result")
                    }
                }
            }

            return derivedKey.toByteArray()
        }
    }
}
