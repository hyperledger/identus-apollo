import org.gradle.internal.os.OperatingSystem
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

val os: OperatingSystem = OperatingSystem.current()

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.dokka")
}

fun KotlinNativeTarget.secp256k1CInterop(target: String) {
    compilations.getByName("main").cinterops {
        val libsecp256k1 by creating {
            includeDirs.headerFilterOnly(project.file("native/secp256k1/include/"))
            tasks[interopProcessingTaskName].dependsOn(":secp256k1-kmp:native:buildSecp256k1${target.capitalize()}")
        }
    }
}

kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "1.8"
        }
    }

//    if (os.isLinux) {
//        linuxX64("linux") {
//            secp256k1CInterop("host")
//            // https://youtrack.jetbrains.com/issue/KT-39396
//            compilations.getByName("main").kotlinOptions.freeCompilerArgs += listOf("-include-binary", "$rootDir/secp256k1-kmp/native/build/linux/libsecp256k1.a")
//        }
//    }

    if (os.isMacOsX) {
        ios {
            secp256k1CInterop("ios")
            when (this.name) {
                "iosArm64" -> {
                    // https://youtrack.jetbrains.com/issue/KT-39396
                    compilations.getByName("main").kotlinOptions.freeCompilerArgs += listOf("-include-binary", "$rootDir/secp256k1-kmp/native/build/ios/arm64-iphoneos/libsecp256k1.a")
                }
                "iosX64" -> {
                    // https://youtrack.jetbrains.com/issue/KT-39396
                    compilations.getByName("main").kotlinOptions.freeCompilerArgs += listOf("-include-binary", "$rootDir/secp256k1-kmp/native/build/ios/x86_x64-iphonesimulator/libsecp256k1.a")
                }
                else -> {
                    throw GradleException("$name is not supported")
                }
            }
        }

        macosX64 {
            secp256k1CInterop("macosX64")
            // https://youtrack.jetbrains.com/issue/KT-39396
            compilations.getByName("main").kotlinOptions.freeCompilerArgs += listOf("-include-binary", "$rootDir/secp256k1-kmp/native/build/ios/x86_x64-macosx/libsecp256k1.a")
        }

        if (System.getProperty("os.arch") != "x86_64") { // M1Chip
            iosSimulatorArm64 {
                secp256k1CInterop("iosSimulatorArm64")
                // https://youtrack.jetbrains.com/issue/KT-39396
                compilations.getByName("main").kotlinOptions.freeCompilerArgs += listOf("-include-binary", "$rootDir/secp256k1-kmp/native/build/ios/arm64-iphonesimulator/libsecp256k1.a")
            }
//            watchosSimulatorArm64 {
//                secp256k1CInterop("ios")
//                // https://youtrack.jetbrains.com/issue/KT-39396
//                compilations.getByName("main").kotlinOptions.freeCompilerArgs += listOf("-include-binary", "$rootDir/native/build/ios/arm64_x86_x64-watchossimulator.a")
//            }
//            tvosArm64 {
//                secp256k1CInterop("tvOSArm64")
//                // https://youtrack.jetbrains.com/issue/KT-39396
//                compilations.getByName("main").kotlinOptions.freeCompilerArgs += listOf("-include-binary", "$rootDir/secp256k1-kmp/native/build/ios/arm64-appletvos/libsecp256k1.a")
//            }
            macosArm64 {
                secp256k1CInterop("macosArm64")
                // https://youtrack.jetbrains.com/issue/KT-39396
                compilations.getByName("main").kotlinOptions.freeCompilerArgs += listOf("-include-binary", "$rootDir/secp256k1-kmp/native/build/ios/arm64-macosx/libsecp256k1.a")
            }
        }
    }

    sourceSets {
        val commonMain by getting
        val commonTest by getting
        val nativeMain by creating {
            this.dependsOn(commonMain)
        }
        if (os.isMacOsX) {
            val iosMain by getting {
                this.dependsOn(nativeMain)
            }
            val iosTest by getting
//            val tvosMain by getting {
//                this.dependsOn(iosMain)
//            }
//            val tvosTest by getting
//            val watchosMain by getting {
//                this.dependsOn(iosMain)
//            }
//            val watchosTest by getting
            val macosX64Main by getting {
                this.dependsOn(nativeMain)
            }
            val macosX64Test by getting
            if (System.getProperty("os.arch") != "x86_64") { // M1Chip
                val iosSimulatorArm64Main by getting {
                    this.dependsOn(iosMain)
                }
                val iosSimulatorArm64Test by getting {
                    this.dependsOn(iosTest)
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
    }
}
