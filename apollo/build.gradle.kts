import org.gradle.internal.os.OperatingSystem
import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackOutput.Target

val currentModuleName: String = "Apollo"
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
        watchos()
        macosX64()
        if (System.getProperty("os.arch") != "x86_64") { // M1Chip
            iosSimulatorArm64()
            tvosArm64("tvos")
            tvosSimulatorArm64()
            watchosSimulatorArm64()
            macosArm64()
        }
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
        val jsMain by getting
        val jsTest by getting
        if (os.isMacOsX) {
            val iosMain by getting
            val iosTest by getting
            val watchosMain by getting
            val watchosTest by getting
            val macosX64Main by getting
            val macosX64Test by getting
            if (System.getProperty("os.arch") != "x86_64") { // M1Chip
                val iosSimulatorArm64Main by getting {
                    this.dependsOn(iosMain)
                }
                val iosSimulatorArm64Test by getting {
                    this.dependsOn(iosTest)
                }
                val tvosMain by getting
                val tvosTest by getting
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
        This is a Kotlin Multiplatform Library for cryptography
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
