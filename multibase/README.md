# Apollo - Multibase

[![Kotlin](https://img.shields.io/badge/kotlin-1.9.20-blue.svg?logo=kotlin)](http://kotlinlang.org)

![android](https://camo.githubusercontent.com/b1d9ad56ab51c4ad1417e9a5ad2a8fe63bcc4755e584ec7defef83755c23f923/687474703a2f2f696d672e736869656c64732e696f2f62616467652f706c6174666f726d2d616e64726f69642d3645444238442e7376673f7374796c653d666c6174)
![apple-silicon](https://camo.githubusercontent.com/a92c841ffd377756a144d5723ff04ecec886953d40ac03baa738590514714921/687474703a2f2f696d672e736869656c64732e696f2f62616467652f737570706f72742d2535424170706c6553696c69636f6e2535442d3433424246462e7376673f7374796c653d666c6174)
![ios](https://camo.githubusercontent.com/1fec6f0d044c5e1d73656bfceed9a78fd4121b17e82a2705d2a47f6fd1f0e3e5/687474703a2f2f696d672e736869656c64732e696f2f62616467652f706c6174666f726d2d696f732d4344434443442e7376673f7374796c653d666c6174)
![jvm](https://camo.githubusercontent.com/700f5dcd442fd835875568c038ae5cd53518c80ae5a0cf12c7c5cf4743b5225b/687474703a2f2f696d672e736869656c64732e696f2f62616467652f706c6174666f726d2d6a766d2d4442343133442e7376673f7374796c653d666c6174)
![js](https://camo.githubusercontent.com/3e0a143e39915184b54b60a2ecedec75e801f396d34b5b366c94ec3604f7e6bd/687474703a2f2f696d672e736869656c64732e696f2f62616467652f706c6174666f726d2d6a732d4638444235442e7376673f7374796c653d666c6174)
![getNode-js](https://camo.githubusercontent.com/d08fda729ceebcae0f23c83499ca8f06105350f037661ac9a4cc7f58edfdbca9/68747470733a2f2f696d672e736869656c64732e696f2f62616467652f706c6174666f726d2d6e6f64656a732d3638613036332e7376673f7374796c653d666c6174)
![macos](https://camo.githubusercontent.com/1b8313498db244646b38a4480186ae2b25464e5e8d71a1920c52b2be5212b909/687474703a2f2f696d672e736869656c64732e696f2f62616467652f706c6174666f726d2d6d61636f732d3131313131312e7376673f7374796c653d666c6174)
![tvos](https://camo.githubusercontent.com/4ac08d7fb1bcb8ef26388cd2bf53b49626e1ab7cbda581162a946dd43e6a2726/687474703a2f2f696d672e736869656c64732e696f2f62616467652f706c6174666f726d2d74766f732d3830383038302e7376673f7374796c653d666c6174)
![watchos](https://camo.githubusercontent.com/135dbadae40f9cabe7a3a040f9380fb485cff36c90909f3c1ae36b81c304426b/687474703a2f2f696d672e736869656c64732e696f2f62616467652f706c6174666f726d2d77617463686f732d4330433043302e7376673f7374796c653d666c6174)

Apollo Multibase is Kotlin Multiplatform library of implementation of [Multibase](https://github.com/multiformats/multibase) -self identifying base encodings- in KMM

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
val decode = MultiBase.decode(input) // will auto-detect the encoding base and return the decoded value
val inputByteArray: ByteArray = ....
val encode = MultiBase.encode(MultiBase.Base.BASE64, inputByteArray)
```

## More info

The current Multibase table is [here](multibase.csv):

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