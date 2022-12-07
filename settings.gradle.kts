pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

rootProject.name = "apollo"
include(":Apollo")
include(":Hashing")
include(":UUID")
include(":Base16")
include(":Base32")
include(":Base58")
include(":Base64")
include(":Multibase")
