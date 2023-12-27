import dev.petuska.npm.publish.extension.domain.NpmAccess
import org.gradle.internal.os.OperatingSystem
import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.kotlin.gradle.plugin.mpp.BitcodeEmbeddingMode
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackOutput.Target
import org.jetbrains.kotlin.gradle.tasks.CInteropProcess
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.net.URL
import java.time.Year

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

/**
 * The `javadocJar` variable is used to register a `Jar` task to generate a Javadoc JAR file.
 * The Javadoc JAR file is created with the classifier "javadoc" and it includes the HTML documentation generated
 * by the `dokkaHtml` task.
 */
val javadocJar by tasks.registering(Jar::class) {
    archiveClassifier.set("javadoc")
    from(tasks.dokkaHtml)
}

kotlin {
    androidTarget {
        publishAllLibraryVariants()
    }
    jvm {
        withSourcesJar()
        publishing {
            publications {
                withType<MavenPublication> {
                    artifact(javadocJar)
                }
            }
        }
        compilations.all {
            kotlinOptions {
                jvmTarget = "11"
            }
        }
        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
        }
    }
    iosArm64 {
        swiftCinterop("IOHKSecureRandomGeneration", name)
        swiftCinterop("IOHKCryptoKit", name)

        binaries.framework {
            baseName = "ApolloLibrary"
            embedBitcode(BitcodeEmbeddingMode.DISABLE)
        }
    }
    iosX64 {
        swiftCinterop("IOHKSecureRandomGeneration", name)
        swiftCinterop("IOHKCryptoKit", name)

        binaries.framework {
            baseName = "ApolloLibrary"
            embedBitcode(BitcodeEmbeddingMode.DISABLE)
            if (os.isMacOsX) {
                if (System.getenv().containsKey("XCODE_VERSION_MAJOR") && System.getenv("XCODE_VERSION_MAJOR") == "1500") {
                    linkerOpts += "-ld64"
                }
            }
        }
    }
    iosSimulatorArm64 {
        swiftCinterop("IOHKSecureRandomGeneration", name)
        swiftCinterop("IOHKCryptoKit", name)

        binaries.framework {
            baseName = "ApolloLibrary"
            embedBitcode(BitcodeEmbeddingMode.DISABLE)
        }
    }
    macosArm64 {
        swiftCinterop("IOHKSecureRandomGeneration", name)
        swiftCinterop("IOHKCryptoKit", name)

        binaries.framework {
            baseName = "ApolloLibrary"
            embedBitcode(BitcodeEmbeddingMode.DISABLE)
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
    applyDefaultHierarchyTemplate()

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
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.1")
                implementation("com.ionspin.kotlin:bignum:0.3.8")
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
                api("fr.acinq.secp256k1:secp256k1-kmp:0.11.0")
                implementation("fr.acinq.secp256k1:secp256k1-kmp-jni-jvm:0.11.0")
                implementation("fr.acinq.secp256k1:secp256k1-kmp-jni-android:0.11.0")
                implementation("com.google.guava:guava:30.1-jre")
                implementation("org.bouncycastle:bcprov-jdk15on:1.68")
                implementation("org.bitcoinj:bitcoinj-core:0.16.2")
            }
        }
        val androidUnitTest by getting {
            dependencies {
                implementation("junit:junit:4.13.2")
            }
        }
        val jvmMain by getting {
            dependencies {
                api("fr.acinq.secp256k1:secp256k1-kmp:0.11.0")
                implementation("fr.acinq.secp256k1:secp256k1-kmp-jni-jvm:0.11.0")
                implementation("com.google.guava:guava:30.1-jre")
                implementation("org.bouncycastle:bcprov-jdk15on:1.68")
                implementation("org.bitcoinj:bitcoinj-core:0.16.2")
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

        val appleMain by getting {
            dependencies {
                implementation(project(":secp256k1-kmp"))
            }
        }
    }

    // Enable the export of KDoc (Experimental feature) to Generated Native targets (Apple, Linux, etc.)
    targets.withType<org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget> {
        compilations.getByName("main") {
            compilerOptions.options.freeCompilerArgs.add("-Xexport-kdoc")
        }
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
    namespace = "io.iohk.atala.prism.apollo"
    compileSdk = 33
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = 21
        targetSdk = 33
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
tasks.withType<DokkaTask>().configureEach {
    moduleName.set(currentModuleName)
    moduleVersion.set(rootProject.version.toString())
    description = "This is a Kotlin Multiplatform Library for cryptography"
    pluginConfiguration<org.jetbrains.dokka.base.DokkaBase, org.jetbrains.dokka.base.DokkaBaseConfiguration> {
        customAssets = listOf(rootDir.resolve("Logo.png"))
        footerMessage = "(c) ${Year.now().value} IOG Copyright"
    }
    dokkaSourceSets {
        configureEach {
            jdkVersion.set(11)
            languageVersion.set("1.9.21")
            apiVersion.set("2.0")
            includes.from(
                "docs/Apollo.md",
                "docs/Base64.md",
                "docs/SecureRandom.md"
            )
            sourceLink {
                localDirectory.set(projectDir.resolve("src"))
                remoteUrl.set(URL("https://github.com/input-output-hk/atala-prism-apollo/tree/main/src"))
                remoteLineSuffix.set("#L")
            }
            externalDocumentationLink {
                url.set(URL("https://kotlinlang.org/api/latest/jvm/stdlib/"))
            }
            externalDocumentationLink {
                url.set(URL("https://kotlinlang.org/api/kotlinx.serialization/"))
            }
            externalDocumentationLink {
                url.set(URL("https://api.ktor.io/"))
            }
            externalDocumentationLink {
                url.set(URL("https://kotlinlang.org/api/kotlinx-datetime/"))
                packageListUrl.set(URL("https://kotlinlang.org/api/kotlinx-datetime/"))
            }
            externalDocumentationLink {
                url.set(URL("https://kotlinlang.org/api/kotlinx.coroutines/"))
            }
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
            readme.set(rootDir.resolve("README.md"))
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

// Workaround for a bug in Gradle
afterEvaluate {
    tasks.named("lintAnalyzeDebug") {
        this.enabled = false
    }
    tasks.named("lintAnalyzeRelease") {
        this.enabled = false
    }
    tasks.named("iosX64Test") {
        this.enabled = false
    }
    tasks.withType<PublishToMavenRepository> {
        dependsOn(tasks.withType<Sign>())
    }
    // Disable publish of targets
    if (os.isMacOsX) {
        if (tasks.findByName("publishIosX64PublicationToSonatypeRepository") != null) {
            tasks.named("publishIosX64PublicationToSonatypeRepository") {
                this.enabled = false
            }
        }
        if (tasks.findByName("publishIosArm64PublicationToSonatypeRepository") != null) {
            tasks.named("publishIosArm64PublicationToSonatypeRepository") {
                this.enabled = false
            }
        }
        if (tasks.findByName("publishIosSimulatorArm64PublicationToSonatypeRepository") != null) {
            tasks.named("publishIosSimulatorArm64PublicationToSonatypeRepository") {
                this.enabled = false
            }
        }
        if (tasks.findByName("publishMacosArm64PublicationToSonatypeRepository") != null) {
            tasks.named("publishMacosArm64PublicationToSonatypeRepository") {
                this.enabled = false
            }
        }
    }
    if (tasks.findByName("publishJsPublicationToSonatypeRepository") != null) {
        tasks.named("publishJsPublicationToSonatypeRepository") {
            this.enabled = false
        }
    }
}
