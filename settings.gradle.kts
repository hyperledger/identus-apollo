pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
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
include(":base-symmetric-encryption")
include(":secure-random")
include(":aes")
include(":base-asymmetric-encryption")
include(":rsa")
