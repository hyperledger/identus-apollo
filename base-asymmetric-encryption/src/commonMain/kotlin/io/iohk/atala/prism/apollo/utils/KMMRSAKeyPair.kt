package io.iohk.atala.prism.apollo.utils

expect final class KMMRSAKeyPair(privateKey: KMMRSAPrivateKey, publicKey: KMMRSAPublicKey) {
    companion object : RSAKeyPairGeneration
}
