package io.iohk.atala.prism.apollo

@JsExport
public object SHA256DigestCompanion {
    public fun fromHex(string: String): Sha256Digest =
        Sha256Digest.fromHex(string)

    public fun fromBytes(bytes: ByteArray): Sha256Digest =
        Sha256Digest.fromBytes(bytes)
}
