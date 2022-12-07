# Apollo - Multibase
[![Kotlin](https://img.shields.io/badge/kotlin-1.7.20-blue.svg?logo=kotlin)](http://kotlinlang.org)

Apollo Multibase is Kotlin Multiplatform library of implementation of [multibase](https://github.com/multiformats/multibase) -self identifying base encodings- in KMM

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
```kotlin
val input: String = "..."
val decode = MultiBase.decode(input)
val inputByteArray: ByteArray = ....
val encode = MultiBase.encode(MultiBase.Base.BASE64, inputByteArray)
```

## More info
The current multibase table is [here](multibase.csv):

```
encoding,          code, description,                                                  status,           implemented
identity,          0x00, 8-bit binary (encoder and decoder keeps data unmodified),     default           No
base2,             0,    binary (01010101),                                            candidate         No
base8,             7,    octal,                                                        draft             No
base10,            9,    decimal,                                                      draft             No
base16,            f,    hexadecimal,                                                  default           Yes
base16upper,       F,    hexadecimal,                                                  default           Yes
base32hex,         v,    rfc4648 case-insensitive - no padding - highest char,         candidate         Yes
base32hexupper,    V,    rfc4648 case-insensitive - no padding - highest char,         candidate         Yes
base32hexpad,      t,    rfc4648 case-insensitive - with padding,                      candidate         Yes
base32hexpadupper, T,    rfc4648 case-insensitive - with padding,                      candidate         Yes
base32,            b,    rfc4648 case-insensitive - no padding,                        default           Yes
base32upper,       B,    rfc4648 case-insensitive - no padding,                        default           Yes
base32pad,         c,    rfc4648 case-insensitive - with padding,                      candidate         Yes
base32padupper,    C,    rfc4648 case-insensitive - with padding,                      candidate         Yes
base32z,           h,    z-base-32 (used by Tahoe-LAFS),                               draft             No
base36,            k,    base36 [0-9a-z] case-insensitive - no padding,                draft             No
base36upper,       K,    base36 [0-9a-z] case-insensitive - no padding,                draft             No
base58btc,         z,    base58 bitcoin,                                               default           Yes
base58flickr,      Z,    base58 flicker,                                               candidate         Yes
base64,            m,    rfc4648 no padding,                                           default           Yes
base64pad,         M,    rfc4648 with padding - MIME encoding,                         candidate         Yes
base64url,         u,    rfc4648 no padding,                                           default           Yes
base64urlpad,      U,    rfc4648 with padding,                                         default           Yes
proquint,          p,    PRO-QUINT https://arxiv.org/html/0901.4016,                   draft             No
base256emoji,      🚀,    base256 with custom alphabet using variable-sized-codepoints, draft            No
```