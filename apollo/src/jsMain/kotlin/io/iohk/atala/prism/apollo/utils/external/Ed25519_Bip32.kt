package io.iohk.atala.prism.apollo.utils.external

external interface XPrvWrapper {
    fun public(): ByteArray

    fun derive(index: Any): XPrvWrapper

    fun extended_secret_key(): ByteArray

    fun chain_code(): ByteArray

    var from_nonextended_noforce: (js_bytes: ByteArray, js_chain_code: ByteArray) -> XPrvWrapper

    var from_extended_and_chaincode: (js_bytes: ByteArray, js_chain_code: ByteArray) -> XPrvWrapper
}

external interface ed25519_bip32_export {
    var XPrvWrapper: XPrvWrapper
}

@JsModule("ed25519_bip32_export")
external val ed25519_bip32: ed25519_bip32_export
