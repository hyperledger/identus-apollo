import org.gradle.internal.os.OperatingSystem
import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackOutput.Target

val currentModuleName = "ApolloSecureRandom"
val os: OperatingSystem = OperatingSystem.current()

plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("org.jetbrains.dokka")
}

fun org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget.swiftCinterop(library: String, platform: String) {
    compilations.getByName("main") {
        cinterops.create(library) {
            extraOpts = listOf("-compiler-option", "-DNS_FORMAT_ARGUMENT(A)=")
            when (platform) {
                "iosX64", "iosSimulatorArm64" -> {
                    includeDirs.headerFilterOnly("$rootDir/iOSLibs/$library/build/Release-iphonesimulator/include/")
                    tasks[interopProcessingTaskName].dependsOn(":iOSLibs:build${library.capitalize()}Iphonesimulator")
                }
                "iosArm64" -> {
                    includeDirs.headerFilterOnly("$rootDir/iOSLibs/$library/build/Release-iphoneos/include/")
                    tasks[interopProcessingTaskName].dependsOn(":iOSLibs:build${library.capitalize()}Iphoneos")
                }
                "macosX64", "macosArm64" -> {
                    includeDirs.headerFilterOnly("$rootDir/iOSLibs/$library/build/Release/include/")
                    tasks[interopProcessingTaskName].dependsOn(":iOSLibs:build${library.capitalize()}Macosx")
                }
                "tvosArm64", "tvosX64", "tvos" -> {
                    includeDirs.headerFilterOnly("$rootDir/iOSLibs/$library/build/Release-appletvos/include/")
                    tasks[interopProcessingTaskName].dependsOn(":iOSLibs:build${library.capitalize()}Appletvos")
                }
                "tvosSimulatorArm64" -> {
                    includeDirs.headerFilterOnly("$rootDir/iOSLibs/$library/build/Release-appletvsimulator/include/")
                    tasks[interopProcessingTaskName].dependsOn(":iOSLibs:build${library.capitalize()}Appletvsimulator")
                }
                "watchosArm32", "watchosArm64" -> {
                    includeDirs.headerFilterOnly("$rootDir/iOSLibs/$library/build/Release-watchos/include/")
                    tasks[interopProcessingTaskName].dependsOn(":iOSLibs:build${library.capitalize()}Watchos")
                }
                "watchosSimulatorArm64", "watchosX64" -> {
                    includeDirs.headerFilterOnly("$rootDir/iOSLibs/$library/build/Release-watchsimulator/include/")
                    tasks[interopProcessingTaskName].dependsOn(":iOSLibs:build${library.capitalize()}Watchsimulator")
                }
                else -> {
                    throw GradleException("MOUSSA-ERROR: $library - $platform - ERROR")
                }
            }
        }
    }
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
        ios {
            swiftCinterop("IOHKSecureRandomGeneration", name)
        }
        watchos {
            swiftCinterop("IOHKSecureRandomGeneration", name)
        }
        macosX64 {
            swiftCinterop("IOHKSecureRandomGeneration", name)
        }
        if (System.getProperty("os.arch") != "x86_64") { // M1Chip
            iosSimulatorArm64 {
                swiftCinterop("IOHKSecureRandomGeneration", name)
            }
            tvosArm64("tvos") {
                swiftCinterop("IOHKSecureRandomGeneration", name)
            }
            tvosSimulatorArm64 {
                swiftCinterop("IOHKSecureRandomGeneration", name)
            }
            watchosSimulatorArm64 {
                swiftCinterop("IOHKSecureRandomGeneration", name)
            }
            macosArm64 {
                swiftCinterop("IOHKSecureRandomGeneration", name)
            }
        }
    }
//    if (os.isWindows) {
//        mingwX64()
//    }
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
            this.webpackTask(
                Action {
                    this.output.library = currentModuleName
                    this.output.libraryTarget = Target.VAR
                }
            )
            this.testTask(
                Action {
                    this.useKarma {
                        this.useChromeHeadless()
                    }
                }
            )
        }
        nodejs {
            this.testTask(
                Action {
                    this.useKarma {
                        this.useChromeHeadless()
                    }
                }
            )
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":utils"))
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val jvmMain by getting
        val jvmTest by getting
        val androidMain by getting
        val androidTest by getting {
            dependencies {
                implementation("junit:junit:4.13.2")
            }
        }
        val jsMain by getting
        val jsTest by getting
        if (os.isMacOsX) {
            val iosMain by getting
            val iosTest by getting
            val watchosMain by getting {
                this.dependsOn(iosMain)
            }
            val watchosTest by getting {
                this.dependsOn(iosTest)
            }
            val macosX64Main by getting {
                this.dependsOn(iosMain)
            }
            val macosX64Test by getting {
                this.dependsOn(iosTest)
            }
            if (System.getProperty("os.arch") != "x86_64") { // M1Chip
                val iosSimulatorArm64Main by getting {
                    this.dependsOn(iosMain)
                }
                val iosSimulatorArm64Test by getting {
                    this.dependsOn(iosTest)
                }
                val tvosMain by getting {
                    this.dependsOn(iosMain)
                }
                val tvosTest by getting {
                    this.dependsOn(iosTest)
                }
                val tvosSimulatorArm64Main by getting {
                    this.dependsOn(tvosMain)
                }
                val tvosSimulatorArm64Test by getting {
                    this.dependsOn(tvosTest)
                }
                val watchosSimulatorArm64Main by getting {
                    this.dependsOn(watchosMain)
                }
                val watchosSimulatorArm64Test by getting {
                    this.dependsOn(watchosTest)
                }
                val macosArm64Main by getting {
                    this.dependsOn(macosX64Main)
                }
                val macosArm64Test by getting {
                    this.dependsOn(macosX64Test)
                }
            }
        }
//        if (os.isWindows) {
//            val mingwX64Main by getting
//            val mingwX64Test by getting
//        }
    }
}

android {
    compileSdk = 33
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
        This is a Kotlin Multiplatform Library is a secure random generation module
    """.trimIndent()
    dokkaSourceSets {
        // TODO: Figure out how to include files to the documentations
        named("commonMain") {
            includes.from("Module.md", "docs/Module.md")
        }
    }
}

tasks.matching {
    // Because runtime exit with code 134
    it.name.contains("tvosSimulatorArm64Test")
}.all {
    this.enabled = false
}
