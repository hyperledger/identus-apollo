package io.iohk.atala.prism.apollo.utils

import java.security.PrivateKey
import java.security.PublicKey
import javax.crypto.SecretKey

actual open class KMMPrivateKey(val nativeType: PrivateKey)
actual open class KMMPublicKey(val nativeType: PublicKey)
actual open class KMMSymmetricKey(val nativeType: SecretKey)
