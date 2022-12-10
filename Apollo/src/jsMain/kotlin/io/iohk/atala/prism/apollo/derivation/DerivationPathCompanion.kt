package io.iohk.atala.prism.apollo.derivation

@JsExport
public object DerivationPathCompanion {
    public fun empty(): DerivationPath =
        DerivationPath.empty()

    public fun fromPath(path: String): DerivationPath =
        DerivationPath.fromPath(path)
}
