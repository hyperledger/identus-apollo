pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        google()
    }
}

buildscript {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        mavenLocal()
        google()
        maven("https://plugins.gradle.org/m2/")
        // Needed for Kotlin coroutines that support new memory management mode
        maven {
            url = uri("https://maven.pkg.jetbrains.space/public/p/kotlinx-coroutines/maven")
        }
    }

    dependencies {
        classpath("io.arrow-kt:arrow-ank-gradle:0.11.0")
    }
}

rootProject.name = "apollo"
include(":Apollo")
include(":Hashing")
include(":UUID")
include(":Base16")
include(":Base32")
include(":Base58")
include(":base64")
include(":Base64")
include(":Multibase")
include(":utils")
include(":base-symmetric-encryption")
include(":secure-random")
include(":aes")
include(":base-asymmetric-encryption")
include(":rsa")
include(":secp256k1")
include(":ecdsa")
include(":varint")
include(":jose")
