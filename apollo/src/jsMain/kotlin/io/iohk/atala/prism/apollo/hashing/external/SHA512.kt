@file:Suppress("INTERFACE_WITH_SUPERCLASS", "OVERRIDING_FINAL_MEMBER", "RETURN_TYPE_MISMATCH_ON_OVERRIDE", "CONFLICTING_OVERLOADS")
// Automatically generated by dukat and then slightly adjusted manually to make it compile
@file:JsModule("@noble/hashes/sha512")
/* ktlint-disable */

package io.iohk.atala.prism.apollo.hashing.external

import org.khronos.webgl.* // ktlint-disable no-wildcard-imports
import kotlin.js.*

/**
 * The `Hashe` class is an open external class that represents a hash function. It provides methods for updating the hash state, computing the hash digest, destroying the hash object
 *, and cloning the hash object.
 *
 * @param T the type parameter which must extend `Hashe<T>`
 * @property blockLen the length of the hash block
 * @property outputLen the length of the hash output
 *
 * @constructor Creates a new instance of the `Hashe` class.
 */
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
/**
 * SHA2 is a class that provides methods for computing SHA-2 hash functions.
 *
 * @param T the type of the subclass that extends SHA2
 * @property blockLen the length of the hash block
 * @property outputLen the length of the hash output
 * @property padOffset the padding offset
 * @property isLE indicates whether the byte order is little endian
 * @constructor Creates a SHA2 instance with the specified parameters.
 */
open external class SHA2<T : SHA2<T>>(blockLen: Number, outputLen: Number, padOffset: Number, isLE: Boolean) :
    Hashe<T> {
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

/**
 * The SHA512 class is an implementation of the SHA-512 hash algorithm.
 */
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

/**
 * The SHA512 class provides a platform-specific implementation for generating SHA-512 hash.
 */
external object sha512 {
    @nativeInvoke
    operator fun invoke(message: Any): Uint8Array
    var outputLen: Number
    var blockLen: Number
    fun create(): Any
}

/**
 * A class representing the SHA-512*/
external object sha512_224 {
    @nativeInvoke
    operator fun invoke(message: Any): Uint8Array
    var outputLen: Number
    var blockLen: Number
    fun create(): Any
}

/**
 * The `sha512_256` class represents an object that can compute SHA-512*/
external object sha512_256 {
    @nativeInvoke
    operator fun invoke(message: Any): Uint8Array
    var outputLen: Number
    var blockLen: Number
    fun create(): Any
}

/**
 * The `sha384` class provides functions for computing the SHA-384 hash.
 */
external object sha384 {
    @nativeInvoke
    operator fun invoke(message: Any): Uint8Array
    var outputLen: Number
    var blockLen: Number
    fun create(): Any
}
