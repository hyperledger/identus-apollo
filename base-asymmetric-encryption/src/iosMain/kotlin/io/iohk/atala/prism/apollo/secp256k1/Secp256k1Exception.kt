package io.iohk.atala.prism.apollo.secp256k1

class Secp256k1Exception : RuntimeException {
    constructor() : super()
    constructor(message: String?) : super(message)
}
