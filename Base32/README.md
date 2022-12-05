# Apollo - Base32
[![Kotlin](https://img.shields.io/badge/kotlin-1.7.20-blue.svg?logo=kotlin)](http://kotlinlang.org)

Apollo Base32 is Kotlin Multiplatform library containing:

- Standard
- Upper
- Hex
- Hex Upper

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
### Base32 Standard
```kotlin
val text = "k5swyy3pnvssa5dpebeu6rzb".base32Decoded // "Welcome to IOG!"
println("Welcome to IOG!".base32Encoded) // Prints "k5swyy3pnvssa5dpebeu6rzb"
```
### Base32 Standard with padding
```kotlin
val text = "k5swyy3pnvssa5dpebeu6rzb".base32PadDecoded // "Welcome to IOG!"
println("Welcome to IOG!".base32PadEncoded) // Prints "k5swyy3pnvssa5dpebeu6rzb"
```
### Base32 Upper
```kotlin
val text = "K5SWYY3PNVSSA5DPEBEU6RZB".base32UpperDecoded // "Welcome to IOG!"
println("Welcome to IOG!".base32UpperEncoded) // Prints "K5SWYY3PNVSSA5DPEBEU6RZB"
```
### Base32 Upper with padding
```kotlin
val text = "K5SWYY3PNVSSA5DPEBEU6RZB".base32UpperPadDecoded // "Welcome to IOG!"
println("Welcome to IOG!".base32UpperPadEncoded) // Prints "K5SWYY3PNVSSA5DPEBEU6RZB"
```
### Base32 Hex
```kotlin
// Hex value of "Welcome to IOG!" in hex
val value = byteArrayOf(1, 17, -107, -115, -107, -71, -47, -55, -123, -79, -91, -23, -108, -127, -107, -39, -107, -55, -27, -47, -95, -91, -71, -100, -124, -124, -124)
val text = value.base32HexDecoded // "Welcome to IOG!"
println("Welcome to IOG!".base32HexEncoded) // Prints "8him6pbeehp62r39f9ii0pbmclp7it38d5n6e89144"
```
### Base32 Hex with padding
```kotlin
// Hex value of "Welcome to IOG!" in hex
val value = byteArrayOf(1, 17, -107, -115, -107, -71, -47, -55, -123, -79, -91, -23, -108, -127, -107, -39, -107, -55, -27, -47, -95, -91, -71, -100, -124, -124, -124)
val text = value.base32HexPadDecoded // "Welcome to IOG!"
println("Welcome to IOG!".base32HexPadEncoded) // Prints "8him6pbeehp62r39f9ii0pbmclp7it38d5n6e89144"
```
### Base32 Hex Upper
```kotlin
// Hex value of "Welcome to IOG!" in hex
val value = byteArrayOf(1, 17, -107, -115, -107, -71, -47, -55, -123, -79, -91, -23, -108, -127, -107, -39, -107, -55, -27, -47, -95, -91, -71, -100, -124, -124, -124)
val text = value.base32HexUpperDecoded // "Welcome to IOG!"
println("Welcome to IOG!".base32HexUpperEncoded) // Prints ""
```

### Base32 Hex Upper with padding
```kotlin
// Hex value of "Welcome to IOG!" in hex
val value = byteArrayOf(1, 17, -107, -115, -107, -71, -47, -55, -123, -79, -91, -23, -108, -127, -107, -39, -107, -55, -27, -47, -95, -91, -71, -100, -124, -124, -124)
val text = value.base32HexUpperPadDecoded // "Welcome to IOG!"
println("Welcome to IOG!".base32HexUpperPadEncoded) // Prints ""
```