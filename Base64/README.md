# Apollo - Base64
[![Kotlin](https://img.shields.io/badge/kotlin-1.7.20-blue.svg?logo=kotlin)](http://kotlinlang.org)

Apollo Base64 is Kotlin Multiplatform library containing:

- Standard
- URL safe

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
| macOS X86 64                             | :heavy_check_mark: |
| macOS Arm 64 (Apple Silicon)             | :heavy_check_mark: |
| minGW X86 64                             | :heavy_check_mark: |
| minGW X86 32                             | :heavy_check_mark: | 
| watchOS X86 32                           | :heavy_check_mark: |
| watchOS Arm 64(_32)                      | :heavy_check_mark: |
| watchOS Arm 32                           | :heavy_check_mark: |
| watchOS Simulator Arm 64 (Apple Silicon) | :heavy_check_mark: |
| tvOS X86 64                              | :heavy_check_mark: |
| tvOS Arm 64                              | :heavy_check_mark: |
| tvOS Simulator Arm 64 (Apple Silicon)    | :heavy_check_mark: |
| Linux X86 64                             | :x:                |
| Linux Arm 64                             | :x:                |
| Linux Arm 32                             | :x:                |

## Usage
### Base64 Standard
```kotlin
val helloWorld = "V2VsY29tZSB0byBJT0c=".base64Decoded // "Hello, world!"
println("Welcome to IOG".base64Encoded) // Prints "SGVsbG8sIHdvcmxkIQ=="
```
### Base64 URL
```kotlin
val helloWorld = "V2VsY29tZSB0byBJT0c".base64UrlDecoded // "Hello, world!"
println("Welcome to IOG".base64UrlEncoded) // Prints "SGVsbG8sIHdvcmxkIQ"
```