# Apollo
[![Kotlin](https://img.shields.io/badge/kotlin-1.7.20-blue.svg?logo=kotlin)](http://kotlinlang.org)
[![Build](https://github.com/input-output-hk/atala-prism-apollo/actions/workflows/pull-request.yml/badge.svg)](https://github.com/input-output-hk/atala-prism-apollo/actions/workflows/pull-request.yml)
[![Deployment](https://github.com/input-output-hk/atala-prism-apollo/actions/workflows/Deployment.yml/badge.svg)](https://github.com/input-output-hk/atala-prism-apollo/actions/workflows/Deployment.yml)

A cryptography lib built with Kotlin Multiplatform with support for the following targets:
- JS
- iOS
- Android
- JVM

## How to build Apollo
### Install JDK 11

```bash
cs java --jvm adopt:1.11.0-11 --setup
```
after that `java -version` should yield

```bash
openjdk version "11.0.11" 2021-04-20
OpenJDK Runtime Environment (build 11.0.11+9)
OpenJDK 64-Bit Server VM (build 11.0.11+9, mixed mode)
```
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

For Ubuntu, `sudo apt update && sudo apt install android-sdk` should work, leaving the SDK at `~/Android/Sdk`

### Create local.properties file

Create a file named local.properties in the root of the prism-kotlin-sdk.

Add your sdk path to local.properties file. Like so:
```properties
sdk.dir = /Users/{{YOUR USER}}/Library/Android/sdk
```
This will indicate to your ide which SDK to use.

Alternatively, you can add the following environment variable into your shell profile file:
```bash
$ export ANDROID_HOME='/Users/{{YOUR USER}}/Library/Android/sdk
```

### Building the project

You should be able to import and build the project in IntelliJ IDEA now. 

#### Troubleshooting
If you get error:

```log
No binary for ChromeHeadless browser on your platform.
Please, set "CHROME_BIN" env variable.
java.lang.IllegalStateException: Errors occurred during launch of browser for testing.
- ChromeHeadless
```

Solution:
- Install headless chrome or just Chrome browser

In case IntelliJ was building but was still showing syntax error in Gradle Script. 
Solution:
- Go to preference/settings and make sure to select the correct Java version 11.

## How to use for JVM/Android app
TBD

## How to use for iOS app
TBD

## How to use for JS app
TBD

## How to use for NodeJS app
TBD

## How to use for Python
TBD

## How to use for Scala
TBD

## How to use for another KMM project
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
| iOS Arm 32                               | :heavy_check_mark: |
| iOS Simulator Arm 64 (Apple Silicon)     | :heavy_check_mark: |
| JVM                                      | :heavy_check_mark: | 
| Android                                  | :heavy_check_mark: |
| JS Browser                               | :heavy_check_mark: |
| NodeJS Browser                           | :heavy_check_mark: |
| macOS X86 64                             | :heavy_check_mark: |
| macOS Arm 64 (Apple Silicon)             | :heavy_check_mark: |
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
| minGW X86 64                             | :x:                |
| minGW X86 32                             | :x:                | 

**For the first, second & third point** we have two cases using Groovy and using Kotlin DSL
### Using Groovy
In the project `build.gradle`
```groovy
allprojects {
    repositories {
        // along with all the other current existing repos add the following
        maven {
            url = uri("https://maven.pkg.github.com/input-output-hk/atala-prism-apollo")
            credentials {
                username = System.getenv("ATALA_GITHUB_ACTOR") // this is CMD system environment and you can replace the retrieval of its value to any other preferable way if needed
                password = System.getenv("ATALA_GITHUB_TOKEN") // this is CMD system environment and you can replace the retrieval of its value to any other preferable way if needed
            }
        }
    }
}
```
In the module `build.gradle`
```groovy
kotlin {
    // if you are going to use the iOS as one of the KMM module target. You need to use the new iOS hierarchy
    ios() // New iOS hierarchy
    // instead of the old way
    iosX64()
    iosArm64()
    iosSimulatorArm64()
    
    sourceSets {
        commonMain {
            dependencies {
                // This following is just an example you can import it as per you needs
                implementation 'io.iohk.atala.prism:Apollo:${latest version}'
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
        maven {
            url = uri("https://maven.pkg.github.com/input-output-hk/atala-prism-apollo")
            credentials {
                username = System.getenv("ATALA_GITHUB_ACTOR") // this is CMD system environment, and you can replace the retrieval of its value to any other preferable way if needed
                password = System.getenv("ATALA_GITHUB_TOKEN") // this is CMD system environment, and you can replace the retrieval of its value to any other preferable way if needed
            }
        }
    }
}
```
```kotlin
kotlin {
    // if you are going to use the iOS as one of the KMM module target. You need to use the new iOS hierarchy
    ios() // New iOS hierarchy
    // instead of the old way
    iosX64()
    iosArm64()
    iosSimulatorArm64()
    
    sourceSets {
        val commonMain by getting {
            dependencies {
                // This following is just an example you can import it as per you needs
                implementation("io.iohk.atala.prism:Apollo:<latest version>")
            }
        }
    }
}
```
**For the third point**
You need to use Kotlin version `1.7.20`.

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
