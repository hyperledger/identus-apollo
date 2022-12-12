package io.iohk.atala.prism.apollo.utils

import java.security.PrivateKey
import java.security.PublicKey

actual open class KMMPrivateKey(val nativeType: PrivateKey)
actual open class KMMPublicKey(val nativeType: PublicKey)
