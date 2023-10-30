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
    }

    dependencies {
        classpath("io.arrow-kt:arrow-ank-gradle:0.11.0")
    }
}

rootProject.name = "apollo"
include(":apollo")
include(":hashing")
include(":uuid")
include(":base16")
include(":base32")
include(":base58")
include(":base64")
include(":multibase")
include(":utils")
include(":cryptography")
include(":secure-random")
// include(":aes")
include(":cryptography")
include(":iOSLibs")
// include(":rsa")
// include(":ecdsa")
include(":varint")
// include(":jose")
include("secp256k1-kmp")
include("secp256k1-kmp:native")
