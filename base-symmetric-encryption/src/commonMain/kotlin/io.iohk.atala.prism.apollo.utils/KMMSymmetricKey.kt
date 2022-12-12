package io.iohk.atala.prism.apollo.utils

expect open class KMMSymmetricKey : SymmetricKeyBase64Export {
    companion object : SymmetricKeyBase64Import, IVBase64Import, IVBase64Export, IVGeneration
}
