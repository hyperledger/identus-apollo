# Apollo - UUID
[![Kotlin](https://img.shields.io/badge/kotlin-1.7.21-blue.svg?logo=kotlin)](http://kotlinlang.org)

Apollo UUID is a Kotlin Multiplatform library for UUID generation with support to the following versions:

- 1
- 3
- 4
- 5

## Usage
### Version 1
```kotlin
val uuid: UUID = UUID.randomUUID1()
val uuidString = uuid.toString()
```

### Version 3
```kotlin
val uuid: UUID = UUID.randomUUID3("hello".encodeToByteArray())
val uuidString = uuid.toString()
```

### Version 4
```kotlin
val uuid: UUID = UUID.randomUUID4() // most used version and default one
val uuidString = uuid.toString()
```

### Version 1
```kotlin
val uuid: UUID = UUID.randomUUID5("hello".encodeToByteArray())
val uuidString = uuid.toString()
```

## TODO
Implement UUID version 2