package io.iohk.atala.prism.apollo.utils

import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPrivateKey

actual open class KMMECPrivateKey(val nativeValue: BCECPrivateKey)
