package io.iohk.atala.prism.apollo.utils

import kotlinx.cinterop.ExperimentalForeignApi
import platform.Security.SecKeyRef

@OptIn(ExperimentalForeignApi::class)
actual final class KMMRSAPrivateKey(val nativeType: SecKeyRef)
