import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootExtension
import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootPlugin

plugins {
    id("org.jetbrains.dokka") version "1.9.0"
    id("org.jlleitschuh.gradle.ktlint") version "11.5.1"
    id("maven-publish")
    id("org.jetbrains.kotlinx.kover") version "0.7.3"
}

buildscript {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.20")
        classpath("com.android.tools.build:gradle:7.4.0")
    }
}

version = "1.7.0-alpha"
group = "io.iohk.atala.prism.apollo"

allprojects {
    version = "1.7.0-alpha"
    group = "io.iohk.atala.prism.apollo"

    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }

    apply(plugin = "org.gradle.maven-publish")

    publishing {
        repositories {
            maven {
                this.name = "GitHubPackages"
                this.url = uri("https://maven.pkg.github.com/input-output-hk/atala-prism-apollo")
                credentials {
                    this.username = getLocalProperty("username") ?: System.getenv("ATALA_GITHUB_ACTOR")
                    this.password = getLocalProperty("token") ?: System.getenv("ATALA_GITHUB_TOKEN")
                }
            }
        }
    }

//    val os: OperatingSystem = OperatingSystem.current()
//    if (os.isMacOsX) {
//        tasks.getByName<org.jetbrains.kotlin.gradle.targets.native.tasks.KotlinNativeSimulatorTest>("iosX64Test") {
//            device.set("iPhone 14 Plus")
//        }
//        if (System.getProperty("os.arch") != "x86_64") { // M1Chip
//            tasks.getByName<org.jetbrains.kotlin.gradle.targets.native.tasks.KotlinNativeSimulatorTest>("iosSimulatorArm64Test") {
//                device.set("iPhone 14 Plus")
//            }
//        }
//    }
}

subprojects {
    apply(plugin = "org.jetbrains.kotlinx.kover")
    apply(plugin = "org.jlleitschuh.gradle.ktlint")

    ktlint {
        verbose.set(true)
        outputToConsole.set(true)
        filter {
            exclude("/base-asymmetric-encryption/src/jsMain/kotlin/io/iohk/atala/prism/apollo/utils/external/**")
            exclude {
                it.file.toString().contains("external")
            }
            val baseAsymmetricEncryptionExternalPath = rootDir
                .resolve("base-asymmetric-encryption")
                .resolve("src")
                .resolve("jsMain")
                .resolve("kotlin")
                .resolve("io")
                .resolve("iohk")
                .resolve("atala")
                .resolve("prism")
                .resolve("apollo")
                .resolve("utils")
                .resolve("external")
            exclude(
                "$baseAsymmetricEncryptionExternalPath/**",
                "$baseAsymmetricEncryptionExternalPath/*"
            )
        }
    }
}

dependencies {
    kover(project(":apollo"))
    kover(project(":hashing"))
    kover(project(":uuid"))
    kover(project(":base16"))
    kover(project(":base32"))
    kover(project(":base58"))
    kover(project(":base64"))
    kover(project(":multibase"))
    kover(project(":utils"))
    kover(project(":base-symmetric-encryption"))
    kover(project(":secure-random"))
    // kover(project(":aes"))
    kover(project(":base-asymmetric-encryption"))
    // kover(project(":rsa"))
    // kover(project(":ecdsa"))
    kover(project(":varint"))
    // kover(project(":jose"))
    kover(project("secp256k1-kmp"))
    kover(project("secp256k1-kmp:native"))
}

rootProject.plugins.withType(NodeJsRootPlugin::class.java) {
    rootProject.extensions.getByType(NodeJsRootExtension::class.java).nodeVersion = "18.17.1"
}

tasks.dokkaGfmMultiModule.configure {
    outputDirectory.set(buildDir.resolve("dokkaCustomMultiModuleOutput"))
}

/**
 * Read any properties file and return the value of the key passed
 *
 * @param key value to key that needs reading
 * @param file file name in root folder that will be read with default value of "local.properties"
 * @throws [IllegalStateException] in case of failing to read file
 *
 * @return value of the key if found
 */
@kotlin.jvm.Throws(IllegalStateException::class)
fun Project.getLocalProperty(key: String, file: String = "local.properties"): String? {
    require(file.endsWith(".properties"))
    val properties = java.util.Properties()
    val localProperties = File(file)
    if (localProperties.isFile) {
        java.io.InputStreamReader(java.io.FileInputStream(localProperties), Charsets.UTF_8).use { reader ->
            properties.load(reader)
        }
    } else {
        // Handle CI in GitHub doesn't have `local.properties` file
        logger.warn("$file File not found.")
        return "null"
    }

    val value = properties.getProperty(key, "null")

    return if (value == "null") null else value
}

koverReport {
    filters {
        excludes {
            classes("io.iohk.atala.prism.apollo.utils.bip39.wordlists.*")
        }
    }
}
