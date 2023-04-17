package io.iohk.atala.prism.apollo.utils

interface X25519KeyPairGeneration {
    fun generateX25519KeyPair(): KMMX25519KeyPair
}
