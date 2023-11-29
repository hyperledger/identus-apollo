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
include(":iOSLibs")
include("secp256k1-kmp")
include("secp256k1-kmp:native")
