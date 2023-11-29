import dev.petuska.npm.publish.extension.domain.NpmAccess
import org.gradle.internal.os.OperatingSystem
import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackOutput.Target
import org.jetbrains.kotlin.gradle.tasks.CInteropProcess
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val currentModuleName: String = "Apollo"
val os: OperatingSystem = OperatingSystem.current()

plugins {
    kotlin("multiplatform")
    id("io.github.luca992.multiplatform-swiftpackage") version "2.0.5-arm64"
    id("com.android.library")
    id("org.jetbrains.dokka")
    id("dev.petuska.npm.publish") version "3.4.1"
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
    ios {
        swiftCinterop("IOHKSecureRandomGeneration", name)
        swiftCinterop("IOHKCryptoKit", name)

        binaries.framework {
            baseName = "ApolloLibrary"
        }
    }
//    macosX64 {
//        swiftCinterop("IOHKSecureRandomGeneration", name)
//        swiftCinterop("IOHKCryptoKit", name)
//
//        binaries.framework {
//            baseName = "ApolloLibrary"
//        }
//    }
    // Mx Chip
    if (System.getProperty("os.arch") != "x86_64") {
        macosArm64 {
            swiftCinterop("IOHKSecureRandomGeneration", name)
            swiftCinterop("IOHKCryptoKit", name)

            binaries.framework {
                baseName = "ApolloLibrary"
            }
        }
        iosSimulatorArm64 {
            swiftCinterop("IOHKSecureRandomGeneration", name)
            swiftCinterop("IOHKCryptoKit", name)

            binaries.framework {
                baseName = "ApolloLibrary"
            }
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
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")
                implementation("com.ionspin.kotlin:bignum:0.3.7")
                implementation("org.kotlincrypto.macs:hmac-sha2:0.3.0")
                implementation("org.kotlincrypto.hash:sha2:0.3.0")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val androidMain by getting {
            dependencies {
                api("fr.acinq.secp256k1:secp256k1-kmp:0.9.0")
                implementation("fr.acinq.secp256k1:secp256k1-kmp-jni-jvm:0.9.0")
                implementation("fr.acinq.secp256k1:secp256k1-kmp-jni-android:0.9.0")
                implementation("com.google.guava:guava:30.1-jre")
                implementation("org.bouncycastle:bcprov-jdk15on:1.68")
                implementation("org.bitcoinj:bitcoinj-core:0.15.10")
            }
        }
        val androidTest by getting {
            dependencies {
                implementation("junit:junit:4.13.2")
            }
        }
        val jvmMain by getting {
            dependencies {
                api("fr.acinq.secp256k1:secp256k1-kmp:0.9.0")
                implementation("fr.acinq.secp256k1:secp256k1-kmp-jni-jvm:0.9.0")
                implementation("com.google.guava:guava:30.1-jre")
                implementation("org.bouncycastle:bcprov-jdk15on:1.68")
                implementation("org.bitcoinj:bitcoinj-core:0.15.10")
            }
        }
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
                implementation(npm("hash.js", "1.1.7"))
                implementation(npm("@noble/hashes", "1.3.1", true))

                // Polyfill dependencies
                implementation(npm("stream-browserify", "3.0.0"))
                implementation(npm("buffer", "6.0.3"))

                implementation("org.jetbrains.kotlin-wrappers:kotlin-web:1.0.0-pre.461")
                implementation("org.jetbrains.kotlin-wrappers:kotlin-node:18.11.13-pre.461")
            }
        }
        val jsTest by getting

        if (os.isMacOsX) {
            val appleMain by creating {
                dependsOn(commonMain)
                dependencies {
                    implementation(project(":secp256k1-kmp"))
                }
            }
            val appleTest by creating {
                dependsOn(commonTest)
            }
            val iosMain by getting {
                dependsOn(appleMain)
            }
            val iosTest by getting {
                dependsOn(appleTest)
            }
//            val macosX64Main by getting {
//                dependsOn(appleMain)
//            }
//            val macosX64Test by getting {
//                dependsOn(appleTest)
//            }
            // Mx Chip
            if (System.getProperty("os.arch") != "x86_64") {
                val iosSimulatorArm64Main by getting {
                    dependsOn(appleMain)
                }
                val iosSimulatorArm64Test by getting {
                    dependsOn(appleTest)
                }
                val macosArm64Main by getting {
                    dependsOn(appleMain)
                }
                val macosArm64Test by getting {
                    dependsOn(appleTest)
                }
            }
        }
    }

    if (os.isMacOsX) {
        tasks.getByName<org.jetbrains.kotlin.gradle.targets.native.tasks.KotlinNativeSimulatorTest>("iosX64Test") {
            device.set("iPhone 14 Plus")
        }
        // Mx Chip
        if (System.getProperty("os.arch") != "x86_64") {
            tasks.getByName<org.jetbrains.kotlin.gradle.targets.native.tasks.KotlinNativeSimulatorTest>("iosSimulatorArm64Test") {
                device.set("iPhone 14 Plus")
            }
        }
    }
}

android {
    namespace = "io.iohk.atala.prism.apollo"
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

afterEvaluate {
    tasks.withType<KotlinCompile> {
        dependsOn(
            ":iOSLibs:buildIOHKCryptoKitIphoneos",
            ":iOSLibs:buildIOHKCryptoKitIphonesimulator",
            ":iOSLibs:buildIOHKCryptoKitMacosx",
            ":iOSLibs:buildIOHKSecureRandomGenerationIphoneos",
            ":iOSLibs:buildIOHKSecureRandomGenerationIphonesimulator",
            ":iOSLibs:buildIOHKSecureRandomGenerationMacosx"
        )
    }
    tasks.withType<ProcessResources> {
        dependsOn(
            ":iOSLibs:buildIOHKCryptoKitIphoneos",
            ":iOSLibs:buildIOHKCryptoKitIphonesimulator",
            ":iOSLibs:buildIOHKCryptoKitMacosx",
            ":iOSLibs:buildIOHKSecureRandomGenerationIphoneos",
            ":iOSLibs:buildIOHKSecureRandomGenerationIphonesimulator",
            ":iOSLibs:buildIOHKSecureRandomGenerationMacosx"
        )
    }
    tasks.withType<CInteropProcess> {
        dependsOn(
            ":iOSLibs:buildIOHKCryptoKitIphoneos",
            ":iOSLibs:buildIOHKCryptoKitIphonesimulator",
            ":iOSLibs:buildIOHKCryptoKitMacosx",
            ":iOSLibs:buildIOHKSecureRandomGenerationIphoneos",
            ":iOSLibs:buildIOHKSecureRandomGenerationIphonesimulator",
            ":iOSLibs:buildIOHKSecureRandomGenerationMacosx"
        )
    }
}

ktlint {
    filter {
        exclude("**/external/*", "./src/jsMain/kotlin/io/iohk/atala/prism/apollo/utils/external/*")
        exclude {
            it.file.toString().contains("external")
        }
        exclude { projectDir.toURI().relativize(it.file.toURI()).path.contains("/external/") }
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
    organization.set("atala")
    version.set(rootProject.version.toString())
    access.set(NpmAccess.PUBLIC)
    packages {
        access.set(NpmAccess.PUBLIC)
        named("js") {
            scope.set("atala")
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
        register("npmjs") {
            uri.set("https://registry.npmjs.org")
            authToken.set(System.getenv("ATALA_NPM_TOKEN"))
        }
    }
}
