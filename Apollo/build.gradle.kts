import org.gradle.internal.os.OperatingSystem
import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackOutput.Target

version = rootProject.version
val currentModuleName: String = "Apollo"
val os: OperatingSystem = OperatingSystem.current()

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("com.android.library")
    id("org.jetbrains.dokka")
}

kotlin {
    explicitApi()
    explicitApi = ExplicitApiMode.Strict

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

        ios("ios") {

            binaries.framework {
                baseName = currentModuleName
                embedBitcode("disable")
            }

            // Facade to SwiftCryptoKit
            val platform = when (name) {
                "ios" -> "iphonesimulator"
                "iosX64" -> "iphonesimulator"
                "iosArm64" -> "iphoneos"
                else -> error("Unsupported target $name.")
            }

            compilations.getByName("main") {
                cinterops.create("SwiftCryptoKit") {
                    extraOpts = listOf("-compiler-option", "-DNS_FORMAT_ARGUMENT(A)=")
                    val interopTask = tasks[interopProcessingTaskName]
                    interopTask.dependsOn(":SwiftCryptoKit:build${platform.capitalize()}")
                    includeDirs.headerFilterOnly("$rootDir/SwiftCryptoKit/build/Release-$platform/include")
                }
            }

            compilations.getByName("main") {
                cinterops.create("secp256k1") {
                    val interopTask = tasks[interopProcessingTaskName]
                    includeDirs.headerFilterOnly("$rootDir/Secp256k1/include")
                }
            }
        }
    }
    js(IR) {
        this.moduleName = "apollo"
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
            this.commonWebpackConfig {
                this.cssSupport {
                    this.enabled = true
                }
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

    /*if (os.isMacOsX) {
        cocoapods {
            this.summary = "ApolloUtils is a Utils helper module"
            this.version = rootProject.version.toString()
            this.authors = "IOG"
            framework {
                baseName = currentModuleName
                embedBitcode("marker")
            }
        }
    }*/

    sourceSets {
        val commonMain by getting {
            dependencies {
                api(project(":base16"))
                api(project(":base32"))
                api(project(":base58"))
                api(project(":base64"))
                api(project(":hashing"))
                api(project(":multibase"))
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val androidMain by getting {
            kotlin.srcDir("src/commonJvmAndroidMain/kotlin")
            dependencies {
                implementation("com.google.guava:guava:30.1-jre")
                implementation("com.madgag.spongycastle:prov:1.58.0.0")
                implementation("org.bitcoinj:bitcoinj-core:0.15.10") {
                    exclude("com.google.protobuf")
                }
            }
        }
        val androidTest by getting {
            kotlin.srcDir("src/commonJvmAndroidTest/kotlin")
            resources.srcDir("src/commonJvmAndroidTest/resources")
            dependencies {
                implementation("junit:junit:4.12")
            }
        }
        val jvmMain by getting {
            kotlin.srcDir("src/commonJvmAndroidMain/kotlin")
            dependencies {
                implementation("com.google.guava:guava:30.1-jre")
                implementation("org.bouncycastle:bcprov-jdk15on:1.68")
                implementation("org.bitcoinj:bitcoinj-core:0.15.10")
            }
        }
        val jvmTest by getting {
            kotlin.srcDir("src/commonJvmAndroidTest/kotlin")
            resources.srcDir("src/commonJvmAndroidTest/resources")
            dependencies {
                implementation("junit:junit:4.12")
            }
        }

        val jsMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-nodejs:0.0.7")

                implementation(npm("hash.js", "1.1.7"))
                implementation(npm("elliptic", "6.5.3"))
                implementation(npm("bip32", "2.0.6"))
                implementation(npm("bip39", "3.0.3"))

                // Polyfill dependencies
                implementation(npm("stream-browserify", "3.0.0"))
                implementation(npm("buffer", "6.0.3"))
            }
        }
        val jsTest by getting {
            dependencies {
            }
        }
        if (os.isMacOsX) {
            val iosMain by getting {
                dependencies {
                    implementation("fr.acinq.bitcoin:bitcoin-kmp:0.10.0")
                }
            }
            val iosTest by getting
            if (System.getProperty("os.arch") != "x86_64") { // M1Chip
                val iosSimulatorArm64Main by getting {
                    this.dependsOn(iosMain)
                }
                val iosSimulatorArm64Test by getting {
                    this.dependsOn(iosTest)
                }
            }
        }
        all {
            languageSettings.optIn("kotlin.RequiresOptIn")
            languageSettings.optIn("kotlin.ExperimentalUnsignedTypes")
            languageSettings.optIn("kotlin.js.ExperimentalJsExport")
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
        This is a Kotlin Multiplatform Library for cryptography
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
