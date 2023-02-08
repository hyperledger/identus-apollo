// Automatically generated by dukat and then slightly adjusted manually to make it compile
@file:Suppress("ktlint")
// @file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS")
@file:JsModule("elliptic")

/* ktlint-disable */
package io.iohk.atala.prism.apollo.utils.external

import io.iohk.atala.prism.apollo.utils.external.eddsa.KeyPair as _eddsa_KeyPair
import io.iohk.atala.prism.apollo.utils.external.eddsa.KeyPairOptions as _eddsa_KeyPairOptions
import io.iohk.atala.prism.apollo.utils.external.eddsa.Signature as _eddsa_Signature
import org.khronos.webgl.Uint8Array

external var utils: Any

external var rand: Any

external var version: Number

external interface Coordinates {
    var x: String
    var y: String
}

external object curve {
    open class base {
        companion object {
            open class BasePoint {
                fun encode(enc: String): String
            }
        }

        open fun decodePoint(bytes: String): BasePoint
        open fun decodePoint(bytes: Uint8Array): BasePoint
    }
}

open external class ec {
    open var curve: curve.base
    open var n: BN
    open var nh: Any
    open var g: Any
    open var hash: Any
    constructor(options: String)
    constructor(options: PresetCurve)
    open fun keyPair(options: KeyPairOptions): KeyPair
    open fun keyFromPrivate(priv: Uint8Array, enc: String = definedExternally): KeyPair
    open fun keyFromPrivate(priv: Uint8Array): KeyPair
    open fun keyFromPrivate(priv: String, enc: String = definedExternally): KeyPair
    open fun keyFromPrivate(priv: String): KeyPair
    open fun keyFromPrivate(priv: Array<Number>, enc: String = definedExternally): KeyPair
    open fun keyFromPrivate(priv: Array<Number>): KeyPair
    open fun keyFromPrivate(priv: KeyPair, enc: String = definedExternally): KeyPair
    open fun keyFromPrivate(priv: KeyPair): KeyPair
    open fun keyFromPublic(pub: Uint8Array, enc: String = definedExternally): KeyPair
    open fun keyFromPublic(pub: Uint8Array): KeyPair
    open fun keyFromPublic(pub: String, enc: String = definedExternally): KeyPair
    open fun keyFromPublic(pub: String): KeyPair
    open fun keyFromPublic(pub: Array<Number>, enc: String = definedExternally): KeyPair
    open fun keyFromPublic(pub: Array<Number>): KeyPair
    open fun keyFromPublic(pub: Coordinates, enc: String = definedExternally): KeyPair
    open fun keyFromPublic(pub: Coordinates): KeyPair
    open fun keyFromPublic(pub: KeyPair, enc: String = definedExternally): KeyPair
    open fun keyFromPublic(pub: KeyPair): KeyPair
    open fun genKeyPair(options: GenKeyPairOptions = definedExternally): KeyPair
    open fun sign(msg: String, key: KeyPair, enc: String, options: SignOptions = definedExternally): Signature
    open fun sign(msg: String, key: KeyPair, enc: String): Signature
    open fun sign(msg: BN, key: KeyPair, enc: String, options: SignOptions = definedExternally): Signature
    open fun sign(msg: BN, key: KeyPair, enc: String): Signature
    open fun sign(msg: Number, key: KeyPair, enc: String, options: SignOptions = definedExternally): Signature
    open fun sign(msg: Number, key: KeyPair, enc: String): Signature
    open fun sign(msg: Uint8Array, key: KeyPair, enc: String, options: SignOptions = definedExternally): Signature
    open fun sign(msg: Uint8Array, key: KeyPair, enc: String): Signature
    open fun sign(msg: Array<Number>, key: KeyPair, enc: String, options: SignOptions = definedExternally): Signature
    open fun sign(msg: Array<Number>, key: KeyPair, enc: String): Signature
    open fun sign(msg: String, key: KeyPair, options: SignOptions = definedExternally): Signature
    open fun sign(msg: String, key: KeyPair): Signature
    open fun sign(msg: BN, key: KeyPair, options: SignOptions = definedExternally): Signature
    open fun sign(msg: BN, key: KeyPair): Signature
    open fun sign(msg: Number, key: KeyPair, options: SignOptions = definedExternally): Signature
    open fun sign(msg: Number, key: KeyPair): Signature
    open fun sign(msg: Uint8Array, key: KeyPair, options: SignOptions = definedExternally): Signature
    open fun sign(msg: Uint8Array, key: KeyPair): Signature
    open fun sign(msg: Array<Number>, key: KeyPair, options: SignOptions = definedExternally): Signature
    open fun sign(msg: Array<Number>, key: KeyPair): Signature
    open fun sign(msg: String, key: String, enc: String): Signature
    open fun sign(msg: Array<Number>, key: String, enc: String): Signature
    open fun verify(msg: String, signature: Any /* ec.Signature | ec.SignatureOptions | Uint8Array | ReadonlyArray<Number> | String */, key: Any /* Buffer | ec.KeyPair */, enc: String = definedExternally): Boolean
    open fun verify(msg: String, signature: Any /* ec.Signature | ec.SignatureOptions | Uint8Array | ReadonlyArray<Number> | String */, key: Any /* Buffer | ec.KeyPair */): Boolean
    open fun verify(msg: BN, signature: Any /* ec.Signature | ec.SignatureOptions | Uint8Array | ReadonlyArray<Number> | String */, key: Any /* Buffer | ec.KeyPair */, enc: String = definedExternally): Boolean
    open fun verify(msg: BN, signature: Any /* ec.Signature | ec.SignatureOptions | Uint8Array | ReadonlyArray<Number> | String */, key: Any /* Buffer | ec.KeyPair */): Boolean
    open fun verify(msg: Number, signature: Any /* ec.Signature | ec.SignatureOptions | Uint8Array | ReadonlyArray<Number> | String */, key: Any /* Buffer | ec.KeyPair */, enc: String = definedExternally): Boolean
    open fun verify(msg: Number, signature: Any /* ec.Signature | ec.SignatureOptions | Uint8Array | ReadonlyArray<Number> | String */, key: Any /* Buffer | ec.KeyPair */): Boolean
    open fun verify(msg: Uint8Array, signature: Any /* ec.Signature | ec.SignatureOptions | Uint8Array | ReadonlyArray<Number> | String */, key: Any /* Buffer | ec.KeyPair */, enc: String = definedExternally): Boolean
    open fun verify(msg: Uint8Array, signature: Any /* ec.Signature | ec.SignatureOptions | Uint8Array | ReadonlyArray<Number> | String */, key: Any /* Buffer | ec.KeyPair */): Boolean
    open fun verify(msg: Array<Number>, signature: Any /* ec.Signature | ec.SignatureOptions | Uint8Array | ReadonlyArray<Number> | String */, key: Any /* Buffer | ec.KeyPair */, enc: String = definedExternally): Boolean
    open fun verify(msg: Array<Number>, signature: Any /* ec.Signature | ec.SignatureOptions | Uint8Array | ReadonlyArray<Number> | String */, key: Any /* Buffer | ec.KeyPair */): Boolean
    open fun recoverPubKey(msg: String, signature: Any /* ec.Signature | ec.SignatureOptions | Uint8Array | ReadonlyArray<Number> | String */, j: Number, enc: String = definedExternally): Any
    open fun recoverPubKey(msg: String, signature: Any /* ec.Signature | ec.SignatureOptions | Uint8Array | ReadonlyArray<Number> | String */, j: Number): Any
    open fun recoverPubKey(msg: BN, signature: Any /* ec.Signature | ec.SignatureOptions | Uint8Array | ReadonlyArray<Number> | String */, j: Number, enc: String = definedExternally): Any
    open fun recoverPubKey(msg: BN, signature: Any /* ec.Signature | ec.SignatureOptions | Uint8Array | ReadonlyArray<Number> | String */, j: Number): Any
    open fun recoverPubKey(msg: Number, signature: Any /* ec.Signature | ec.SignatureOptions | Uint8Array | ReadonlyArray<Number> | String */, j: Number, enc: String = definedExternally): Any
    open fun recoverPubKey(msg: Number, signature: Any /* ec.Signature | ec.SignatureOptions | Uint8Array | ReadonlyArray<Number> | String */, j: Number): Any
    open fun recoverPubKey(msg: Uint8Array, signature: Any /* ec.Signature | ec.SignatureOptions | Uint8Array | ReadonlyArray<Number> | String */, j: Number, enc: String = definedExternally): Any
    open fun recoverPubKey(msg: Uint8Array, signature: Any /* ec.Signature | ec.SignatureOptions | Uint8Array | ReadonlyArray<Number> | String */, j: Number): Any
    open fun recoverPubKey(msg: Array<Number>, signature: Any /* ec.Signature | ec.SignatureOptions | Uint8Array | ReadonlyArray<Number> | String */, j: Number, enc: String = definedExternally): Any
    open fun recoverPubKey(msg: Array<Number>, signature: Any /* ec.Signature | ec.SignatureOptions | Uint8Array | ReadonlyArray<Number> | String */, j: Number): Any
    open fun getKeyRecoveryParam(e: Error?, signature: Signature, Q: BN, enc: String = definedExternally): Number
    open fun getKeyRecoveryParam(e: Error?, signature: Signature, Q: BN): Number
    open fun getKeyRecoveryParam(e: Error?, signature: SignatureOptions, Q: BN, enc: String = definedExternally): Number
    open fun getKeyRecoveryParam(e: Error?, signature: SignatureOptions, Q: BN): Number
    open fun getKeyRecoveryParam(e: Error?, signature: Uint8Array, Q: BN, enc: String = definedExternally): Number
    open fun getKeyRecoveryParam(e: Error?, signature: Uint8Array, Q: BN): Number
    open fun getKeyRecoveryParam(e: Error?, signature: Array<Number>, Q: BN, enc: String = definedExternally): Number
    open fun getKeyRecoveryParam(e: Error?, signature: Array<Number>, Q: BN): Number
    open fun getKeyRecoveryParam(e: Error?, signature: String, Q: BN, enc: String = definedExternally): Number
    open fun getKeyRecoveryParam(e: Error?, signature: String, Q: BN): Number
    interface GenKeyPairOptions {
        var pers: Any?
            get() = definedExternally
            set(value) = definedExternally
        var entropy: Any
        var persEnc: String?
            get() = definedExternally
            set(value) = definedExternally
        var entropyEnc: String?
            get() = definedExternally
            set(value) = definedExternally
    }
    interface SignOptions {
        var pers: Any?
            get() = definedExternally
            set(value) = definedExternally
        var persEnc: String?
            get() = definedExternally
            set(value) = definedExternally
        var canonical: Boolean?
            get() = definedExternally
            set(value) = definedExternally
        var k: Any?
            get() = definedExternally
            set(value) = definedExternally
    }
    interface `T$1` {
        var result: Boolean
        var reason: String
    }
    open class KeyPair(ec: ec, options: KeyPairOptions) {
        open var ec: ec
        open fun validate(): `T$1`
        open fun getPublic(compact: Boolean, enc: String /* "hex" | "array" */): dynamic /* String | Array */
        open fun getPublic(enc: String /* "hex" | "array" */): dynamic /* String | Array */
        open fun getPublic(): base.BasePoint
        open fun getPrivate(enc: String /* "hex" */): String
        open fun getPrivate(): BN
        open fun derive(pub: base.BasePoint): BN
        open fun sign(msg: String, enc: String, options: SignOptions = definedExternally): Signature
        open fun sign(msg: String, enc: String): Signature
        open fun sign(msg: BN, enc: String, options: SignOptions = definedExternally): Signature
        open fun sign(msg: BN, enc: String): Signature
        open fun sign(msg: Number, enc: String, options: SignOptions = definedExternally): Signature
        open fun sign(msg: Number, enc: String): Signature
        open fun sign(msg: Uint8Array, enc: String, options: SignOptions = definedExternally): Signature
        open fun sign(msg: Uint8Array, enc: String): Signature
        open fun sign(msg: Array<Number>, enc: String, options: SignOptions = definedExternally): Signature
        open fun sign(msg: Array<Number>, enc: String): Signature
        open fun sign(msg: String, options: SignOptions = definedExternally): Signature
        open fun sign(msg: String): Signature
        open fun sign(msg: BN, options: SignOptions = definedExternally): Signature
        open fun sign(msg: BN): Signature
        open fun sign(msg: Number, options: SignOptions = definedExternally): Signature
        open fun sign(msg: Number): Signature
        open fun sign(msg: Uint8Array, options: SignOptions = definedExternally): Signature
        open fun sign(msg: Uint8Array): Signature
        open fun sign(msg: Array<Number>, options: SignOptions = definedExternally): Signature
        open fun sign(msg: Array<Number>): Signature
        open fun verify(msg: String, signature: Any /* ec.Signature | ec.SignatureOptions | Uint8Array | ReadonlyArray<Number> | String */): Boolean
        open fun verify(msg: BN, signature: Any /* ec.Signature | ec.SignatureOptions | Uint8Array | ReadonlyArray<Number> | String */): Boolean
        open fun verify(msg: Number, signature: Any /* ec.Signature | ec.SignatureOptions | Uint8Array | ReadonlyArray<Number> | String */): Boolean
        open fun verify(msg: Uint8Array, signature: Any /* ec.Signature | ec.SignatureOptions | Uint8Array | ReadonlyArray<Number> | String */): Boolean
        open fun verify(msg: Array<Number>, signature: Any /* ec.Signature | ec.SignatureOptions | Uint8Array | ReadonlyArray<Number> | String */): Boolean
        open fun inspect(): String

        companion object {
            fun fromPublic(ec: ec, pub: String, enc: String = definedExternally): KeyPair
            fun fromPublic(ec: ec, pub: Coordinates, enc: String = definedExternally): KeyPair
            fun fromPublic(ec: ec, pub: KeyPair, enc: String = definedExternally): KeyPair
            fun fromPrivate(ec: ec, priv: String, enc: String = definedExternally): KeyPair
            fun fromPrivate(ec: ec, priv: KeyPair, enc: String = definedExternally): KeyPair
        }
    }
    open class Signature {
        open var r: BN
        open var s: BN
        open var recoveryParam: Number?
        open fun toDER(enc: String? = definedExternally): Any
    }
    interface SignatureOptions {
        var r: dynamic /* String | BN | Number | Buffer | Uint8Array | ReadonlyArray<Number> */
            get() = definedExternally
            set(value) = definedExternally
        var s: dynamic /* String | BN | Number | Buffer | Uint8Array | ReadonlyArray<Number> */
            get() = definedExternally
            set(value) = definedExternally
        var recoveryParam: Number?
            get() = definedExternally
            set(value) = definedExternally
    }
    interface KeyPairOptions {
        var privEnc: String?
            get() = definedExternally
            set(value) = definedExternally
        var pubEnc: String?
            get() = definedExternally
            set(value) = definedExternally
    }
}

open external class eddsa(name: String /* "ed25519" */) {
    open var curve: edwards
    open fun sign(message: String, secret: String): _eddsa_Signature
    open fun verify(message: String, sig: String, pub: Any /* String | Buffer | eddsa.Point | eddsa.KeyPair */): Boolean
    open fun verify(message: String, sig: _eddsa_Signature, pub: Any /* String | Buffer | eddsa.Point | eddsa.KeyPair */): Boolean
    open fun hashInt(): BN
    open fun keyFromPublic(pub: String): _eddsa_KeyPair
    open fun keyFromPublic(pub: _eddsa_KeyPair): _eddsa_KeyPair
    open fun keyFromPublic(pub: base.BasePoint): _eddsa_KeyPair
    open fun keyFromSecret(secret: String): _eddsa_KeyPair
    open fun makeSignature(sig: _eddsa_Signature): _eddsa_Signature
    open fun makeSignature(sig: String): _eddsa_Signature
    open fun decodePoint(bytes: String): base.BasePoint
    open fun decodeInt(bytes: String): BN
    open fun decodeInt(bytes: BN): BN
    open fun decodeInt(bytes: Number): BN
    open fun decodeInt(bytes: Uint8Array): BN
    open fun decodeInt(bytes: Array<Number>): BN
    open fun isPoint(param_val: Any): Boolean
    open class Signature {
        constructor(eddsa: eddsa, sig: _eddsa_Signature)
        constructor(eddsa: eddsa, sig: String)
        open fun toHex(): String
    }
    open class KeyPair(eddsa: eddsa, params: _eddsa_KeyPairOptions) {
        open fun sign(message: String): _eddsa_Signature
        open fun verify(message: String, sig: _eddsa_Signature): Boolean
        open fun verify(message: String, sig: String): Boolean
        open fun getSecret(enc: String /* "hex" */): String
        open fun getPublic(enc: String /* "hex" */): String

        companion object {
            fun fromPublic(eddsa: eddsa, pub: String): _eddsa_KeyPair
            fun fromSecret(eddsa: eddsa, secret: String): _eddsa_KeyPair
        }
    }
    interface KeyPairOptions {
        var pub: dynamic /* Buffer | Point */
            get() = definedExternally
            set(value) = definedExternally
    }
}
/* ktlint-disable */
