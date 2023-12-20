# Apollo

[![Kotlin](https://img.shields.io/badge/kotlin-1.9.21-blue.svg?logo=kotlin)](http://kotlinlang.org)
[![Build](https://github.com/input-output-hk/atala-prism-apollo/actions/workflows/pull-request.yml/badge.svg)](https://github.com/input-output-hk/atala-prism-apollo/actions/workflows/pull-request.yml)
[![Deployment](https://github.com/input-output-hk/atala-prism-apollo/actions/workflows/Deployment.yml/badge.svg)](https://github.com/input-output-hk/atala-prism-apollo/actions/workflows/Deployment.yml)

![android](https://camo.githubusercontent.com/b1d9ad56ab51c4ad1417e9a5ad2a8fe63bcc4755e584ec7defef83755c23f923/687474703a2f2f696d672e736869656c64732e696f2f62616467652f706c6174666f726d2d616e64726f69642d3645444238442e7376673f7374796c653d666c6174)
![apple-silicon](https://camo.githubusercontent.com/a92c841ffd377756a144d5723ff04ecec886953d40ac03baa738590514714921/687474703a2f2f696d672e736869656c64732e696f2f62616467652f737570706f72742d2535424170706c6553696c69636f6e2535442d3433424246462e7376673f7374796c653d666c6174)
![ios](https://camo.githubusercontent.com/1fec6f0d044c5e1d73656bfceed9a78fd4121b17e82a2705d2a47f6fd1f0e3e5/687474703a2f2f696d672e736869656c64732e696f2f62616467652f706c6174666f726d2d696f732d4344434443442e7376673f7374796c653d666c6174)
![jvm](https://camo.githubusercontent.com/700f5dcd442fd835875568c038ae5cd53518c80ae5a0cf12c7c5cf4743b5225b/687474703a2f2f696d672e736869656c64732e696f2f62616467652f706c6174666f726d2d6a766d2d4442343133442e7376673f7374796c653d666c6174)
![js](https://camo.githubusercontent.com/3e0a143e39915184b54b60a2ecedec75e801f396d34b5b366c94ec3604f7e6bd/687474703a2f2f696d672e736869656c64732e696f2f62616467652f706c6174666f726d2d6a732d4638444235442e7376673f7374796c653d666c6174)
![getNode-js](https://camo.githubusercontent.com/d08fda729ceebcae0f23c83499ca8f06105350f037661ac9a4cc7f58edfdbca9/68747470733a2f2f696d672e736869656c64732e696f2f62616467652f706c6174666f726d2d6e6f64656a732d3638613036332e7376673f7374796c653d666c6174)
![macos](https://camo.githubusercontent.com/1b8313498db244646b38a4480186ae2b25464e5e8d71a1920c52b2be5212b909/687474703a2f2f696d672e736869656c64732e696f2f62616467652f706c6174666f726d2d6d61636f732d3131313131312e7376673f7374796c653d666c6174)

![Atala Prism Logo](Logo.png)

A cryptography lib built with Kotlin Multiplatform with support for the following targets:

- JS
- iOS
- Android
- JVM

## How to build Apollo

### Set Environment Variables

Set variable `ATALA_GITHUB_ACTOR` with your GitHub Username and set variable `ATALA_GITHUB_TOKEN` with your GitHub Personal Access Token.

As an example we will go with `Bash`

1. Open CMD.
2. Run `sudo nano $HOME/.bash_profile`.
3. Insert `export ATALA_GITHUB_ACTOR="YOUR GITHUB USERNAME"`
4. Insert `export ATALA_GITHUB_TOKEN="YOUR GITHUB PERSONAL ACCESS TOKEN"`
5. Save profile and restart CMD to take effect.

### Install Homebrew (Mac Only)

```bash
/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
```

### Install autoconf, automake & libtool (Mac Only)

```bash
brew install autoconf automake libtool
```

### Install JDK 11

```bash
cs java --jvm adopt:1.11.0-11 --setup
```
after that `java -version` should yield something like that

```text
openjdk version "11.0.11" 2021-04-20
OpenJDK Runtime Environment (build 11.0.11+9)
OpenJDK 64-Bit Server VM (build 11.0.11+9, mixed mode)
```

In case of using macOS with M chip, make sure to install the arch64 version of Java

### Install XCode (Mac Only)

Install XCode from App Store. 

Then approve xcodebuild license in your terminal. Like so:
```bash
$ sudo xcodebuild -license
```

### Install Android SDK

Install Android SDK from SDK Manager (via Android Studio). 

Then approve Android SDK license. Like so:
```bash
$ cd /Users/{{YOUR USER}}/Library/Android/sdk
$ tools/bin/sdkmanager --licenses
```
While there are many ways to install Android SDK this has proven to be the most reliable way. Standard IntelliJ with Android plugin may work. However, we've had several issues. Your mileage may vary.

For Ubuntu, 
```bash
sudo apt update && sudo apt install android-sdk
```
Leaving the SDK at `~/Android/Sdk`

### Create local.properties file

Create a file named `local.properties` in the root of Apollo.

Add your android sdk path to `local.properties file`. Like so:
```properties
sdk.dir = /Users/{{YOUR USER}}/Library/Android/sdk
```
This will indicate to your IDE which android SDK to use.

Alternatively, you can add the following environment variable into your shell profile file:
```bash
$ export ANDROID_HOME='/Users/{{YOUR USER}}/Library/Android/sdk
```

### Building the project

You should be able to import and build the project in IntelliJ IDEA now. 

#### Troubleshooting

Here is a list of common issues you might face and its solutions.

##### Enviroment Variables were added but not available

If you already added the envorment variable to your CMD profile and still not being available.

**Solution**

* Restart your Device.

##### No binary for ChromeHeadless browser on your platform

If you get error:
```log
No binary for ChromeHeadless browser on your platform.
Please, set "CHROME_BIN" env variable.
java.lang.IllegalStateException: Errors occurred during launch of browser for testing.
- ChromeHeadless
```

**Solution**

* Install headless chrome or just Chrome browser

##### In case IntelliJ was building but was still showing syntax error in Gradle Script

**Solution**

* Go to preference/settings and make sure to select the correct Java version 11.

##### Could not find JNA native support

if you get this error on macOS with M chip:
```log
Could not find JNA native support
```
**Solution**

* Make sure that you are using Java version that is arch64.

## How to use for JVM/Android app

In `build.gradle.kts` files include the dependency
```kotlin
repositories {
    mavenCentral()
}
```
For dependencies
```kotlin
dependencies {
    val apolloVersion = ... // Latest Apollo Version
    implementation("io.iohk.atala.prism.apollo:apollo:$apolloVersion")
}
```

## How to use for Swift app

### Using SPM

Inside your `Package.swift` file, add the following
```swift
dependencies: [
    .package(
        url: "git@github.com:input-output-hk/atala-prism-apollo.git",
        from: "latest version"
    )
]
```
### Using generated xcframework directly

The following instruction using Xcode 15
1. Go the [Release Page](https://github.com/input-output-hk/atala-prism-apollo/releases) and check the latest version and download the `Apollo.xcframework.zip` file.
2. Uncompress the downloaded file.
3. Add the `Apollo.xcframework` to your Xcode project.
4. When asked select Copy items if needed.
5. Then go to the project configuration page in Xcode and check the Frameworks and Libraries section and add the `Apollo.xcframework` if not found then choose `Embed & Sign`.
6. Then go to the build phase page and mark the framework as required.

[!WARNING]
**For Intel iOS simulator**: You need to add the following flag as YES `EMBEDDED_CONTENT_CONTAINS_SWIFT=YES` on the target like so:

```swift
Package.swift

Package(
   ...
   targets: .testTarget(
      ...
      swiftSettings: [.define("EMBEDDED_CONTENT_CONTAINS_SWIFT=YES")]
      ...
   )
)

```

## How to use for Node.js app

Inside the `package.json`
```json
{
    "dependencies": {
        "@atala/apollo": "latest version of Apollo"
    }
}
```

## How to use for another KMP (Kotlin Multiplatform) project

You need to do the following:

1. You need to let Gradle know where to search for the Apollo package
2. Import the packages in the common target as per your project needs
    1. Once you insert the import in the common target, it will automatically retrieve each supported target knowing that the currently only available targets are:
3. You need to use the new iOS hierarchy system (In case they have an iOS target)
4. You need to use the same Kotlin version used in our project

| Platform                                 | Supported          |
|------------------------------------------|--------------------|
| iOS x86 64                               | :heavy_check_mark: |
| iOS Arm 64                               | :heavy_check_mark: |
| iOS Simulator Arm 64 (Apple Silicon)     | :heavy_check_mark: |
| JVM                                      | :heavy_check_mark: | 
| Android                                  | :heavy_check_mark: |
| JS Browser                               | :heavy_check_mark: |
| NodeJS Browser                           | :heavy_check_mark: |
| macOS X86 64                             | :heavy_check_mark: |
| macOS Arm 64 (Apple Silicon)             | :heavy_check_mark: |

**For the first, second & third point** we have two cases using Groovy and using Kotlin DSL

### Using Groovy

In the project `build.gradle`
```groovy
allprojects {
    repositories {
        // along with all the other current existing repos add the following
        mavenCentral()
    }
}
```
In the module `build.gradle`
```groovy
kotlin {
    sourceSets {
        commonMain {
            dependencies {
                // This following is just an example you can import it as per you needs
                implementation 'io.iohk.atala.prism.apollo:apollo:${latest version}'
            }
        }
    }
}
```

### Using Kotlin DSL

In the project `build.gradle.kts`
```kotlin
allprojects {
    repositories {
        // along with all the other current existing repos add the following
        mavenCentral()
    }
}
```
```kotlin
kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                // This following is just an example you can import it as per you needs
                implementation("io.iohk.atala.prism.apollo:apollo:<latest version>")
            }
        }
    }
}
```
**For the third point**
You need to use Kotlin version `1.9.21`.

## Usage

Please have a look at unit tests, more samples will be added soon.

## Cryptography Notice

This distribution includes cryptographic software. The country in which you currently reside may 
have restrictions on the import, possession, use, and/or re-export to another country, of encryption 
software. BEFORE using any encryption software, please check your country's laws, regulations and policies 
concerning the import, possession, or use, and re-export of encryption software, to see if this is permitted. 
See [http://www.wassenaar.org/](http://www.wassenaar.org/) for more information.

## License

This software is provided 'as-is', without any express or implied warranty. In no event will the
authors be held liable for any damages arising from the use of this software. Permission is granted
to anyone to use this software for any purpose, including commercial applications, and to alter it
and redistribute it freely.
