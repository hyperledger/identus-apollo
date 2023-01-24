package io.iohk.atala.prism.apollo.utils

expect final class KMMKeyPair(privateKey: KMMPrivateKey, publicKey: KMMPublicKey) {
    companion object : RSAKeyPairGeneration
}
