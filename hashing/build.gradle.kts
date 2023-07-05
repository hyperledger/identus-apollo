import org.gradle.internal.os.OperatingSystem
import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackOutput.Target

val currentModuleName = "ApolloHashing"
val os: OperatingSystem = OperatingSystem.current()

plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    id("com.android.library")
    id("org.jetbrains.dokka")
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
    if (os.isMacOsX) {
        ios()
//        tvos()
//        watchos()
        macosX64()
        if (System.getProperty("os.arch") != "x86_64") { // M1Chip
            iosSimulatorArm64()
//            tvosSimulatorArm64()
//            watchosSimulatorArm64()
            macosArm64()
        }
    }
    if (os.isWindows) {
        mingwX86()
        mingwX64()
    }
    js(IR) {
        this.moduleName = currentModuleName
        this.binaries.library()
        this.useCommonJs()
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
            this.testTask {
                if (os.isWindows) {
                    this.enabled = false
                }
                this.useKarma {
                    this.useChromeHeadless()
                }
            }
        }
        nodejs {
            this.testTask {
                if (os.isWindows) {
                    this.enabled = false
                }
                this.useKarma {
                    this.useChromeHeadless()
                }
            }
        }
    }

    if (os.isMacOsX) {
        cocoapods {
            this.summary = "Apollo Hashing is a Hashing library containing MD2, MD4, MD5, SHA, SHA-2, SHA-3, Blake, Blake2b, Blake2s and Blake3"
            this.version = rootProject.version.toString()
            this.authors = "IOG"
            this.ios.deploymentTarget = "13.0"
            this.osx.deploymentTarget = "12.0"
            this.tvos.deploymentTarget = "13.0"
            this.watchos.deploymentTarget = "8.0"
            framework {
                this.baseName = currentModuleName
            }
        }
    }

    sourceSets {
        val commonMain by getting
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.4")
            }
        }
        val allButJSMain by creating {
            this.dependsOn(commonMain)
        }
        val allButJSTest by creating {
            this.dependsOn(commonTest)
        }
        val jvmMain by getting {
            this.dependsOn(allButJSMain)
        }
        val jvmTest by getting {
            this.dependsOn(allButJSTest)
        }
        val androidMain by getting {
            this.dependsOn(allButJSMain)
        }
        val androidTest by getting {
            this.dependsOn(allButJSTest)
            dependencies {
                implementation("junit:junit:4.13.2")
            }
        }
        val jsMain by getting {
            dependencies {
                implementation(npm("hash.js", "1.1.7"))

                // Polyfill dependencies
                implementation(npm("stream-browserify", "3.0.0"))
                implementation(npm("buffer", "6.0.3"))
            }
        }
        val jsTest by getting
        if (os.isMacOsX) {
            val iosMain by getting {
                this.dependsOn(allButJSMain)
            }
            val iosTest by getting {
                this.dependsOn(allButJSTest)
            }
            val macosX64Main by getting {
                this.dependsOn(allButJSMain)
            }
            val macosX64Test by getting {
                this.dependsOn(allButJSTest)
            }
//            val tvosMain by getting
//            val tvosTest by getting
//            val watchosMain by getting
//            val watchosTest by getting
            if (System.getProperty("os.arch") != "x86_64") { // M1Chip
                val iosSimulatorArm64Main by getting {
                    this.dependsOn(allButJSMain)
                }
                val iosSimulatorArm64Test by getting {
                    this.dependsOn(allButJSTest)
                }
//                val tvosSimulatorArm64Main by getting {
//                    this.dependsOn(tvosMain)
//                }
//                val tvosSimulatorArm64Test by getting {
//                    this.dependsOn(tvosTest)
//                }
//                val watchosSimulatorArm64Main by getting {
//                    this.dependsOn(watchosMain)
//                }
//                val watchosSimulatorArm64Test by getting {
//                    this.dependsOn(watchosTest)
//                }
                val macosArm64Main by getting {
                    this.dependsOn(macosX64Main)
                }
                val macosArm64Test by getting {
                    this.dependsOn(macosX64Test)
                }
            }
        }
        if (os.isWindows) {
            val mingwX86Main by getting {
                this.dependsOn(allButJSMain)
            }
            val mingwX86Test by getting {
                this.dependsOn(allButJSTest)
            }
            val mingwX64Main by getting {
                this.dependsOn(allButJSMain)
            }
            val mingwX64Test by getting {
                this.dependsOn(allButJSTest)
            }
        }
        all {
            languageSettings.optIn("kotlin.RequiresOptIn")
        }
    }

    if (os.isMacOsX) {
        tasks.getByName<org.jetbrains.kotlin.gradle.targets.native.tasks.KotlinNativeSimulatorTest>("iosX64Test") {
            deviceId = "iPhone 14 Plus"
        }
        if (System.getProperty("os.arch") != "x86_64") { // M1Chip
            tasks.getByName<org.jetbrains.kotlin.gradle.targets.native.tasks.KotlinNativeSimulatorTest>("iosSimulatorArm64Test") {
                deviceId = "iPhone 14 Plus"
            }
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
    description = """
        This is a Kotlin Multiplatform Library for Hashing in cryptography
    """.trimIndent()
    dokkaSourceSets {
        // TODO: Figure out how to include files to the documentations
        named("commonMain") {
            includes.from("Module.md", "docs/Module.md")
        }
    }
}

// afterEvaluate {
//    tasks.withType<AbstractTestTask> {
//        testLogging {
//            events("passed", "skipped", "failed", "standard_out", "standard_error")
//            showExceptions = true
//            showStackTraces = true
//        }
//    }
// }
