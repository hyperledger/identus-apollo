package io.iohk.atala.prism.apollo.hashing.external

@JsModule("hash.js")
internal external val hash: Hash

internal external interface BlockHash<T> {
    var hmacStrength: Number
    var padLength: Number
    var endian: String /* "big" | "little" */
}

internal external interface MessageDigest<T> {
    var blockSize: Number
    var outSize: Number
    fun update(msg: Any, enc: String /* "hex" */ = definedExternally): T
    fun digest(): Array<Number>
    fun digest(enc: String /* "hex" */): String
}

internal external interface Hash {
    var hmac: HmacConstructor
    var ripemd: RipemdSet
    var ripemd160: Ripemd160Constructor
    var sha: ShaSet
    var sha1: Sha1Constructor
    var sha224: Sha224Constructor
    var sha256: Sha256Constructor
    var sha384: Sha384Constructor
    var sha512: Sha512Constructor
    var utils: Utils
}

internal external interface Utils {
    fun toArray(msg: Any, enc: String /* "hex" */): Array<Number>
    fun toHex(msg: Any): String
}

internal external interface RipemdSet {
    var ripemd160: Ripemd160Constructor
}

internal external interface ShaSet {
    var sha1: Sha1Constructor
    var sha224: Sha224Constructor
    var sha256: Sha256Constructor
    var sha384: Sha384Constructor
    var sha512: Sha512Constructor
}

internal external interface HmacConstructor {
    @nativeInvoke
    operator fun invoke(hash: BlockHash<Any>, key: Any, enc: String /* "hex" */ = definedExternally): Hmac
}

internal external interface Ripemd160Constructor {
    @nativeInvoke
    operator fun invoke(): Ripemd160
}

internal external interface Sha1Constructor {
    @nativeInvoke
    operator fun invoke(): Sha1
}

internal external interface Sha224Constructor {
    @nativeInvoke
    operator fun invoke(): Sha224
}

internal external interface Sha256Constructor {
    @nativeInvoke
    operator fun invoke(): Sha256
}

internal external interface Sha384Constructor {
    @nativeInvoke
    operator fun invoke(): Sha384
}

internal external interface Sha512Constructor {
    @nativeInvoke
    operator fun invoke(): Sha512
}

internal external interface Hmac : MessageDigest<Hmac> {
    override var blockSize: Number /* 512 */
    override var outSize: Number /* 160 */
}

internal external interface Ripemd160 : BlockHash<Ripemd160>, MessageDigest<Ripemd160> {
    override var blockSize: Number /* 512 */
    override var hmacStrength: Number /* 192 */
    override var outSize: Number /* 160 */
    override var padLength: Number /* 64 */
    override var endian: String /* "little" */
}

internal external interface Sha1 : BlockHash<Sha1>, MessageDigest<Sha1> {
    override var blockSize: Number /* 512 */
    override var hmacStrength: Number /* 80 */
    override var outSize: Number /* 160 */
    override var padLength: Number /* 64 */
    override var endian: String /* "big" */
}

internal external interface Sha224 : BlockHash<Sha224>, MessageDigest<Sha224> {
    override var blockSize: Number /* 512 */
    override var hmacStrength: Number /* 192 */
    override var outSize: Number /* 224 */
    override var padLength: Number /* 64 */
    override var endian: String /* "big" */
}

internal external interface Sha256 : BlockHash<Sha256>, MessageDigest<Sha256> {
    override var blockSize: Number /* 512 */
    override var hmacStrength: Number /* 192 */
    override var outSize: Number /* 256 */
    override var padLength: Number /* 64 */
    override var endian: String /* "big" */
}

internal external interface Sha384 : BlockHash<Sha384>, MessageDigest<Sha384> {
    override var blockSize: Number /* 1024 */
    override var hmacStrength: Number /* 192 */
    override var outSize: Number /* 384 */
    override var padLength: Number /* 128 */
    override var endian: String /* "big" */
}

internal external interface Sha512 : BlockHash<Sha512>, MessageDigest<Sha512> {
    override var blockSize: Number /* 1024 */
    override var hmacStrength: Number /* 192 */
    override var outSize: Number /* 512 */
    override var padLength: Number /* 128 */
    override var endian: String /* "big" */
}
