import org.gradle.internal.os.OperatingSystem
import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackOutput.Target

val currentModuleName = "ApolloBase32"
val os: OperatingSystem = OperatingSystem.current()

plugins {
    kotlin("multiplatform")
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
//        macosX64()
        iosSimulatorArm64()
//            tvosSimulatorArm64()
//            watchosSimulatorArm64()
        macosArm64()
    }
//    if (os.isWindows) {
//        mingwX64()
//    }
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
//                    this.enabled = true
//                }
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

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("com.ionspin.kotlin:bignum:0.3.7")
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
//            val tvosMain by getting
//            val tvosTest by getting
//            val watchosMain by getting
//            val watchosTest by getting
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
            val macosArm64Main by getting
            val macosArm64Test by getting
        }
//        if (os.isWindows) {
//            // val mingwX86Main by getting // it depend on kotlinx-datetime lib to support this platform before we can support it as well
//            // val mingwX86Test by getting // it depend on kotlinx-datetime lib to support this platform before we can support it as well
//            val mingwX64Main by getting
//            val mingwX64Test by getting
//        }
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
    description = "This is a Kotlin Multiplatform Library for Base32"
    dokkaSourceSets {
        // TODO: Figure out how to include files to the documentations
        named("commonMain") {
            includes.from("Module.md", "docs/Module.md")
        }
    }
}
