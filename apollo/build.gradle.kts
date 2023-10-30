import dev.petuska.npm.publish.extension.domain.NpmAccess
import org.gradle.internal.os.OperatingSystem
import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackOutput.Target

val currentModuleName: String = "Apollo"
val os: OperatingSystem = OperatingSystem.current()

plugins {
    kotlin("multiplatform")
    id("io.github.luca992.multiplatform-swiftpackage") version "2.0.5-arm64"
    id("com.android.library")
    id("org.jetbrains.dokka")
    id("dev.petuska.npm.publish") version "3.4.1"
}

kotlin {
    android {
        publishAllLibraryVariants()
    }
    jvm {
        compilations.all {
            kotlinOptions {
                jvmTarget = "11"
            }
        }
        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
        }
    }

    ios {
        binaries.framework {
            export(project(":cryptography"))
            export(project(":multibase"))
            export(project(":base64"))
            export(project(":base58"))
            export(project(":base32"))
            export(project(":utils"))
            export(project(":hashing"))
        }
    }
    iosSimulatorArm64() {
        binaries.framework {
            export(project(":cryptography"))
            export(project(":multibase"))
            export(project(":base64"))
            export(project(":base58"))
            export(project(":base32"))
            export(project(":utils"))
            export(project(":hashing"))
        }
    }
    macosArm64() {
        binaries.framework {
            export(project(":cryptography"))
            export(project(":multibase"))
            export(project(":base64"))
            export(project(":base58"))
            export(project(":base32"))
            export(project(":utils"))
            export(project(":hashing"))
        }
    }

    js(IR) {
        this.moduleName = currentModuleName
        this.binaries.library()
        this.useCommonJs()
        generateTypeScriptDefinitions()
        this.compilations["main"].packageJson {
            this.version = rootProject.version.toString()
        }
        this.compilations["test"].packageJson {
            this.version = rootProject.version.toString()
        }
        browser {
            this.webpackTask {
                this.output.library = currentModuleName
                this.output.libraryTarget = Target.VAR
            }
            this.commonWebpackConfig {
//                this.cssSupport {
//                    enabled(true)
//                }
            }
            this.testTask {
                this.useKarma {
                    this.useChromeHeadless()
                }
            }
        }
        nodejs {
            this.testTask {
                this.useKarma {
                    this.useChromeHeadless()
                }
            }
        }
    }

    multiplatformSwiftPackage {
        packageName("Apollo")
        swiftToolsVersion("5.3")
        targetPlatforms {
            iOS { v("13") }
            macOS { v("11") }
        }
        outputDirectory(File(rootDir, "apollo/build/packages/ApolloSwift"))
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                api(project(":cryptography"))
                api(project(":multibase"))
                api(project(":base64"))
                api(project(":base58"))
                api(project(":base32"))
                api(project(":utils"))
                api(project(":hashing"))
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val androidMain by getting
        val androidTest by getting {
            dependencies {
                implementation("junit:junit:4.13.2")
            }
        }
        val jvmMain by getting
        val jvmTest by getting {
            dependencies {
                implementation("junit:junit:4.13.2")
            }
        }
        val jsMain by getting {
            dependencies {
                implementation(npm("elliptic", "6.5.4"))
                implementation(npm("@types/elliptic", "6.4.14"))
                implementation(npm("@noble/curves", "1.2.0"))
                implementation(npm("@stablelib/x25519", "1.0.3"))

                // Polyfill dependencies
                implementation(npm("stream-browserify", "3.0.0"))
                implementation(npm("buffer", "6.0.3"))
            }
        }
        val jsTest by getting

        val iosMain by getting
        val iosTest by getting

        val iosSimulatorArm64Main by getting {
            this.dependsOn(iosMain)
        }
        val iosSimulatorArm64Test by getting {
            this.dependsOn(iosTest)
        }
        val macosArm64Main by getting
        val macosArm64Test by getting
    }

    if (os.isMacOsX) {
        tasks.getByName<org.jetbrains.kotlin.gradle.targets.native.tasks.KotlinNativeSimulatorTest>("iosX64Test") {
            device.set("iPhone 14 Plus")
        }
        tasks.getByName<org.jetbrains.kotlin.gradle.targets.native.tasks.KotlinNativeSimulatorTest>("iosSimulatorArm64Test") {
            device.set("iPhone 14 Plus")
        }
    }
}

android {
    compileSdk = 32
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = 21
        targetSdk = 32
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    /**
     * Because Software Components will not be created automatically for Maven publishing from
     * Android Gradle Plugin 8.0. To opt-in to the future behavior, set the Gradle property android.
     * disableAutomaticComponentCreation=true in the `gradle.properties` file or use the new
     * publishing DSL.
     */
    publishing {
        multipleVariants {
            withSourcesJar()
            withJavadocJar()
            allVariants()
        }
    }
}

// Dokka implementation
tasks.withType<DokkaTask> {
    moduleName.set(project.name)
    moduleVersion.set(rootProject.version.toString())
    description = "This is a Kotlin Multiplatform Library for cryptography"
    dokkaSourceSets {
        // TODO: Figure out how to include files to the documentations
        named("commonMain") {
            includes.from("Module.md", "docs/Module.md")
        }
    }
}

npmPublish {
    organization.set("input-output-hk")
    version.set(rootProject.version.toString())
    access.set(NpmAccess.PUBLIC)
    packages {
        access.set(NpmAccess.PUBLIC)
        named("js") {
            scope.set("input-output-hk")
            packageName.set("apollo")
            packageJson {
                author {
                    name.set("IOG")
                }
                repository {
                    type.set("git")
                    url.set("https://github.com/input-output-hk/atala-prism-apollo.git")
                }
            }
        }
    }
    registries {
        access.set(NpmAccess.PUBLIC)
        github {
            uri.set("https://npm.pkg.github.com/")
            access.set(NpmAccess.PUBLIC)
            this.authToken.set(System.getenv("ATALA_GITHUB_TOKEN"))
        }
    }
}
