import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootExtension
import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootPlugin

plugins {
    id("org.jetbrains.dokka") version "1.7.10"
    id("org.jlleitschuh.gradle.ktlint") version "11.0.0"
    id("maven-publish")
    id("org.jetbrains.kotlinx.kover") version "0.7.2"
}

buildscript {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.20")
        classpath("com.android.tools.build:gradle:7.2.2")
        // classpath("org.jetbrains.kotlin:kotlin-compiler-embeddable:1.7.21")
    }
}

version = "1.0.2"
group = "io.iohk.atala.prism.apollo"

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
//    kover(project(":aes"))
    kover(project(":base-asymmetric-encryption"))
//    kover(project(":rsa"))
//    kover(project(":ecdsa"))
    kover(project(":varint"))
//    kover(project(":jose"))
    kover(project("secp256k1-kmp"))
    kover(project("secp256k1-kmp:native"))
}

allprojects {
    version = "1.0.2"
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
}

subprojects {
    apply(plugin = "org.jlleitschuh.gradle.ktlint")
    apply(plugin = "org.jetbrains.kotlinx.kover")

    ktlint {
        verbose.set(true)
        outputToConsole.set(true)
        filter {
            exclude("/base-asymmetric-encryption/src/jsMain/kotlin/io/iohk/atala/prism/apollo/utils/external/**")
            exclude {
                it.file.toString().contains("external")
            }
            exclude(
                "/github/workspace/base-asymmetric-encryption/src/jsMain/kotlin/io/iohk/atala/prism/apollo/utils/external/**",
                "/github/workspace/base-asymmetric-encryption/src/jsMain/kotlin/io/iohk/atala/prism/apollo/utils/external/*",
                "/github/workspace/base-asymmetric-encryption/src/jsMain/kotlin/io/iohk/atala/prism/apollo/utils/external/BNjs.kt",
                "/github/workspace/base-asymmetric-encryption/src/jsMain/kotlin/io/iohk/atala/prism/apollo/utils/external/Curve.kt",
                "/github/workspace/base-asymmetric-encryption/src/jsMain/kotlin/io/iohk/atala/prism/apollo/utils/external/PresetCurve.kt",
                "/github/workspace/base-asymmetric-encryption/src/jsMain/kotlin/io/iohk/atala/prism/apollo/utils/external/Ellipticjs.kt",
                "./github/workspace/base-asymmetric-encryption/src/jsMain/kotlin/io/iohk/atala/prism/apollo/utils/external/secp256k1js.kt",

                "github/workspace/base-asymmetric-encryption/src/jsMain/kotlin/io/iohk/atala/prism/apollo/utils/external/**",
                "github/workspace/base-asymmetric-encryption/src/jsMain/kotlin/io/iohk/atala/prism/apollo/utils/external/*",
                "github/workspace/base-asymmetric-encryption/src/jsMain/kotlin/io/iohk/atala/prism/apollo/utils/external/BNjs.kt",
                "github/workspace/base-asymmetric-encryption/src/jsMain/kotlin/io/iohk/atala/prism/apollo/utils/external/Curve.kt",
                "github/workspace/base-asymmetric-encryption/src/jsMain/kotlin/io/iohk/atala/prism/apollo/utils/external/PresetCurve.kt",
                "github/workspace/base-asymmetric-encryption/src/jsMain/kotlin/io/iohk/atala/prism/apollo/utils/external/Ellipticjs.kt",
                "./github/workspace/base-asymmetric-encryption/src/jsMain/kotlin/io/iohk/atala/prism/apollo/utils/external/secp256k1js.kt",

                "./github/workspace/base-asymmetric-encryption/src/jsMain/kotlin/io/iohk/atala/prism/apollo/utils/external/**",
                "./github/workspace/base-asymmetric-encryption/src/jsMain/kotlin/io/iohk/atala/prism/apollo/utils/external/*",
                "./github/workspace/base-asymmetric-encryption/src/jsMain/kotlin/io/iohk/atala/prism/apollo/utils/external/BNjs.kt",
                "./github/workspace/base-asymmetric-encryption/src/jsMain/kotlin/io/iohk/atala/prism/apollo/utils/external/Curve.kt",
                "./github/workspace/base-asymmetric-encryption/src/jsMain/kotlin/io/iohk/atala/prism/apollo/utils/external/PresetCurve.kt",
                "./github/workspace/base-asymmetric-encryption/src/jsMain/kotlin/io/iohk/atala/prism/apollo/utils/external/Ellipticjs.kt",
                "./github/workspace/base-asymmetric-encryption/src/jsMain/kotlin/io/iohk/atala/prism/apollo/utils/external/secp256k1js.kt"
            )
            exclude {
                it.file.toString() == "BNjs.kt" || it.file.toString() == "Curve.kt" || it.file.toString() == "PresetCurve.kt" || it.file.toString() == "Ellipticjs.kt" || it.file.toString() == "secp256k1js.kt"
            }
            exclude("./base-asymmetric-encryption/src/jsMain/kotlin/io/iohk/atala/prism/apollo/utils/external/**")
            exclude {
                it.file.toString().contains("external")
            }
            exclude { projectDir.toURI().relativize(it.file.toURI()).path.contains("/external/") }
        }
    }
}

rootProject.plugins.withType(NodeJsRootPlugin::class.java) {
    rootProject.extensions.getByType(NodeJsRootExtension::class.java).nodeVersion = "16.17.0"
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
