@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS")
// Automatically generated by dukat and then slightly adjusted manually to make it compile
@file:JsModule("@noble/hashes/sha512")
/* ktlint-disable */

package io.iohk.atala.prism.apollo.hashing.external

import org.khronos.webgl.* // ktlint-disable no-wildcard-imports
import kotlin.js.*

open external class Hashe<T : Hashe<T>> {
    open var blockLen: Number
    open var outputLen: Number
    open fun update(buf: Uint8Array): Hashe<T> /* this */
    open fun update(buf: String): Hashe<T> /* this */
    open fun digestInto(buf: Uint8Array)
    open fun digest(): Uint8Array
    open fun destroy()
    open fun _cloneInto(to: T = definedExternally): T
    open fun clone(): T
}
open external class SHA2<T : SHA2<T>>(blockLen: Number, outputLen: Number, padOffset: Number, isLE: Boolean) : Hashe<T> {
    override var blockLen: Number
    override var outputLen: Number
    open var padOffset: Number
    open var isLE: Boolean
    open fun process(buf: DataView, offset: Number)
    open fun get(): Array<Number>
    open fun set(vararg args: Number)
    override fun destroy()
    open fun roundClean()
    open var buffer: Uint8Array
    open var view: DataView
    open var finished: Boolean
    open var length: Number
    open var pos: Number
    open var destroyed: Boolean
    override fun update(data: Uint8Array): SHA2<T> /* this */
    override fun update(data: String): SHA2<T> /* this */
    override fun digestInto(out: Uint8Array)
    override fun digest(): Uint8Array
    override fun _cloneInto(to: T): T
}

open external class SHA512 : SHA2<SHA512> {
    open var Ah: Number
    open var Al: Number
    open var Bh: Number
    open var Bl: Number
    open var Ch: Number
    open var Cl: Number
    open var Dh: Number
    open var Dl: Number
    open var Eh: Number
    open var El: Number
    open var Fh: Number
    open var Fl: Number
    open var Gh: Number
    open var Gl: Number
    open var Hh: Number
    open var Hl: Number
    override fun get(): dynamic /* JsTuple<Number, Number, Number, Number, Number, Number, Number, Number, Number, Number, Number, Number, Number, Number, Number, Number> */
    open fun set(Ah: Number, Al: Number, Bh: Number, Bl: Number, Ch: Number, Cl: Number, Dh: Number, Dl: Number, Eh: Number, El: Number, Fh: Number, Fl: Number, Gh: Number, Gl: Number, Hh: Number, Hl: Number)
    override fun process(view: DataView, offset: Number)
    override fun roundClean()
    override fun destroy()
}

external object sha512 {
    @nativeInvoke
    operator fun invoke(message: Any): Uint8Array
    var outputLen: Number
    var blockLen: Number
    fun create(): Any
}

external object sha512_224 {
    @nativeInvoke
    operator fun invoke(message: Any): Uint8Array
    var outputLen: Number
    var blockLen: Number
    fun create(): Any
}

external object sha512_256 {
    @nativeInvoke
    operator fun invoke(message: Any): Uint8Array
    var outputLen: Number
    var blockLen: Number
    fun create(): Any
}

external object sha384 {
    @nativeInvoke
    operator fun invoke(message: Any): Uint8Array
    var outputLen: Number
    var blockLen: Number
    fun create(): Any
}