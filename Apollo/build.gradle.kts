import org.gradle.internal.os.OperatingSystem
import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackOutput.Target

version = rootProject.version
val moduleName = "Apollo"
val os = OperatingSystem.current()

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
        iosSimulatorArm64()
    }
    js(IR) {
        this.moduleName = moduleName
        this.binaries.executable()
        this.useCommonJs()
        this.compilations["main"].packageJson {
            this.version = rootProject.version.toString()
        }
        this.compilations["test"].packageJson {
            this.version = rootProject.version.toString()
        }
        browser {
            this.webpackTask {
                this.output.library = moduleName
                this.output.libraryTarget = Target.VAR
            }
            this.commonWebpackConfig {
                this.cssSupport.enabled = true
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

    if (os.isMacOsX) {
        cocoapods {
            this.summary = "Apollo is a collection of the cryptographic methods used all around Atala PRISM"
            this.version = rootProject.version.toString()
            this.authors = "IOG"
            this.ios.deploymentTarget = "13.0"
            framework {
                this.baseName = moduleName
            }
        }
    }
    
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":Hashing"))
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val jvmMain by getting
        val jvmTest by getting {
            dependencies {
                implementation("junit:junit:4.13.2")
            }
        }
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
            val iosSimulatorArm64Main by getting {
                this.dependsOn(iosMain)
            }
            val iosSimulatorArm64Test by getting {
                this.dependsOn(iosTest)
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
        This is a Kotlin Multiplatform Library for cryptography
    """.trimIndent()
    dokkaSourceSets {
        // TODO: Figure out how to include files to the documentations
        named("commonMain") {
            includes.from("Module.md", "docs/Module.md")
        }
    }
}

//afterEvaluate {
//    tasks.withType<AbstractTestTask> {
//        testLogging {
//            events("passed", "skipped", "failed", "standard_out", "standard_error")
//            showExceptions = true
//            showStackTraces = true
//        }
//    }
//}