import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootExtension
import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootPlugin

plugins {
    id("org.jetbrains.dokka") version "1.9.10"
    id("org.jlleitschuh.gradle.ktlint") version "11.6.1"
    id("maven-publish")
    id("org.jetbrains.kotlinx.kover") version "0.7.4"
}

buildscript {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.20")
        classpath("com.android.tools.build:gradle:7.2.2")
        classpath("org.jetbrains.dokka:dokka-base:1.9.10")
    }
}

group = "io.iohk.atala.prism.apollo"

dependencies {
    kover(project(":apollo"))
    kover(project("secp256k1-kmp"))
    kover(project("secp256k1-kmp:native"))
}

koverReport {
    filters {
        excludes {
            classes("io.iohk.atala.prism.apollo.utils.bip39.wordlists.*")
        }
    }
}

allprojects {
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
                    this.username = System.getenv("ATALA_GITHUB_ACTOR")
                    this.password = System.getenv("ATALA_GITHUB_TOKEN")
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
            exclude {
                it.file.toString().contains("external")
            }
            exclude {
                it.file.toString() == "BNjs.kt" || it.file.toString() == "Curve.kt" || it.file.toString() == "PresetCurve.kt" ||
                    it.file.toString() == "Ellipticjs.kt" || it.file.toString() == "secp256k1js.kt"
            }
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
