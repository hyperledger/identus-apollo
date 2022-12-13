package io.iohk.atala.prism.apollo.utils

import platform.Security.SecKeyRef

actual open class KMMPrivateKey(val nativeType: SecKeyRef)
actual open class KMMPublicKey(val nativeType: SecKeyRef)
