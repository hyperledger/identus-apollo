package io.iohk.atala.prism.apollo.utils

expect open class KMMPrivateKey
expect open class KMMPublicKey

final class KMMKeyPair(val publicKey: KMMPublicKey, val privateKey: KMMPrivateKey)
