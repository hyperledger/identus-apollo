import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootExtension
import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootPlugin

version = "1.0.0-alpha"
group = "io.iohk.atala.prism"

plugins {
    java
    kotlin("jvm") version "1.7.22"
    kotlin("multiplatform") version "1.7.22" apply false
    kotlin("native.cocoapods") version "1.7.22" apply false
    kotlin("plugin.serialization") version "1.7.22" apply false
    id("dev.petuska.npm.publish") version "3.1.0" apply false
    id("org.jetbrains.dokka") version "1.7.10"
    id("org.jlleitschuh.gradle.ktlint") version "11.0.0"
    id("maven-publish")
}

buildscript {
    repositories {
        mavenCentral()
        mavenLocal()
        google()
        gradlePluginPortal()
        maven("https://plugins.gradle.org/m2/")
        // Needed for Kotlin coroutines that support new memory management mode
        maven {
            url = uri("https://maven.pkg.jetbrains.space/public/p/kotlinx-coroutines/maven")
        }
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.22")
        classpath("com.android.tools.build:gradle:7.2.2")
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
        vendor.set(JvmVendorSpec.ADOPTOPENJDK)
    }
}

allprojects {
    repositories {
        mavenCentral()
        mavenLocal()
        google()
        maven("https://plugins.gradle.org/m2/")
        // Needed for Kotlin coroutines that support new memory management mode
        maven {
            url = uri("https://maven.pkg.jetbrains.space/public/p/kotlinx-coroutines/maven")
        }
    }

    val listOfModulesNotToPublish = listOf(
        "utils",
        "base-symmetric-encryption"
    )

    if (listOfModulesNotToPublish.contains(name).not()) {
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
}

subprojects {
    apply(plugin = "org.jlleitschuh.gradle.ktlint")

    configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
        verbose.set(true)
        outputToConsole.set(true)
    }
}

rootProject.plugins.withType(org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootPlugin::class.java) {
    rootProject.extensions.getByType(org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootExtension::class.java).nodeVersion = "16.17.0"
}

tasks.dokkaGfmMultiModule.configure {
    outputDirectory.set(buildDir.resolve("dokkaCustomMultiModuleOutput"))
}
