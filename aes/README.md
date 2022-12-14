# Apollo - AES
[![Kotlin](https://img.shields.io/badge/kotlin-1.7.21-blue.svg?logo=kotlin)](http://kotlinlang.org)

Apollo AES is Kotlin Multiplatform library for AES encryption & decryption with the following supports.

## Supported Key Size:

- 128
- 192
- 256

## Supported Block Mode:

- ECB
- CBC
- CFB
- CFB8
- CTR
- GCM
- OFB
- RC4

## Supported Padding:

- No Padding
- PKCS5
- PKCS7

## Usage
```kotlin
val text = "Welcome to IOG!"
val algo = KAESAlgorithm.AES_256
val key = AES.createRandomAESKey(algo)
val aes = AES(
    algo,
    KAESBlockMode.GCM,
    KAESPadding.No_Padding,
    key,
    KMMSymmetricKey.createRandomIV(16)
)
val encryptedBytes = aes.encrypt(text.encodeToByteArray())
val decryptedBytes = aes.decrypt(encryptedBytes)
```