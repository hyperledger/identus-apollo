package io.iohk.atala.prism.apollo.derivation

@JsExport
public object DerivationAxisCompanion {
    public fun normal(num: Int): DerivationAxis =
        DerivationAxis.normal(num)

    public fun hardened(num: Int): DerivationAxis =
        DerivationAxis.hardened(num)
}
