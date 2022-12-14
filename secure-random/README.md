# Apollo - Secure Random
[![Kotlin](https://img.shields.io/badge/kotlin-1.7.21-blue.svg?logo=kotlin)](http://kotlinlang.org)

Apollo Secure Random is Kotlin Multiplatform library generate secure random

## Usage
```kotlin
val iv: ByteArray = SecureRandom().nextBytes(16)
val seed: ByteArray = SecureRandom.generateSeed(10)
```