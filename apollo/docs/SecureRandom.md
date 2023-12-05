# Package io.iohk.atala.prism.apollo.securerandom


![Atala Prism Logo](../../images/Logo.png)

Apollo Secure Random is Kotlin Multiplatform library to generate secure random bytes

## Supported Targets

| Platform                                 | Supported          |
|------------------------------------------|--------------------|
| iOS x86 64                               | :heavy_check_mark: |
| iOS Arm 64                               | :heavy_check_mark: |
| iOS Arm 32                               | :heavy_check_mark: |
| iOS Simulator Arm 64 (Apple Silicon)     | :heavy_check_mark: |
| JVM                                      | :heavy_check_mark: | 
| Android                                  | :heavy_check_mark: |
| JS Browser                               | :heavy_check_mark: |
| NodeJS Browser                           | :heavy_check_mark: |
| macOS Arm 64 (Apple Silicon)             | :heavy_check_mark: |

## Usage

```kotlin
val iv: ByteArray = SecureRandom().nextBytes(16)
val seed: ByteArray = SecureRandom.generateSeed(10)
```
