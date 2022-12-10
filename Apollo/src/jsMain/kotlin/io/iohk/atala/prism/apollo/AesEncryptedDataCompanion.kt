package io.iohk.atala.prism.apollo

internal object AesEncryptedDataCompanion {
    fun fromCombined(bytes: ByteArray): AesEncryptedData =
        AesEncryptedData.fromCombined(bytes)
}
