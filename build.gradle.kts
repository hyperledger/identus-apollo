import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootExtension
import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootPlugin

plugins {
    id("maven-publish")
    id("org.jetbrains.dokka") version "1.7.10"
}

buildscript {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.21")
        classpath("com.android.tools.build:gradle:7.2.2")
    }
}

version = "1.0.0-alpha"
group = "io.iohk.atala.prism"

allprojects {
    repositories {
        google()
        mavenCentral()
    }

//    apply(plugin = "org.gradle.maven-publish")

//    publishing {
//        repositories {
//            maven {
//                this.name = "GitHubPackages"
//                this.url = uri("https://maven.pkg.github.com/input-output-hk/atala-prism-apollo")
//                credentials {
//                    this.username = System.getenv("ATALA_GITHUB_ACTOR")
//                    this.password = System.getenv("ATALA_GITHUB_TOKEN")
//                }
//            }
//        }
//    }
}

rootProject.plugins.withType(NodeJsRootPlugin::class.java) {
    rootProject.extensions.getByType(NodeJsRootExtension::class.java).nodeVersion = "16.17.0"
}

tasks.dokkaGfmMultiModule.configure {
    outputDirectory.set(buildDir.resolve("dokkaCustomMultiModuleOutput"))
}